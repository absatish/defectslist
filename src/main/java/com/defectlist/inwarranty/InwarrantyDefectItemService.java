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
import com.defectlist.inwarranty.model.TargetPage;
import com.defectlist.inwarranty.ui.UIFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private static final String CALL_SEARCH_REGEX = "title=\"B\\d+\" value=\"B\\d+";
    private static final String PART_CODE_REGEX = "<nobr>[A-Z\\d].+</nobr>"; //"<nobr>[A-Z][A-Z\\d]+</nobr>";
    private static final String GOOD_PARTS_REGEX = "<td class='altrow' align='left' wrap >[A-Z\\d].+<input type='hidden' size='50' name = 'Description[\\d].*' value = '[A-Z\\d].+'>";
    private static final String ALLOCATED_QTY_REGEX = "<td class='altrow' align='right' nowrap >[\\d]<input type='hidden'  name = 'Allocated_Qty[A-Z\\d].+' value = '1'>";

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
        final LoginRequest successRequest = getLoginRequest(loginRequest);
        final String content = getCallIds(successRequest, successRequest.getLoggedInUserName());
        servitiumCrmConnector.logout(new LogoutRequest(loginRequest.getUserId()));
        return content;
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
            return "<font color=red>Something went wrong. please try to <a href='/app/v1/defects'>login</a> again..!</font><br><hr>"
                    + getPreload(loginRequest.getVersion(), TargetPage.DEFECTIVE);
        }
    }

    public String getPreload(final Version version, final TargetPage targetPage) {

        String jSessionId;
        String serverId;

        LOGGER.info("No session values found in cache. retrieve fresh values");
        final CaptchaResponse captchaResponse = servitiumCrmConnector.getHttpHeaders();
        s3Service.upload(bucketName, KEY_NAME, captchaResponse.getImageBytes(), "image/jpg");
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

        return uiFactory.getLoginPageV4(jSessionId, serverId, url, targetPage);
    }

    @EmailServiceEnabled
    public void validateUsername(final LoginRequest loginRequest) throws InvalidLoginRequestException {
        loginRequest.validateUsername();
    }

    @EmailServiceEnabled
    public void validate(final LoginRequest loginRequest) throws InvalidLoginRequestException {
        loginRequest.validate();
    }

    public String uploadDoc(final LoginRequest loginRequest) {
        final String goodItems = getContent(loginRequest, "REG");
        final String oldGoodCodesString = s3Service.getGoodParts();
        final LocalDateTime lastModified = LocalDateTime.parse(s3Service.getGoodPartsModified());
        if (lastModified.toLocalDate().equals(LocalDate.now())) {
            return "<table  border=1 width=100%><tr><th bgcolor=pink><marquee>Already started from today</marquee></th></tr></table>" + getGoodItems(loginRequest, goodItems);
        }
        s3Service.upload(bucketName, "items.html", goodItems.getBytes(StandardCharsets.UTF_8), "text/plain");
        s3Service.upload(bucketName, "items-prev.html", oldGoodCodesString.getBytes(StandardCharsets.UTF_8), "text/plain");
        return getGoodItems(loginRequest, goodItems);
    }

    public String getGoodItems(final LoginRequest loginRequest) throws InvalidLoginRequestException {
        final LoginRequest successRequest = getLoginRequest(loginRequest);
        final String goodItems = getContent(successRequest, "REG");
        return getGoodItems(successRequest, goodItems);
    }

    public String getHappyCode(final LoginRequest loginRequest, final String callId) throws InvalidLoginRequestException {
        final LoginRequest successRequest = getLoginRequest(loginRequest);
        final String happyCode = servitiumCrmConnector.readHappyCode(ContentRequest.builder()
                        .jSessionId(successRequest.getJSessionId())
                        .server(successRequest.getServer())
                .build(), callId);
        return Stream.of(happyCode.split("\n"))
                .filter(s -> s.contains("<input type=\"hidden\" name=\"completionCd\" id=\"completionCd\""))
                .map(s -> "<center><u><font color=green size=15px> " + callId + ":" + s.replaceAll("<input type=\"hidden\" name=\"completionCd\" id=\"completionCd\"", "")
                        .replaceAll("value=\"", "")
                        .replaceAll("\"", "")
                        .replaceAll(">", "") + "</font></center></u>")
                .findFirst().orElse("Not found");
    }

    private String getGoodItems(final LoginRequest successRequest, final String goodItems) {
        final LocalDateTime lastModified = LocalDateTime.parse(s3Service.getGoodPartsModified());
        final String claimItems = getContent(successRequest, lastModified.toLocalDate(), LocalDate.now());
        final Map<String, Long> partCodes = new HashMap<>();
        Arrays.stream(claimItems.split("\n"))
                .filter(line -> matches(line, CALL_SEARCH_REGEX))
                .map(this::getComplaintId)
                .map(servitiumCrmConnector::getJobSheet)
                .map(this::getPartCodes)
                .flatMap(Collection::stream)
                .forEach(partCode -> {
                    var count = partCodes.computeIfAbsent(partCode, v -> 0L);
                    partCodes.put(partCode, count + 1);
                });

        final Map<String, Long> goodCodes = new LinkedHashMap<>();
        addToGoodCodes(goodItems, goodCodes);

        final Map<String, Long> oldGoodCodes = new HashMap<>();
        final String oldGoodCodesString = s3Service.getGoodParts();
        addToGoodCodes(oldGoodCodesString, oldGoodCodes);

        return "<html>" +
                "   <head>" +
                "       <title> Good Parts Comparison</title>" +
                "       <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">\n" +
                "       \n" +
                "       <!-- jQuery library -->\n" +
                "       <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js\"></script>\n" +
                "       \n" +
                "       <!-- Latest compiled JavaScript -->\n" +
                "       <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js\"></script>" +
                "   </head>" +
                "   <body>" +
                "       <table style='margin-left: 10px' border=1>" +
                "           <tr><th bgcolor=pink colspan=2>Comparision between " + lastModified + " & " + LocalDateTime.now() + "</th><th bgcolor=pink  colspan=2><a href='/app/v2/defects/start?id=" + successRequest.getJSessionId() + "&server=" + successRequest.getServer() + "'>Start from Now</a></th></tr>" +
                "           <tr><th>Part Name</th><th>New Count</th><th>Old Count</th><th>Claimed</th></tr>\n" + goodCodes.entrySet().stream().map(entry -> {

                    long oldGoodCode = oldGoodCodes.getOrDefault(entry.getKey(), 0L);
                    long partCode = partCodes.getOrDefault(entry.getKey(), 0L);
                    boolean isDescripency = entry.getValue() != oldGoodCode - partCode;
                    String color = isDescripency ? "pink" : "white";
                    return "<tr bgcolor='" + color + "'><td>" + entry.getKey()
                            + "</td><td>" + entry.getValue() + "</td><td>" + oldGoodCode
                            + "</td><td>" + partCode;
                })
                .collect(Collectors.joining("</td></tr>\n"))
                + "</td></tr>\n</table>" +
                "</body>" +
                "</html>";
    }

    public LoginRequest getLoginRequest(final LoginRequest loginRequest) throws InvalidLoginRequestException {
        try {
            if (HttpStatus.OK.equals(servitiumCrmConnector.login(loginRequest))) {
                final String loggedInUserName = WelcomeListFactory
                        .getLoggedInUserName(servitiumCrmConnector.getWelcomeList(loginRequest));
                validate(loginRequest);
                loginRequest.setLoggedInUserName(loggedInUserName);
                return loginRequest;
            }
        } catch (Exception exception) {
            throw new InvalidLoginRequestException(exception.getMessage());
        }
        throw new InvalidLoginRequestException("Login failed. Unknown reason");
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
        final String responseBody = getContent(loginRequest, "DEF");
        final String[] data = responseBody.split("\n");
        final Map<String, String> callIds = new HashMap<>();
        DefectivePartType.getAvailablePartTypes(includeOther)
                .forEach(partType -> callIds.put(partType.getPartType(), ""));
        int localLineCount = -1;
        String tempCallId = "";
        boolean started = false;
        for (String line : data) {
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

    private String getContent(final LoginRequest loginRequest, final String type) {
        final String jSessionId = loginRequest.getJSessionId();
        final String serverId = loginRequest.getServer();
        return servitiumCrmConnector.readContentFromServitiumCrm(ContentRequest.builder()
                .jSessionId(jSessionId)
                .server(serverId)
                .callSearch(false)
                .build(), type);
    }

    private String getContent(final LoginRequest loginRequest, final LocalDate fromDate, final LocalDate toDate) {
        final String jSessionId = loginRequest.getJSessionId();
        final String serverId = loginRequest.getServer();
        return servitiumCrmConnector.readContentFromServitiumCrm(ContentRequest.builder()
                .jSessionId(jSessionId)
                .server(serverId)
                .callSearch(true)
                .fromDate(fromDate)
                .toDate(toDate)
                .build(), "REG");
    }

    private List<String> getPartCodes(final String jobSheet) {
        return Arrays.stream(jobSheet.split("\n"))
                .filter(line -> matches(line, PART_CODE_REGEX) && !line.contains("Vikram"))
                .map(line -> line.replace("<nobr>", "").replaceAll("</nobr>", "").trim())
                .collect(Collectors.toList());
    }

    private String getComplaintId(final String line) {
        return line.split("title=\"")[1].split("\" value=\"")[0];
    }

    private boolean matches(final String line, final String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    private void addToGoodCodes(final String goodItems, Map<String, Long> goodCodes) {
        var array = goodItems.split("\n");
        for (int i = 0; i < array.length; i++) {
            if (matches(array[i], GOOD_PARTS_REGEX)) {
                var partName = array[i].split("<td class='altrow' align='left' wrap >")[1].split("<input type")[0];
                var partCount = array[i + 3].split("<td class='altrow' align='right' nowrap >")[1].split("<input")[0];
                i = i + 3;
                goodCodes.put(partName, Long.valueOf(partCount));
            }
        }
    }
}
