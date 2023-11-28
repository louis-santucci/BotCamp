package com.botcamp.common.utils;

import com.botcamp.common.exception.EmailParsingException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.validator.EmailValidator;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailUtils {

    private static final String UTF_8 = "UTF-8";
    private static final EmailValidator emailValidator = EmailValidator.getInstance();


    public static MimeMessage buildEmail(Session session, String fromEmail, List<String> toEmailList, String subject, String body) throws MessagingException, EmailParsingException {
        List<Address> addressList = new ArrayList<>();

        validateEmail(fromEmail);

        for (String email: toEmailList) {
            validateEmail(email);
            addressList.add(new InternetAddress(email, false));
        }

        MimeMessage msg = new MimeMessage(session);
        msg.addHeader("Content-type", "text/HTML; charset=" + UTF_8);
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");
        msg.setFrom(new InternetAddress(fromEmail));
        msg.setSubject(subject, UTF_8);
        msg.setText(body, UTF_8);
        msg.setSentDate(new Date());
        msg.setRecipients(Message.RecipientType.TO, addressList.toArray(Address[]::new));

        return msg;
    }

    public static void validateEmail(String email) throws EmailParsingException {
        if (!emailValidator.isValid(email)) {
            String errorMsg = email + " is not a valid email";
            throw new EmailParsingException(errorMsg);
        }
    }
}
