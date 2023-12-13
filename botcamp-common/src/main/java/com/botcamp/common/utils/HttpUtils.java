package com.botcamp.common.utils;

import com.botcamp.common.response.EmailResponse;
import com.botcamp.common.response.GenericResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpUtils {

    public static final String SUCCESS = "Success";

    public static ResponseEntity<GenericResponse> generateResponse(HttpStatus status, String message, Object responseObj) {
        GenericResponse response = generateGenericResponse(status, message, responseObj);
        return new ResponseEntity<>(response, status);
    }

    private static GenericResponse generateGenericResponse(HttpStatus status, String message, Object responseObj) {
        GenericResponse response = GenericResponse.builder()
                .status(status.value())
                .message(message)
                .dateTime(DateUtils.dateTimeToString(LocalDateTime.now()))
                .data(responseObj)
                .build();

        if (responseObj instanceof Collection<?>) {
            response.setSize(((Collection<?>) responseObj).size());
        } else if (responseObj instanceof Map<?,?>) {
            response.setSize(((Map<?,?>) responseObj).size());
        } else if (responseObj instanceof EmailResponse) {
            response.setSize(((EmailResponse) responseObj).getEmails().size());
        }

        return response;
    }
}
