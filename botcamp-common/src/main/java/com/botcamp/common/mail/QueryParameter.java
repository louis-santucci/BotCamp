package com.botcamp.common.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryParameter {
    private String from;
    private String beginDate;
    private String endDate;
    private String subject;
}
