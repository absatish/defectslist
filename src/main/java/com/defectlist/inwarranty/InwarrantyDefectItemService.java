package com.defectlist.inwarranty;

import com.amazonaws.HttpMethod;
import com.defectlist.inwarranty.aop.EmailServiceEnabled;
import com.defectlist.inwarranty.configuration.CacheType;
import com.defectlist.inwarranty.connector.ServitiumCrmConnector;
import com.defectlist.inwarranty.exception.InvalidLoginRequestException;
import com.defectlist.inwarranty.exception.UnknownException;
import com.defectlist.inwarranty.httprequestheaders.ContentRequest;
import com.defectlist.inwarranty.httprequestheaders.LoginRequest;
import com.defectlist.inwarranty.httprequestheaders.LogoutRequest;
import com.defectlist.inwarranty.model.CaptchaResponse;
import com.defectlist.inwarranty.model.DefectivePartType;
import com.defectlist.inwarranty.model.GridItem;
import com.defectlist.inwarranty.ui.LineImage;
import com.defectlist.inwarranty.ui.UIFactory;
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

import static com.defectlist.inwarranty.ui.LineImage.HORIZONTAL_LINE_IMAGE;
import static com.defectlist.inwarranty.ui.LineImage.VERTICAL_LINE_IMAGE;
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

    private static final int MAX_TRY_COUNT = 4;

    private static final String LINE_REGEX = "<input type='hidden' size='50' name = 'Call_Id";

    private final ServitiumCrmConnector servitiumCrmConnector;

    private final S3Service s3Service;

    private final CacheService cacheService;

    private final String bucketName;

    private final UIFactory uiFactory;

    private final GridItemFactory gridItemFactory;

    private final boolean enableAsync;

    @Autowired
    public InwarrantyDefectItemService(final ServitiumCrmConnector servitiumCrmConnector,
           final S3Service s3Service,
           final CacheService cacheService,
           @Value("${aws.s3.captcha-bucket}") final String bucketName,
           final UIFactory uiFactory,
           final GridItemFactory gridItemFactory,
           @Value("${servitium.jobsheets.enable-async}") boolean enableAsync) {
        this.servitiumCrmConnector = servitiumCrmConnector;
        this.s3Service = s3Service;
        this.cacheService = cacheService;
        this.bucketName = bucketName;
        this.uiFactory = uiFactory;
        this.gridItemFactory = gridItemFactory;
        this.enableAsync = enableAsync;
    }

    public String login(final LoginRequest loginRequest) throws InvalidLoginRequestException {
        if (HttpStatus.OK.equals(servitiumCrmConnector.login(loginRequest))) {
            final String loggedInUserName = WelcomeListFactory
                    .getLoggedInUserName(servitiumCrmConnector.getWelcomeList(loginRequest));
            validate(loginRequest);
            loginRequest.setLoggedInUserName(loggedInUserName);
            final String content = getCallIds(loginRequest, loggedInUserName);
            servitiumCrmConnector.logout(new LogoutRequest(loginRequest.getUserId()));
            return content;
        } else {
            return "<font color=red>Something went wrong. please try to <a href='/app/v1/defects'>login</a> again..!</font><br><hr>" + getPreload(loginRequest.getVersion());
        }
    }

    public String loginAndLoadBillNumbers(final LoginRequest loginRequest) throws InvalidLoginRequestException {
        if (HttpStatus.OK.equals(servitiumCrmConnector.login(loginRequest))) {
            final String loggedInUserName = WelcomeListFactory
                    .getLoggedInUserName(servitiumCrmConnector.getWelcomeList(loginRequest));
            validate(loginRequest);
            loginRequest.setLoggedInUserName(loggedInUserName);
            final String content = getOnlyCallIds(loginRequest, loggedInUserName);
            servitiumCrmConnector.logout(new LogoutRequest(loginRequest.getUserId()));
            return content;
        } else {
            return "<font color=red>Something went wrong. please try to <a href='/app/v1/defects'>login</a> again..!</font><br><hr>" + getPreload(loginRequest.getVersion());
        }
    }

    public String getPreload(final Version version) {

        String jSessionId;
        String serverId;

        LOGGER.info("No session values found in cache. retrieve fresh values");
        final CaptchaResponse captchaResponse = servitiumCrmConnector.getHttpHeaders();
        s3Service.upload(bucketName, KEY_NAME, captchaResponse.getImageBytes());
        try {
            final List<String> cookies = captchaResponse.getHttpHeaders().getValuesAsList("set-cookie");

            jSessionId = cookies.get(0).substring(JSESSION_ID_PREFIX.length()).split(DELIMITER_SEMICOLON)[0];
            serverId = cookies.get(1).substring(SERVER_ID_PREFIX.length()).split(DELIMITER_SEMICOLON)[0];

            LOGGER.info("put session values into cache, jSessionId : {}", jSessionId);
            cacheService.put(CacheType.SESSION.getCacheName(), JSESSION_ID, jSessionId);
            LOGGER.info("put session values into cache, serverId : {}", serverId);

            cacheService.put(CacheType.SESSION.getCacheName(), SERVER_ID, serverId);
            } catch (final IndexOutOfBoundsException indexOutOfBoundsException) {
            try {
                jSessionId = cacheService.get(CacheType.SESSION.getCacheName(), JSESSION_ID, String.class).get();
                serverId = cacheService.get(CacheType.SESSION.getCacheName(), SERVER_ID, String.class).get();
            } catch (final Exception exception) {
                throw new RuntimeException(exception.getMessage());
            }

        } catch (final Exception exception) {
            LOGGER.error("An exception occurred while trying to fetch headers.", exception);
            throw new UnknownException("Something went wrong..! Please try again after few minutes..! Reason : " + exception.getMessage());
        }
        final URL url = s3Service.generatePresignedUrl(bucketName, KEY_NAME, Date.from(Instant.now().plusSeconds(300)), HttpMethod.GET);

        return uiFactory.getLoginPageV4(jSessionId, serverId, url);
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

    @EmailServiceEnabled
    public void validateUsername(final LoginRequest loginRequest) throws InvalidLoginRequestException {
        loginRequest.validateUsername();
    }

    @EmailServiceEnabled
    public void validate(final LoginRequest loginRequest) throws InvalidLoginRequestException {
        loginRequest.validate();
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

    private String getOnlyCallIds(final LoginRequest loginRequest, final String loggedInUserName) {
        final Map<String, String> callIds = loadCallIds(loginRequest);
        final Map<String, List<GridItem>> gridItemsMap = buildGridItems(callIds, loggedInUserName);
        final URL verticleImage = generatePresignedUrl(VERTICAL_LINE_IMAGE);
        final URL horizontalImage = generatePresignedUrl(HORIZONTAL_LINE_IMAGE);
        return uiFactory.buildGridPageWithBillNumbers(gridItemsMap, verticleImage, horizontalImage);
    }

    private String getCallIds(final LoginRequest loginRequest, final String loggedInUserName) {
        final Map<String, String> callIds = loadCallIds(loginRequest);
        return getGridItems(callIds, loggedInUserName);
    }

    private Map<String, String> loadCallIds(final LoginRequest loginRequest) {
        final boolean includeOther = loginRequest.includeOthers();
        final String responseBody = getContent(loginRequest);
        final String[] data = responseBody.split("\n");
        final Map<String, String> callIds = new HashMap<>();
        DefectivePartType.getAvailablePartTypes(includeOther)
                .forEach(partType -> callIds.put(partType.getPartType(), ""));
        int localLineCount = -1;
        String tempCallId = "";
        boolean started = false;
        for(String line : data) {
            if (line.contains(LINE_REGEX)) {
                final String billLine = line.split(LINE_REGEX)[0];
                tempCallId = billLine.substring(billLine.length() - 12);
                started = true;
                localLineCount = 0;
            }
            if (localLineCount == 6 && started) {
                final DefectivePartType partType = resolvePartType(line);
                if (!partType.equals(DefectivePartType.OTHER) || includeOther) {
                    final String delimiter = callIds.get(partType.getPartType()).isBlank() ? "" : DELIMITER_COMMA;
                    callIds.put(partType.getPartType(), callIds.get(partType.getPartType()).concat(delimiter + tempCallId));
                }
                tempCallId = "";
                localLineCount = -1;
                started = false;
            }
            localLineCount++;
        }
        return callIds;
    }

    private String getGridItems(final Map<String, String> callIds, final String loggedInUserName) {
        final Map<String, List<GridItem>> gridItemsMap = buildGridItems(callIds, loggedInUserName);
        final URL verticleImage = s3Service
                .generatePresignedUrl(bucketName, "cut-vertical.jpg", Date.from(Instant.now().plusSeconds(300)), HttpMethod.GET);
        final URL horizontalImage = s3Service
                .generatePresignedUrl(bucketName, "cut.jpg", Date.from(Instant.now().plusSeconds(300)), HttpMethod.GET);
        return uiFactory.buildGridPage(gridItemsMap, verticleImage, horizontalImage);
    }

    private Map<String, List<GridItem>> buildGridItems(final Map<String, String> callIds, final String loggedInUserName) {
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

    private DefectivePartType resolvePartType(final String line) {
        final String typeFromLine = line.split("<input")[0]
                .split("wrap >")[1]
                .replaceAll("no", "");
        return Arrays.stream(DefectivePartType.values())
                .filter(type -> type.matches(typeFromLine))
                .findFirst()
                .orElse(DefectivePartType.OTHER);
    }

    private String getContent(final LoginRequest loginRequest) {
        final String jSessionId = loginRequest.getJSessionId();
        final String serverId = loginRequest.getServer();
        return servitiumCrmConnector.readContentFromServitiumCrm(new ContentRequest(jSessionId, serverId));
    }
}
