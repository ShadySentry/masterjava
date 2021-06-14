package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import ru.javaops.masterjava.ExceptionType;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.service.mail.persist.MailCase;
import ru.javaops.masterjava.service.mail.persist.MailCaseDao;
import ru.javaops.web.WebStateException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

@Slf4j
public class MailSender {
    private static final MailCaseDao MAIL_CASE_DAO = DBIProvider.getDao(MailCaseDao.class);

    static MailResult sendTo(Addressee to, String subject, String body, byte[] attachment) throws WebStateException {
        String state = null;
        state = sendToGroup(ImmutableSet.of(to), ImmutableSet.of(), subject, body, attachment);

        return new MailResult(to.getEmail(), state);
    }

    static String sendToGroup(Set<Addressee> to, Set<Addressee> cc, String subject, String body, byte[] attachmentData) throws WebStateException {
        log.info("Send mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));
        String state = MailResult.OK;
        File persistedAttachment = null;
        try {
            persistedAttachment = processFile(attachmentData, "test.xml");

            val email = MailConfig.createHtmlEmail();
            email.setSubject(subject);
            email.setHtmlMsg(body);
            for (Addressee addressee : to) {
                email.addTo(addressee.getEmail(), addressee.getName());
            }
            for (Addressee addressee : cc) {
                email.addCc(addressee.getEmail(), addressee.getName());
            }

            //  https://yandex.ru/blog/company/66296
            email.setHeaders(ImmutableMap.of("List-Unsubscribe", "<mailto:masterjava@javaops.ru?subject=Unsubscribe&body=Unsubscribe>"));
            if (persistedAttachment != null) {
                EmailAttachment attachment = MailConfig.attach(persistedAttachment.getPath(), persistedAttachment.getName(),"generated email attachment");
                email.attach(attachment);
            }
            email.send();
        } catch (EmailException e) {
            try {
                Files.deleteIfExists(persistedAttachment.toPath());
            } catch (IOException ioException) {
                log.error("tmp attachment deleting exception", e);
                throw new WebStateException(e, ExceptionType.ATTACH);
            }
            log.error(e.getMessage(), e);
            state = e.getMessage();
        } catch (IOException e) {
            try {
                Files.deleteIfExists(Paths.get(persistedAttachment.getPath()));
            } catch (IOException ioException) {
                log.error("tmp attachment deleting exception", e);
                throw new WebStateException(e, ExceptionType.ATTACH);
            }
            log.error("Attachment processing exception", e);
            throw new WebStateException(e, ExceptionType.ATTACH);
        }
        try {
            MAIL_CASE_DAO.insert(MailCase.of(to, cc, subject, state));
        } catch (Exception e) {
            log.error("Mail history saving exception", e);
            try {
                Files.deleteIfExists(Paths.get(persistedAttachment.getPath()));
            } catch (IOException ioException) {
                log.error("tmp attachment deleting exception", e);
                throw new WebStateException(e, ExceptionType.ATTACH);
            }
            throw new WebStateException(e, ExceptionType.DATA_BASE);
        }
        try {
            Files.deleteIfExists(Paths.get(persistedAttachment.getPath()));
        } catch (IOException e) {
            log.error("tmp attachment deleting exception", e);
            throw new WebStateException(e, ExceptionType.ATTACH);
        }

        log.info("Sent with state: " + state);
        return state;
    }

    private static File processFile(@NonNull byte[] data, String fileName) throws IOException {

        String uploadDir = (new File(".")).getCanonicalPath();
        File savedFile = new File(uploadDir + "/" + fileName);

        FileOutputStream output = new FileOutputStream(savedFile);
        output.write(data);
        output.close();

        log.info("MailSender: File {} uploaded to {} in", fileName, savedFile.getAbsolutePath());

        return savedFile;
    }
}
