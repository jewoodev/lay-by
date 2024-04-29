package com.orderservice.web.handler;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400:
                break;
            case 404:
                if (methodKey.contains("purchaseWishList")) {
                    return new ResponseStatusException(HttpStatusCode.valueOf(response.status()),
                            "Your wish list is empty.");
                }
            default:
                return new Exception(response.reason());
        }
        return null;
    }
}
