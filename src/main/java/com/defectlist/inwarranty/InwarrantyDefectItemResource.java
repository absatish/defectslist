package com.defectlist.inwarranty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;


@RestController
@RequestMapping("/app/v1/defects")
public class InwarrantyDefectItemResource {

    private final InwarrantyDefectItemService inwarrantyDefectItemService;

    @Autowired
    public InwarrantyDefectItemResource(final InwarrantyDefectItemService inwarrantyDefectItemService) {
        this.inwarrantyDefectItemService = inwarrantyDefectItemService;
    }

    @GetMapping()
    public String initialPage() {
        return inwarrantyDefectItemService.getPreload();
    }

    @GetMapping(path = "/login")
    public String login(
            @RequestParam(value = "username") final String username,
            @RequestParam(value = "password") final String password,
            @RequestParam("captcha") final String captcha,
            @RequestParam("id") final String jSessionId,
            @RequestParam(value = "server", required = false) final String server) {
        return inwarrantyDefectItemService.login(username, password, jSessionId, server, captcha);
    }

    @GetMapping("/items")
    public String getItems() {
        return inwarrantyDefectItemService.getDocuments();
    }

}
