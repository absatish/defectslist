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
import com.defectlist.inwarranty.ui.UIFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    private static final String LINE_REGEX = "<input type='hidden' size='50' name = 'Call_Id";

    private final ServitiumCrmConnector servitiumCrmConnector;

    private final S3Service s3Service;

    private final CacheService cacheService;

    private final String bucketName;

    private final UIFactory uiFactory;

    private final GridItemBuilderService gridItemBuilderService;

    private final boolean enablePagination;

    private final PaginatedInwarrantyDefectItemService paginatedInwarrantyDefectItemService;

    @Autowired
    public InwarrantyDefectItemService(final ServitiumCrmConnector servitiumCrmConnector,
                                       final S3Service s3Service,
                                       final CacheService cacheService,
                                       @Value("${aws.s3.captcha-bucket}") final String bucketName,
                                       final UIFactory uiFactory,
                                       final GridItemBuilderService gridItemBuilderService,
                                       @Value("${pagination.active}") final boolean enablePagination,
                                       final PaginatedInwarrantyDefectItemService paginatedInwarrantyDefectItemService) {
        this.servitiumCrmConnector = servitiumCrmConnector;
        this.s3Service = s3Service;
        this.cacheService = cacheService;
        this.bucketName = bucketName;
        this.uiFactory = uiFactory;
        this.gridItemBuilderService = gridItemBuilderService;
        this.enablePagination = enablePagination;
        this.paginatedInwarrantyDefectItemService = paginatedInwarrantyDefectItemService;
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

    @EmailServiceEnabled
    public void validateUsername(final LoginRequest loginRequest) throws InvalidLoginRequestException {
        loginRequest.validateUsername();
    }

    @EmailServiceEnabled
    public void validate(final LoginRequest loginRequest) throws InvalidLoginRequestException {
        loginRequest.validate();
    }

    private String getOnlyCallIds(final LoginRequest loginRequest, final String loggedInUserName) {
        final Map<String, String> callIds = loadCallIds(loginRequest);
        final Map<String, List<GridItem>> gridItemsMap = gridItemBuilderService.buildGridItems(callIds, loggedInUserName);
        final URL verticleImage = gridItemBuilderService.generatePresignedUrl(VERTICAL_LINE_IMAGE);
        final URL horizontalImage = gridItemBuilderService.generatePresignedUrl(HORIZONTAL_LINE_IMAGE);
        return uiFactory.buildGridPageWithBillNumbers(gridItemsMap, verticleImage, horizontalImage);
    }

    private String getCallIds(final LoginRequest loginRequest, final String loggedInUserName) {
        final Map<String, String> callIds = loadCallIds(loginRequest);
        if (loginRequest.paginated()) {
            return paginatedInwarrantyDefectItemService.getPaginatedGridItems(callIds, loggedInUserName, 0);
        }
        return gridItemBuilderService.getGridItems(callIds, loggedInUserName);
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
