package com.rabiloo.base.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Value("${email.receiver}")
    private String receiver;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    Environment environment;

    /**
     * sendEmail
     *
     * @param messageTemplate
     */
    public void sendEmail(final EmailTemplate messageTemplate) {
        new Thread(() -> {
            try {
                LOGGER.info("Sending email {} to {} ", messageTemplate.getSubject(), messageTemplate.getReceiver());
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();

                MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage);

                mailMsg.setFrom(new InternetAddress("info@shop-analyze.com", "BASE_API", "utf-8"));
                mailMsg.setTo(messageTemplate.getReceiver());
                mailMsg.setSubject(messageTemplate.getSubject());
                mailMsg.setText(messageTemplate.getContent(), true);

                javaMailSender.send(mimeMessage);
            } catch (MessagingException | UnsupportedEncodingException e) {
                LOGGER.error("Error when sending email : ", e);
            }
        }).start();
    }

    /**
     * sendEmail
     *
     * @param receiver
     * @param subject
     * @param content
     */
    public void sendEmail(String receiver, String subject, String content) {
        EmailTemplate emailMessage = new EmailTemplate();

        emailMessage.setReceiver(receiver);
        emailMessage.setSubject(subject);
        emailMessage.setContent(content);

        sendEmail(emailMessage);
    }

    /**
     * sendEmail
     *
     * @param subject
     * @param content
     */
    public void sendEmail(String subject, String content) {
        EmailTemplate emailMessage = new EmailTemplate();

        emailMessage.setReceiver(receiver);
        emailMessage.setSubject(subject);
        emailMessage.setContent(content);

        sendEmail(emailMessage);
    }
}
