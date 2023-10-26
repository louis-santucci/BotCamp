package com.botcamp.botcamp.service.reporting.bandcamp;

import com.botcamp.botcamp.service.reporting.Record;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
public class BandcampRecord implements Record {
    private LocalDate releaseDate;
    private String page;
    private String artist;
    private String name;
    private String imageUrl;
    private String description;


    @Override
    public Set<String> getFields() {
        return Arrays.stream(new String[]{
                Fields.RELEASE_DATE,
                Fields.PAGE,
                Fields.ARTIST,
                Fields.NAME,
                Fields.IMAGE_URL,
                Fields.DESCRIPTION
        }).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public String toReportLine() {
        return null;
    }

    public interface Fields {
        String RELEASE_DATE = "releaseDate";
        String PAGE = "page";
        String ARTIST = "artist";
        String NAME = "name";
        String IMAGE_URL = "imageUrl";
        String DESCRIPTION = "description";
    }
}
