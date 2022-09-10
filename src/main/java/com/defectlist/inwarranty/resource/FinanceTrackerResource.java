package com.defectlist.inwarranty.resource;

import com.defectlist.inwarranty.FinanceTrackerService;
import com.defectlist.inwarranty.LoginDetailsValidator;
import com.defectlist.inwarranty.ui.TrackerUIPage;
import com.defectlist.inwarranty.utils.MonthUtils;
import com.defectlist.inwarranty.utils.RequestParameterResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/app/v1/tracker")
public class FinanceTrackerResource {

    private static final String USERNAME = "username";
    private static final String PIN = "pin";
    private static final String MONTH = "month";

    private final LoginDetailsValidator loginDetailsValidator;
    private final FinanceTrackerService financeTrackerService;

    @Autowired
    public FinanceTrackerResource(final LoginDetailsValidator loginDetailsValidator,
                                  final FinanceTrackerService financeTrackerService) {
        this.loginDetailsValidator = loginDetailsValidator;
        this.financeTrackerService = financeTrackerService;
    }

    @GetMapping
    public String getLoginPage() {
        return TrackerUIPage.getFirstPage();
    }

    @GetMapping("/login")
    public String getLoginPage2() {
        return getLoginPage();
    }

    @PostMapping("/login")
    public String validateLoginAndLoadFinancialTracker(@RequestParam final Map<String, String> requestParams) {
        final String userName = RequestParameterResolver.getValue(requestParams, USERNAME);
        final String pin = RequestParameterResolver.getValue(requestParams, PIN);
        if (loginDetailsValidator.validate(userName, Long.valueOf(pin))) {
            final String month = MonthUtils.currentMonth();
            return financeTrackerService.getFinanceTrackerDetails(month);
        } else {
            return getLoginPage();
        }
    }

    @PostMapping("/load")
    public String loadMonthData(@RequestParam final Map<String, String> requestParams) {
        final String month = RequestParameterResolver.getValue(requestParams, MONTH);
        return financeTrackerService.getFinanceTrackerDetails(month);
    }

    @PostMapping("/create")
    public String createRecord(@RequestParam final Map<String, String> requestParams) {
        return financeTrackerService.create(requestParams);
    }

}
