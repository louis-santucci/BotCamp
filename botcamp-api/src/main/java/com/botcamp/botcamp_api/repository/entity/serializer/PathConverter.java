package com.botcamp.botcamp_api.repository.entity.serializer;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import java.nio.file.Path;

@Slf4j
public class PathConverter implements AttributeConverter<Path, String> {
    @Override
    public String convertToDatabaseColumn(Path path) {
        if (path == null) return null;
        Path absolutePath = path.toAbsolutePath();
        return absolutePath.toString();
    }

    @Override
    public Path convertToEntityAttribute(String s) {
        return (s != null ) ? Path.of(s) : null;
    }
}
