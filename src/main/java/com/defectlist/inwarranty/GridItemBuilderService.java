package com.defectlist.inwarranty;

import com.amazonaws.HttpMethod;
import com.defectlist.inwarranty.configuration.CacheType;
import com.defectlist.inwarranty.connector.ServitiumCrmConnector;
import com.defectlist.inwarranty.model.GridItem;
import com.defectlist.inwarranty.ui.LineImage;
import com.defectlist.inwarranty.ui.UIFactory;
import com.defectlist.inwarranty.utils.ListUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class GridItemBuilderService {
    private static final Logger LOGGER = getLogger(InwarrantyDefectItemService.class);

    private static final int PARTITION_SIZE = 3;
    private static final int THREAD_POOL_SIZE = 5;
    private static final int MAX_TRY_COUNT = 4;
    private static final String DELIMITER_COMMA = ",";


    private final CacheService cacheService;

    private final S3Service s3Service;

    private final UIFactory uiFactory;

    private final ServitiumCrmConnector servitiumCrmConnector;

    private final GridItemFactory gridItemFactory;

    private final String bucketName;

    private final boolean enableAsync;

    @Autowired
    public GridItemBuilderService(final CacheService cacheService, final S3Service s3Service,
                                  final UIFactory uiFactory,
                                  final ServitiumCrmConnector servitiumCrmConnector,
                                  final GridItemFactory gridItemFactory,
                                  @Value("${aws.s3.captcha-bucket}") final String bucketName,
                                  @Value("${servitium.jobsheets.enable-async}") final boolean enableAsync) {
        this.cacheService = cacheService;
        this.s3Service = s3Service;
        this.uiFactory = uiFactory;
        this.gridItemFactory = gridItemFactory;
        this.bucketName = bucketName;
        this.enableAsync = enableAsync;
        this.servitiumCrmConnector = servitiumCrmConnector;
    }

    public GridItem getJobSheet(final String spareName, final String complaintId, final String loggedInUserName) {
        final Optional<GridItem> gridItemFromCache = cacheService.get(CacheType.GRID_ITEM.getCacheName(),
                spareName + "-" + complaintId, GridItem.class);
        return gridItemFromCache.orElseGet(() -> getJobSheet(spareName, complaintId, 1, loggedInUserName));
    }

    public URL generatePresignedUrl(final LineImage type) {
        Optional<URL> urlFromCache = cacheService.get(CacheType.LINE_URL.getCacheName(), type.getName(), URL.class);
        if (urlFromCache.isPresent()) {
            return urlFromCache.get();
        }
        URL url = s3Service.generatePresignedUrl(bucketName, type.getName(), Date.from(Instant.now().plusSeconds(300)), HttpMethod.GET);
        cacheService.put(CacheType.LINE_URL.getCacheName(), type.getName(), url);
        return url;
    }

    public String getGridItems(final Map<String, String> callIds, final String loggedInUserName) {
        final Map<String, List<GridItem>> gridItemsMap = buildGridItems(callIds, loggedInUserName);
        final URL verticleImage = s3Service
                .generatePresignedUrl(bucketName, "cut-vertical.jpg", Date.from(Instant.now().plusSeconds(300)), HttpMethod.GET);
        final URL horizontalImage = s3Service
                .generatePresignedUrl(bucketName, "cut.jpg", Date.from(Instant.now().plusSeconds(300)), HttpMethod.GET);
        return uiFactory.buildGridPage(gridItemsMap, verticleImage, horizontalImage);
    }

    public String getGridItems(final Map<String, String> callIds,
                               final Map<String, String> nextCallIds,
                               final String loggedInUserName,
                               final int pageNumber) {
        try {
            final String uiPage = getGridItems(callIds, loggedInUserName);
            final String nextCallIdsString = new ObjectMapper().writeValueAsString(nextCallIds);
            final String nextPageButton;
            final boolean isEmptyMap = nextCallIds.values().stream()
                    .allMatch(String::isEmpty);
            if (isEmptyMap) {
                nextPageButton = "";
            } else {
                nextPageButton =uiFactory.buildNextPageButton(nextCallIdsString, null,
                        loggedInUserName, pageNumber);
            }
            return uiPage + nextPageButton;
        } catch (final Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    public Map<String, List<GridItem>> buildGridItems(final Map<String, String> callIds, final String loggedInUserName) {
        final Map<String, List<GridItem>> gridItemsMap = new HashMap<>();

        callIds.forEach((key, value) -> {
            final List<String> complaintIds = Arrays.asList(value.split(DELIMITER_COMMA));
            final List<GridItem> gridItems = enableAsync
                    ? getJobSheetsAsync(complaintIds, key, loggedInUserName)
                    : complaintIds.stream()
                    .filter(complaintId -> complaintId.length() == 12)
                    .map(complaintId -> getJobSheet(key, complaintId, loggedInUserName))
                    .collect(Collectors.toList());
            gridItemsMap.put(key, gridItems);
        });

        return gridItemsMap;
    }

    private List<GridItem> getJobSheetsAsync(final List<String> complaintIds, final String key, final String loggedInUserName) {
        final List<List<String>> complaintIdPartitions = ListUtils.partition(complaintIds, Math.min(complaintIds.size(), PARTITION_SIZE));
        final ExecutorService executorService = Executors.newFixedThreadPool(Math.min(complaintIdPartitions.size(), THREAD_POOL_SIZE));

        final List<CompletableFuture<List<GridItem>>> gridItemsToBeFetched = complaintIdPartitions.stream()
                .map(partition -> CompletableFuture.supplyAsync(
                        () -> partition.stream()
                                .filter(complaintId -> complaintId.length() == 12)
                                .map(complaintId -> getJobSheet(key, complaintId, loggedInUserName))
                                .collect(Collectors.toList()), executorService))
                .collect(Collectors.toList());

        final List<List<GridItem>> gridItemsPartitions = gridItemsToBeFetched.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        executorService.shutdown();

        return gridItemsPartitions.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private GridItem getJobSheet(final String spareName, final String complaintId, final int tryCount,
                                 final String loggedInUserName) {
        LOGGER.info("Get jobsheet, complaintId : {}, tryCount : {}", complaintId, tryCount);
        final String response = servitiumCrmConnector.getJobSheet(complaintId);
        LOGGER.info("Found jobsheet, complaintId : {}, tryCount : {}", complaintId, tryCount);
        final GridItem gridItem = gridItemFactory.buildGridItem(complaintId, spareName, response, loggedInUserName);
        if (!validGridItem(gridItem) && tryCount < MAX_TRY_COUNT) {
            return getJobSheet(spareName, complaintId, tryCount + 1, loggedInUserName);
        }
        if (validGridItem(gridItem)) {
            cacheService.put(CacheType.GRID_ITEM.getCacheName(), spareName + "-" + complaintId, gridItem);
        }
        return gridItem;
    }

    private boolean validGridItem(final GridItem gridItem) {
        return gridItem.getModel() != null && gridItem.getProduct() != null && gridItem.getSerialNumber() != null;
    }


}
