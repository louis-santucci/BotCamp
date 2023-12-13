package com.botcamp.common.response;

import com.botcamp.common.mail.Email;
import com.botcamp.common.mail.EmailError;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailResponse {
    private List<Email> emails;
    private List<EmailError> errors;
}

