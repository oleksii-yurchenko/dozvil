package org.yura.services;

import org.yura.config.AppConfig;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailService {
    private final AppConfig config;
    private final Properties mailProps;

    public MailService(AppConfig config) {
        this.config = config;
        this.mailProps = new Properties();
        mailProps.put("mail.smtp.auth", config.getProperty("mail.smtp.auth"));
        mailProps.put("mail.smtp.starttls.enable", config.getProperty("mail.smtp.starttls.enable"));
        mailProps.put("mail.smtp.host", config.getProperty("mail.smtp.host"));
        mailProps.put("mail.smtp.port", config.getProperty("mail.smtp.port"));
    }

    public void sendEmail(String to, String subject, String text) {
      Session session = Session.getInstance(mailProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getProperty("mail.smtp.username"), config.getProperty("mail.smtp.password"));
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(config.getProperty("mail.smtp.username")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
