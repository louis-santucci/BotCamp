package com.botcamp.botcamp_api.repository.entity.serializer;

import com.botcamp.common.mail.QueryParameter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import java.io.IOException;

@Slf4j
public class QueryParameterConverter implements AttributeConverter<QueryParameter, String> {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String convertToDatabaseColumn(QueryParameter queryParameter) {
        String result = null;
        try {
            result = objectMapper.writeValueAsString(queryParameter);
        } catch (JsonProcessingException e) {
            log.error("Cannot convert queryParameter into JSON");
        }

        return result;
    }

    @Override
    public QueryParameter convertToEntityAttribute(String s) {
        QueryParameter queryParameter = null;
        try {
            queryParameter = objectMapper.readValue(s, QueryParameter.class);
        } catch (final IOException e) {
            log.error("JSON reading error", e);
        }

        return queryParameter;
    }
}
