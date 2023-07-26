package com.defectlist.inwarranty.resource;

import com.defectlist.inwarranty.ui.UIFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Deprecated
@RestController
@RequestMapping("/app/v1/defects")
public class InwarrantyDefectItemResource {

    private static final String DEPRICATION_TEXT = UIFactory.redirectToLatestVersion();

    @GetMapping
    public String initialPage() {
       return DEPRICATION_TEXT;
    }

    @GetMapping("/login")
    public String login() {
        return "<center><font color=red>Invalid Session. Please first login..!</font></center><br>" + initialPage();
    }

}
