package ru.javaops.masterjava.service.mail;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.io.Resources;
import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import ru.javaops.masterjava.config.Configs;
import ru.javaops.masterjava.web.AuthUtil;
import ru.javaops.masterjava.web.WebStateException;
import ru.javaops.masterjava.web.WsClient;
import ru.javaops.masterjava.web.handler.SoapLoggingHandlers;

import javax.xml.namespace.QName;
import javax.xml.ws.soap.MTOMFeature;
import java.util.List;
import java.util.Set;

@Slf4j
public class MailWSClient {
    private static Config HOSTS;
    private static final WsClient<MailService> WS_CLIENT;
    public static String USER;
    public static String PASSWORD;
    private static SoapLoggingHandlers.ClientHandler LOGGING_HANDLER;

    public static String AUTH_HEADER;

    static {
        HOSTS = Configs.getConfig("hosts.conf","hosts");
        USER=HOSTS.getString("mail.user");
        PASSWORD=HOSTS.getString("mail.password");
        LOGGING_HANDLER=new SoapLoggingHandlers.ClientHandler(Level.valueOf(HOSTS.getString("mail.debug.client")));
        AUTH_HEADER=AuthUtil.encodeBasicAuthHeader(USER, PASSWORD);
        WS_CLIENT = new WsClient<>(Resources.getResource("wsdl/mailService.wsdl"),
                new QName("http://mail.javaops.ru/", "MailServiceImplService"),
                MailService.class);

        WS_CLIENT.init("mail.endpoint", "/mail/mailService?wsdl");
    }


    public static String sendToGroup(final Set<Addressee> to, final Set<Addressee> cc, final String subject, final String body, List<Attachment> attachments) throws WebStateException {
        log.info("Send to group to '" + to + "' cc '" + cc + "' subject '" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));
        String status = getPort().sendToGroup(to, cc, subject, body, attachments);
        log.info("Send to group with status: " + status);
        return status;
    }

    public static GroupResult sendBulk(final Set<Addressee> to, final String subject, final String body, List<Attachment> attachments) throws WebStateException {
        log.info("Send bulk to '" + to + "' subject '" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));
        GroupResult result = getPort().sendBulk(to, subject, body, attachments);
        log.info("Sent bulk with result: " + result);
        return result;
    }

    private static MailService getPort() {
        MailService port = WS_CLIENT.getPort(new MTOMFeature(1024));
        WsClient.setAuth(port, USER, PASSWORD);
        WsClient.setHandler(port, LOGGING_HANDLER);
        return port;
    }

    public static Set<Addressee> split(String addressees) {
        Iterable<String> split = Splitter.on(',').trimResults().omitEmptyStrings().split(addressees);
        return ImmutableSet.copyOf(Iterables.transform(split, Addressee::new));
    }
}
