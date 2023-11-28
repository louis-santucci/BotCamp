package utils;

import com.botcamp.common.exception.EmailParsingException;
import com.botcamp.common.utils.EmailUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.swing.plaf.synth.SynthViewportUI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmailUtilsTests {

    private static final Session session = Session.getDefaultInstance(new Properties());
    private static final String FROM_EMAIL = "toto@toto.fr";
    private static final String FROM_EMAIL_WRONG = "toto.fr";
    private static final String TO_EMAIL_WRONG = "tothtotnjk";
    private static final String TO_EMAIL = "toto@toto.fr";
    private static final List<String> TO_EMAIL_LIST = Arrays.asList(
            "toto@toto.fr",
            "tutu@tutu.fr"
    );

    private static final String SUBJECT_TEST = "Test";
    private static final String BODY_TEST = "Test Body";

    @Test
    public void test_throws_error_with_wrong_email_address() {
        assertThrows(EmailParsingException.class, () -> EmailUtils.buildEmail(session, FROM_EMAIL, Collections.singletonList(TO_EMAIL_WRONG), SUBJECT_TEST, BODY_TEST));
    }

    @Test
    public void test_throws_error_with_wrong_email_address_sender() {
        assertThrows(EmailParsingException.class, () -> EmailUtils.buildEmail(session, FROM_EMAIL_WRONG, Collections.singletonList(TO_EMAIL), SUBJECT_TEST, BODY_TEST));
    }

    @Test
    public void test_build_email_one_email_address() throws MessagingException, EmailParsingException {
        MimeMessage msg = EmailUtils.buildEmail(session, FROM_EMAIL, Collections.singletonList(TO_EMAIL), SUBJECT_TEST, BODY_TEST);
    }

    @Test
    public void test_build_email_several_email_addresses() throws MessagingException, EmailParsingException {
        MimeMessage msg = EmailUtils.buildEmail(session, FROM_EMAIL, TO_EMAIL_LIST, SUBJECT_TEST, BODY_TEST);
    }

    @Test
    public void test_validate_email_true() {
        String email = "test@test.com";
        assertDoesNotThrow(() -> EmailUtils.validateEmail(email));
    }

    @Test
    public void test_validate_email_wrong() {
        String email = "iojefioej.cej";
        assertThrows(EmailParsingException.class, () -> EmailUtils.validateEmail(email));
    }
}
