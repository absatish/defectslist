package com.defectlist.inwarranty;

import com.amazonaws.HttpMethod;
import com.defectlist.inwarranty.configuration.CacheType;
import com.defectlist.inwarranty.connector.ServitiumCrmConnector;
import com.defectlist.inwarranty.httprequestheaders.ContentRequest;
import com.defectlist.inwarranty.httprequestheaders.LoginRequest;
import com.defectlist.inwarranty.model.CaptchaResponse;
import com.defectlist.inwarranty.model.DefectivePartType;
import com.defectlist.inwarranty.model.GridItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.swing.text.Document;
import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InwarrantyDefectItemService {

    private static final String JSESSION_ID = "jSessionId";
    private static final String SERVER_ID = "serverId";

    private static final String DELIMITER_SEMICOLON = ";";

    private static final String JSESSION_ID_PREFIX = "JSESSIONID=";
    private static final String SERVER_ID_PREFIX = "SERVERID=";

    private static final String KEY_NAME = "captcha.jpg";

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

    public List<Document> getDocument() {
        return servitiumCrmConnector.getDetailsByComplaintNumbers(Arrays.asList("B120312112"));
    }

    public String getContent() {
        final Optional<String> jSessionIdFromCache = cacheService.get(CacheType.SESSION.getCacheName(), JSESSION_ID, String.class);
        final Optional<String> serverIdFromCache = cacheService.get(CacheType.SESSION.getCacheName(), SERVER_ID, String.class);
        return servitiumCrmConnector.readContentFromServitiumCrm(new ContentRequest(jSessionIdFromCache.get(), serverIdFromCache.get()));
    }

    public String login(final String username, final String password, final String jSessionId,
                        final String server, final String captcha) {
        final LoginRequest loginRequest = new LoginRequest("0", username, password, captcha, jSessionId, server);
        return HttpStatus.OK.equals(servitiumCrmConnector.login(loginRequest))
                ? getCallIds()
                : getPreload();
    }

    public String getPreload() {

        final Optional<String> jSessionIdFromCache = cacheService.get(CacheType.SESSION.getCacheName(), JSESSION_ID, String.class);
        final Optional<String> serverIdFromCache = cacheService.get(CacheType.SESSION.getCacheName(), SERVER_ID, String.class);

        final String jSessionId;
        final String serverId;

        if (jSessionIdFromCache.isEmpty()) {
            final CaptchaResponse captchaResponse = servitiumCrmConnector.getHttpHeaders();
            s3Service.upload(bucketName, KEY_NAME, captchaResponse.getImageBytes());
            final List<String> cookies = captchaResponse.getHttpHeaders().getValuesAsList("set-cookie");
            jSessionId = cookies.get(0).substring(JSESSION_ID_PREFIX.length()).split(DELIMITER_SEMICOLON)[0];
            serverId = cookies.get(1).substring(SERVER_ID_PREFIX.length()).split(DELIMITER_SEMICOLON)[0];
            cacheService.put(CacheType.SESSION.getCacheName(), JSESSION_ID, jSessionId);
            cacheService.put(CacheType.SESSION.getCacheName(), SERVER_ID, serverId);
        } else {
            jSessionId = jSessionIdFromCache.get();
            serverId = serverIdFromCache.get();
        }
        final URL url = s3Service.generatePresignedUrl(bucketName, KEY_NAME, Date.from(Instant.now().plusSeconds(300)), HttpMethod.GET);
        return uiFactory.getLoginPage(jSessionId, serverId, url);
    }

    public GridItem getJobSheet(final String spareName, final String compalintId) {
        final String response = servitiumCrmConnector.getJobSheet(compalintId);
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
                final String delimiter = callIds.get(partType.getPartType()).isBlank() ? "" : ",";
                callIds.put(partType.getPartType(), callIds.get(partType.getPartType()).concat(delimiter + tempCallId));
                tempCallId = "";
                localLineCount = -1;
                started = false;
            }
            localLineCount++;
        }
        return getGridItems(callIds);
    }

    private String getGridItems(final Map<String, String> callIds) {
        final Map<String, List<GridItem>> gridItemsMap = new HashMap<>();
        callIds.forEach((key, value) -> {
            final List<GridItem> gridItems = Arrays.stream(value.split(","))
                    .filter(complaintId -> complaintId.length() == 12)
                    .map(compalintId -> getJobSheet(key, compalintId))
                    .collect(Collectors.toList());
            gridItemsMap.put(key, gridItems);
        });
        final URL verticleImage = s3Service.generatePresignedUrl(bucketName, "cut-vertical.jpg", Date.from(Instant.now().plusSeconds(300)), HttpMethod.GET);
        final URL horizontalImage = s3Service.generatePresignedUrl(bucketName, "cut.jpg", Date.from(Instant.now().plusSeconds(300)), HttpMethod.GET);
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
