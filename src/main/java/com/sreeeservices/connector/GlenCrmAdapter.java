package com.sreeeservices.connector;

import com.sreeeservices.exception.NoDataFoundException;
import com.sreeeservices.httprequestheaders.HttpRequestHeadersService;
import com.sreeeservices.model.GlenLoginResponse;
import com.sreeeservices.model.GlenRawResponse;
import com.sreeeservices.model.GlenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.util.List;

@Component
public class GlenCrmAdapter implements GlenCrmConnector {

    private final RestOperations restOperations;
    private final GlenUrlService glenUrlService;

    private final HttpRequestHeadersService httpRequestHeadersService;

    @Autowired
    public GlenCrmAdapter(final RestOperations restOperations, final GlenUrlService glenUrlService,
                          final HttpRequestHeadersService httpRequestHeadersService) {
        this.restOperations = restOperations;
        this.glenUrlService = glenUrlService;
        this.httpRequestHeadersService = httpRequestHeadersService;
    }

    @Override
    public List<GlenResponse> getGlenResponse() {
        final String token = getToken("tmani", "123456");
        final ResponseEntity<GlenRawResponse> exchange = restOperations.exchange(
                glenUrlService.getUrlPrefix(),
                HttpMethod.GET,
                httpRequestHeadersService.getHttpHeader(token),
                new ParameterizedTypeReference<>() {
                });

        return exchange.getBody().getResponse();
    }

    public String getToken(final String username, final String password) {
        final ResponseEntity<GlenLoginResponse> exchange = restOperations.exchange(
                glenUrlService.getLoginUrl(),
                HttpMethod.POST,
                httpRequestHeadersService.getHttpLoginCreds(username, password),
                new ParameterizedTypeReference<GlenLoginResponse>() {
                });

        return exchange.getBody().getResult().getToken();
    }
}
