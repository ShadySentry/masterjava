package ru.javaops.masterjava.service.mail;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.*;

import java.util.List;

@Slf4j
public class MailSender {
    Email email;

    public Email setup() throws EmailException {
        if(email==null){
            email = new SimpleEmail();
        }
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("eurohawk1@gmail.com","qqdvnwupiuivnffu"));
        email.setSSLOnConnect(true);
        email.setFrom("eurohawk@gmail.com");
        email.setSubject("masterjava test email");
        email.setMsg("This is a test mail ... :-)");
        email.addTo("eurohawk1@gmail.com");
//        email.setSslSmtpPort("465");

        return email;
    }


    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {
        log.info("Send mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));

    }
}
