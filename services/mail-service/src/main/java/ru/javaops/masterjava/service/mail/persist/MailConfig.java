package ru.javaops.masterjava.service.mail.persist;

import com.typesafe.config.Config;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import ru.javaops.masterjava.config.Configs;

public class MailConfig {
    private static final MailConfig INSTANCE =
            new MailConfig(Configs.getConfig("mail.conf").getConfig("mail"));

    private final String host;
    private final int smtpPort;
    private final String userName;
    private final String userPassword;
    private final DefaultAuthenticator auth;
    private final boolean sslFlag;
    private final String fromName;

    private MailConfig(Config config) {
        host = config.getString("host");
        smtpPort = config.getInt("port");
        userName = config.getString("username");
        userPassword = config.getString("password");
        auth = new DefaultAuthenticator(userName, userPassword);
        sslFlag = config.getBoolean("useSSL");
        fromName = config.getString("fromName");
    }

    public <T extends Email> T prepareEmail(T email) throws EmailException {
        email.setHostName(host);
        email.setSmtpPort(smtpPort);
        email.setAuthenticator(auth);
        email.setSSLOnConnect(sslFlag);
        email.setFrom(userName, fromName);
        email.setSubject(fromName);

        return email;
    }

    public static SimpleEmail createSimpleEmail() throws EmailException {
        return INSTANCE.prepareEmail(new SimpleEmail());
    }

}
