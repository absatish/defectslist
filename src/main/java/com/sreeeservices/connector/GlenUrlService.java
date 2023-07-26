package com.sreeeservices.connector;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class GlenUrlService {

    private final String urlPrefix;
    private final String loginUrl;
    @Autowired
    public GlenUrlService(@Value("${glen.url-prefix}") final String urlPrefix,
                          @Value("${glen.login-url}") final String loginUrl) {
        this.urlPrefix = urlPrefix;
        this.loginUrl = loginUrl;
    }

}
