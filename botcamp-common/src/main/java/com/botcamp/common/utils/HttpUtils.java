package com.botcamp.common.utils;

import com.botcamp.common.response.GenericResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpUtils {

    public static final String SUCCESS = "Success";


    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static ResponseEntity<GenericResponse> generateResponse(HttpStatus status, String message, Object responseObj) {
        GenericResponse response = generateGenericResponse(status, message, responseObj);
        return new ResponseEntity<>(response, status);
    }

    private static GenericResponse generateGenericResponse(HttpStatus status, String message, Object responseObj) {
        GenericResponse response = GenericResponse.builder()
                .status(status.value())
                .message(message)
                .dateTime(LocalDateTime.now().format(formatter))
                .data(responseObj)
                .build();

        if (responseObj instanceof Collection<?>) {
            response.setSize(((Collection<?>) responseObj).size());
        }

        return response;
    }
}
