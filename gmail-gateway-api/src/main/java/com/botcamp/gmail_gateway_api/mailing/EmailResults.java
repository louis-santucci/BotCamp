package com.botcamp.gmail_gateway_api.mailing;

import com.botcamp.common.mail.Email;
import com.botcamp.common.mail.EmailError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailResults {
    private List<Email> emails;
    private List<EmailError> errors;
}
