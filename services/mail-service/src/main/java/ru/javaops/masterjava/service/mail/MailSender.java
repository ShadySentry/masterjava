package ru.javaops.masterjava.service.mail;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.service.mail.persist.MailCase;
import ru.javaops.masterjava.service.mail.persist.MailCaseDao;
import ru.javaops.masterjava.service.mail.persist.MailConfig;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
public class MailSender {

    static MailCaseDao mailCaseDao = DBIProvider.getDao(MailCaseDao.class);

    public static Email getEmailWithParams(final List<Addressee> to, final List<Addressee> cc, final String subject, final String body) throws EmailException {
        Email email = MailConfig.createSimpleEmail();
        email.setSubject(subject);
        email.setMsg(body);
        email.addTo(to.get(0).getEmail());
        return email;
    }


    @SneakyThrows
    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {
        log.info("Send mail to '" + to + "' cc '" + cc + "' subject '" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));
        Email email = getEmailWithParams(to, cc, subject, body);

        String result = email.send();

        MailCase mailCase = new MailCase();
        mailCase.setReceivers(to.toString());
        mailCase.setCc(email.getCcAddresses().toString());
        mailCase.setSubject(email.getSubject());
        mailCase.setResult(result);
//        mailCase.setState(email.);
        mailCase.setDateTime(new Timestamp(System.currentTimeMillis()));
        mailCaseDao.insert(mailCase);
        log.info("Mail to '" + to + "' cc '" + cc + "' subject '" + subject + "was send with result " + result);
    }
}
