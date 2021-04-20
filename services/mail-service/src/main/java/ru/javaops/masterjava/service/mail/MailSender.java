package ru.javaops.masterjava.service.mail;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.util.List;

@Slf4j
public class MailSender {
    private static final String HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 465;
    private static final String USER_NAME = "eurohawk1@gmail.com";
    private static final String USER_PASSWORD = "x3cd46gh";
    private static final boolean SSL_FLAG = true;

    public static Email getEmail() throws EmailException {
        Email email = new SimpleEmail();

        email.setHostName(HOST);
        email.setSmtpPort(SMTP_PORT);
        email.setAuthenticator(new DefaultAuthenticator(USER_NAME, USER_PASSWORD));
        email.setSSLOnConnect(SSL_FLAG);
        email.setSslSmtpPort(String.valueOf(SMTP_PORT));
        email.setFrom(USER_NAME);
        email.setSubject("masterjava test email");
        email.setMsg("This is a test mail ... :-)");
        email.addTo("eurohawk1@gmail.com");

        return email;
    }

    public static Email getEmailWithParams(final List<Addressee> to, final List<Addressee> cc, final String subject, final String body) throws EmailException {
        Email email = new SimpleEmail();

        email.setHostName(HOST);
        email.setSmtpPort(SMTP_PORT);
        email.setAuthenticator(new DefaultAuthenticator(USER_NAME, USER_PASSWORD));
        email.setSSLOnConnect(SSL_FLAG);
        email.setFrom(USER_NAME);
        email.setSubject(subject);
        email.setMsg(body);
        email.addTo(to.get(0).getEmail().toString());

        return email;
    }


    @SneakyThrows
    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {
        log.info("Send mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));
        Email email = getEmailWithParams(to, cc, subject, body);

        String result = email.send();
        log.info("Mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + "was send with result " + result);
    }
}
