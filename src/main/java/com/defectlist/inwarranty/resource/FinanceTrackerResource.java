package com.defectlist.inwarranty.resource;

import com.defectlist.inwarranty.utils.LoginDetailsValidator;
import com.defectlist.inwarranty.ui.TrackerUIPage;
import com.defectlist.inwarranty.utils.RequestParameterResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/app/v1/tracker")
public class FinanceTrackerResource {

    private static final String USERNAME = "username";
    private static final String PIN = "pin";

    private final LoginDetailsValidator loginDetailsValidator;

    @Autowired
    public FinanceTrackerResource(final LoginDetailsValidator loginDetailsValidator) {
        this.loginDetailsValidator = loginDetailsValidator;
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
            String linkForEdit = "https://1drv.ms/x/s!AkwTrWiwMYx1zRtCikD7wcb5g2PO?e=qAPUgT";
            String link = "https://onedrive.live.com/embed?cid=758C31B068AD134C&resid=758C31B068AD134C%219883&authkey=AL3I0I2TctMmsMA&em=2";
            return "<a href='" + linkForEdit + "'>Edit Xls Sheet</a><iframe src=\"" + link + "\" width=\"100%\" height=\"100%\" frameborder=\"0\" scrolling=\"no\"></iframe>";
        } else {
            return getLoginPage();
        }
    }
}
