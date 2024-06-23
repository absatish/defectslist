package com.defectlist.inwarranty.resource;

import com.defectlist.inwarranty.ui.BatteryHome;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BatteryDischargeOptimizationResource.PATH)
public class BatteryDischargeOptimizationResource {

    public static final String PATH = "/app/v1/battery/discharge";

    @GetMapping
    public String getHomePage() {
        return BatteryHome.getHomePage();
    }
//
//    @PostMapping("/cycles")
//    public String

}
