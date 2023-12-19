package com.botcamp.common.utils;

import com.botcamp.common.http.HttpMethod;
import com.botcamp.common.response.EmailResponse;
import com.botcamp.common.response.GenericResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpUtils {

    private static final String APPLICATION_JSON = "application/json";
    public static final String SUCCESS = "Success";

    public static <T> ResponseEntity<GenericResponse<T>> generateResponse(HttpStatus status, String message, Object responseObj) {
        GenericResponse response = generateGenericResponse(status, message, responseObj);
        return new ResponseEntity<>(response, status);
    }

    private static <T> GenericResponse<T> generateGenericResponse(HttpStatus status, String message, T responseObj) {
        GenericResponse<T> response = GenericResponse.<T>builder()
                .status(status.value())
                .message(message)
                .dateTime(DateUtils.dateTimeToString(LocalDateTime.now()))
                .data(responseObj)
                .build();

        if (responseObj instanceof Collection<?>) {
            response.setSize(((Collection<?>) responseObj).size());
        } else if (responseObj instanceof Map<?, ?>) {
            response.setSize(((Map<?, ?>) responseObj).size());
        } else if (responseObj instanceof EmailResponse) {
            response.setSize(((EmailResponse) responseObj).getEmails().size());
        }

        return response;
    }


    public static String buildUrl(String protocol, String url, int port, String endpoint) {
        StringBuilder sb = new StringBuilder();

        sb.append(protocol);
        sb.append("://");
        sb.append(url);
        sb.append(':');
        sb.append(port);
        if (!endpoint.startsWith("/")) {
            sb.append('/');
        }
        sb.append(endpoint);

        return sb.toString();
    }

    public static HttpUriRequestBase buildHttpObject(HttpMethod httpMethod, String url, StringEntity body) {
        HttpUriRequestBase httpUriRequestBase = switch (httpMethod) {
            case DELETE -> new HttpDelete(url);
            case POST -> new HttpPost(url);
            case GET -> new HttpGet(url);
        };
        httpUriRequestBase.setEntity(body);
        return httpUriRequestBase;
    }

}
