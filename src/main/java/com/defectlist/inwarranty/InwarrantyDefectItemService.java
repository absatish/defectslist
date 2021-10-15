package com.defectlist.inwarranty;

import com.amazonaws.HttpMethod;
import com.defectlist.inwarranty.configuration.CacheType;
import com.defectlist.inwarranty.connector.ServitiumCrmConnector;
import com.defectlist.inwarranty.httprequestheaders.ContentRequest;
import com.defectlist.inwarranty.httprequestheaders.LoginRequest;
import com.defectlist.inwarranty.model.CaptchaResponse;
import com.defectlist.inwarranty.model.DefectivePartType;
import com.defectlist.inwarranty.model.GridItem;
import com.defectlist.inwarranty.utils.ListUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class InwarrantyDefectItemService {

    private static final Logger LOGGER = getLogger(InwarrantyDefectItemService.class);

    private static final String JSESSION_ID = "jSessionId";
    private static final String SERVER_ID = "serverId";

    private static final String DELIMITER_SEMICOLON = ";";
    private static final String DELIMITER_COMMA = ",";

    private static final String JSESSION_ID_PREFIX = "JSESSIONID=";
    private static final String SERVER_ID_PREFIX = "SERVERID=";

    private static final String KEY_NAME = "captcha.jpg";

    private static final int PARTITION_SIZE = 3;
    private static final int THREAD_POOL_SIZE = 5;

    private final ServitiumCrmConnector servitiumCrmConnector;

    private final S3Service s3Service;

    private final CacheService cacheService;

    private final String bucketName;

    private final UIFactory uiFactory;

    private final GridItemFactory gridItemFactory;

    @Autowired
    public InwarrantyDefectItemService(final ServitiumCrmConnector servitiumCrmConnector,
                                       final S3Service s3Service,
                                       final CacheService cacheService,
                                       @Value("${aws.s3.captcha-bucket}") final String bucketName,
                                       final UIFactory uiFactory,
                                       final GridItemFactory gridItemFactory) {
        this.servitiumCrmConnector = servitiumCrmConnector;
        this.s3Service = s3Service;
        this.cacheService = cacheService;
        this.bucketName = bucketName;
        this.uiFactory = uiFactory;
        this.gridItemFactory = gridItemFactory;
    }

    public String getDocuments() {
        final String callIds = "B21090200621,B21083100497,B21083100722,B21090100328,B21090200618,B21090202365,B21090200960," +
                "B21090200621,B21083100497,B21083100722,B21090100328,B21090200618,B21090202365,B21090200960," +
                "B21090200621,B21083100497,B21083100722,B21090100328,B21090200618,B21090202365,B21090200960," +
                "B21090200621,B21083100497,B21083100722,B21090100328,B21090200618,B21090202365,B21090200960," +
                "B21090200621,B21083100497,B21083100722,B21090100328,B21090200618,B21090202365,B21090200960," +
                "B21090200621,B21083100497,B21083100722,B21090100328,B21090200618,B21090202365,B21090200960," +
                "B21090200621,B21083100497,B21083100722,B21090100328,B21090200618,B21090202365,B21090200960," +
                "B21090200621,B21083100497,B21083100722,B21090100328,B21090200618,B21090202365,B21090200960," +
                "B21090200621,B21083100497,B21083100722,B21090100328,B21090200618,B21090202365,B21090200960," +
                "B21090200621,B21083100497,B21083100722,B21090100328,B21090200618,B21090202365,B21090200960," +
                "B21090200621,B21083100497,B21083100722,B21090100328,B21090200618,B21090202365,B21090200960," +
                "B21090200621,B21083100497,B21083100722,B21090100328,B21090200618,B21090202365,B21090200960";
        final Map<String, String> map =  Map.of(DefectivePartType.MOTOR.getPartType(), callIds);
        return getGridItems(map);
    }

    public String getContent() {
        final Optional<String> jSessionIdFromCache = cacheService.get(CacheType.SESSION.getCacheName(), JSESSION_ID, String.class);
        final Optional<String> serverIdFromCache = cacheService.get(CacheType.SESSION.getCacheName(), SERVER_ID, String.class);
        return servitiumCrmConnector.readContentFromServitiumCrm(new ContentRequest(jSessionIdFromCache.get(), serverIdFromCache.get()));
    }

    public String login(final LoginRequest loginRequest) {
        if (HttpStatus.OK.equals(servitiumCrmConnector.login(loginRequest))) {
            return getCallIds();
        } else {
            return "<font color=red>Something went wrong. please try logging again..!</font><br><hr>" + getPreload();
        }
    }

    public String getPreload() {

        final Optional<String> jSessionIdFromCache = cacheService.get(CacheType.SESSION.getCacheName(), JSESSION_ID, String.class);
        final Optional<String> serverIdFromCache = cacheService.get(CacheType.SESSION.getCacheName(), SERVER_ID, String.class);

        final String jSessionId;
        final String serverId;

        if (jSessionIdFromCache.isEmpty()) {
            LOGGER.info("No session values found in cache. retrieve fresh values");
            final CaptchaResponse captchaResponse = servitiumCrmConnector.getHttpHeaders();
            s3Service.upload(bucketName, KEY_NAME, captchaResponse.getImageBytes());
            final List<String> cookies = captchaResponse.getHttpHeaders().getValuesAsList("set-cookie");
            try {
                jSessionId = cookies.get(0).substring(JSESSION_ID_PREFIX.length()).split(DELIMITER_SEMICOLON)[0];
                serverId = cookies.get(1).substring(SERVER_ID_PREFIX.length()).split(DELIMITER_SEMICOLON)[0];
            } catch (final Exception exception) {
                LOGGER.error("An exception occurred while trying to fetch headers.", exception);
                return "<font color=red>Something went wrong..! Please try again after few minutes..!</font><br>";
            }
            LOGGER.info("put session values into cache, jSessionId : {}", jSessionId);
            LOGGER.info("put session values into cache, serverId : {}", serverId);
            cacheService.put(CacheType.SESSION.getCacheName(), JSESSION_ID, jSessionId);
            cacheService.put(CacheType.SESSION.getCacheName(), SERVER_ID, serverId);
        } else {
            jSessionId = jSessionIdFromCache.get();
            serverId = serverIdFromCache.get();
            LOGGER.info("session values fetched from cache : jSessionId : {}, serverId : {}", jSessionId, serverId);
        }
        final URL url = s3Service.generatePresignedUrl(bucketName, KEY_NAME, Date.from(Instant.now().plusSeconds(300)), HttpMethod.GET);
        return uiFactory.getLoginPage(jSessionId, serverId, url);
    }

    public GridItem getJobSheet(final String spareName, final String compalintId) {
        LOGGER.info("Get jobsheet, complaintId : {}", compalintId);
        final String response = servitiumCrmConnector.getJobSheet(compalintId);
        LOGGER.info("Found jobsheet, complaintId : {}", compalintId);
        return gridItemFactory.buildGridItem(compalintId, spareName, response);
    }

    private String getCallIds() {
        final String responseBody = getContent();
        final String lineRegex = "<input type='hidden' size='50' name = 'Call_Id";
        final String[] data = responseBody.split("\n");
        final Map<String, String> callIds = new HashMap<>();
        Arrays.stream(DefectivePartType.values()).forEach(partType -> callIds.put(partType.getPartType(), ""));
        int localLineCount = -1;
        String tempCallId = "";
        boolean started = false;
        for(String line : data) {
            if (line.contains(lineRegex)) {
                final String billLine = line.split(lineRegex)[0];
                tempCallId = billLine.substring(billLine.length() - 12);
                started = true;
                localLineCount = 0;
            }
            if (localLineCount == 6 && started) {
                final DefectivePartType partType = resolvePartType(line);
                final String delimiter = callIds.get(partType.getPartType()).isBlank() ? "" : DELIMITER_COMMA;
                callIds.put(partType.getPartType(), callIds.get(partType.getPartType()).concat(delimiter + tempCallId));
                tempCallId = "";
                localLineCount = -1;
                started = false;
            }
            localLineCount++;
        }
        final String gridItems = getGridItems(callIds);
        LOGGER.info("Evicting cache");
        cacheService.evict(CacheType.SESSION.getCacheName(), JSESSION_ID);
        cacheService.evict(CacheType.SESSION.getCacheName(), SERVER_ID);
        LOGGER.info("Evicted cache");
        return gridItems;
    }

    private String getGridItems(final Map<String, String> callIds) {
        final Map<String, List<GridItem>> gridItemsMap = new HashMap<>();
        callIds.forEach((key, value) -> {
            final List<String> complaintIds = Arrays.asList(value.split(DELIMITER_COMMA));

//            final List<List<String>> complaintIdPartitions = ListUtils.partition(complaintIds, Math.min(complaintIds.size(), PARTITION_SIZE));
//            final ExecutorService executorService = Executors.newFixedThreadPool(Math.min(complaintIdPartitions.size(), THREAD_POOL_SIZE));
//            final List<CompletableFuture<List<GridItem>>> gridItemsToBeFetched = complaintIdPartitions.stream()
//                    .map(partition -> CompletableFuture.supplyAsync(
//                            () -> partition.stream()
//                                    .filter(complaintId -> complaintId.length() == 12)
//                                    .map(complaintId -> getJobSheet(key, complaintId))
//                                    .collect(Collectors.toList()), executorService))
//                    .collect(Collectors.toList());
//            final List<List<GridItem>> gridItemsPartitions = gridItemsToBeFetched.stream()
//                    .map(CompletableFuture::join)
//                    .collect(Collectors.toList());
//            executorService.shutdown();
//            final List<GridItem> gridItems = gridItemsPartitions.stream()
//                    .flatMap(List::stream)
//                    .sorted(Comparator.comparingInt(item -> DefectivePartType.getPartTypeByName(item.getSpareName()).getSortOrder()))
//                    .collect(Collectors.toList());

            final List<GridItem> gridItems = complaintIds.stream()
                    .filter(complaintId -> complaintId.length() == 12)
                    .map(complaintId -> getJobSheet(key, complaintId))
                    .collect(Collectors.toList());
            gridItemsMap.put(key, gridItems);
        });
        final URL verticleImage = s3Service
                .generatePresignedUrl(bucketName, "cut-vertical.jpg", Date.from(Instant.now().plusSeconds(300)), HttpMethod.GET);
        final URL horizontalImage = s3Service
                .generatePresignedUrl(bucketName, "cut.jpg", Date.from(Instant.now().plusSeconds(300)), HttpMethod.GET);
        return uiFactory.buildGridPage(gridItemsMap, verticleImage, horizontalImage);
    }

    private DefectivePartType resolvePartType(final String line) {
        final String typeFromLine = line.split("<input")[0]
                .split("wrap >")[1]
                .replaceAll("no", "");
        return Arrays.stream(DefectivePartType.values())
                .filter(type -> type.matches(typeFromLine))
                .findFirst()
                .orElse(DefectivePartType.OTHER);
    }
}
