package com.defectlist.inwarranty.resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Liviness {

    @GetMapping
    public ResponseEntity isLive() {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.set("Access-Control-Allow-Origin", "http://localhost:3000");
        headers.set("Access-Control-Allow-Methods", "GET, POST");
        headers.set("Access-Control-Allow-Headers", "Content-Type");
        return new ResponseEntity<>(headers,HttpStatus.ACCEPTED);
    }
}
