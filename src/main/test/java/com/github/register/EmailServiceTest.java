package com.github.register;

import com.github.register.application.EmailService;
import com.github.register.domain.mail.Email;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;


/**
 * @author sniper
 * @date 18 Sep 2023
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RegisterApplication.class })
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void simpleEmailMessageTest() {
        Email email = prepareMessage(false);
        Boolean isSent = emailService.sendSimpleMessage(email);

        assertThat(isSent).isEqualTo(true);
    }

    @Test
    public void simpleEmailMessageWithCCList() {
        Email email = prepareMessageWithCCList();
        Boolean isSent = emailService.sendMessageWithCC(email);

        assertThat(isSent).isEqualTo(true);
    }

    @Test
    public void emailMessageWithAttachment() throws IOException, MessagingException {
        Email email = prepareMessage(true);
        Boolean isSent = emailService.sendMessageWithAttachment(email);

        assertThat(isSent).isEqualTo(true);
    }

    /**
     * Method for preparing E-mail data object with/without attachment.
     *
     * @param attachmentIncluded - flag that indicates is attachment needed or not.
     * @return Email data object.
     */
    private Email prepareMessage(Boolean attachmentIncluded) {
        List<String> toList = new ArrayList<>(1);
        //toList.add("elonmusk@tesla.com");
        toList.add("evilsniper@sina.cn");

        Email email = new Email();
        email.setSubject("Simple e-mail test");
        email.setRecipients(toList);
        email.setHtml(false);
        email.setBody("This is simple e-mail message.");

        if (attachmentIncluded) {
            email.setAttachmentPath("/attachment/ad-arch.png");
        }
        return email;
    }

    /**
     * Method for preparing E-mail object with CC list.
     *
     * @return Email data object
     */
    private Email prepareMessageWithCCList()
    {
        List<String> toList = new ArrayList<>(1);
        toList.add("elonmusk@tesla.com");

        List<String> ccList = new ArrayList<>(1);
        ccList.add("jeffdean@google.com");

        Email email = new Email();
        email.setSubject("Simple e-mail test");
        email.setRecipients(toList);
        email.setCcList(ccList);
        email.setHtml(false);
        email.setBody("This is simple e-mail message with CC list.");

        return email;
    }

}
