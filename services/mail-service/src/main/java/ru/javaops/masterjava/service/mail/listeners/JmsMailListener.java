package ru.javaops.masterjava.service.mail.listeners;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.service.mail.Attachment;
import ru.javaops.masterjava.service.mail.MailServiceExecutor;
import ru.javaops.masterjava.service.mail.MailWSClient;
import ru.javaops.masterjava.service.mail.util.Attachments;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Collections;

@WebListener
@Slf4j
public class JmsMailListener implements ServletContextListener {
    private Thread listenerThread = null;
    private QueueConnection connection;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            InitialContext initCtx = new InitialContext();
            QueueConnectionFactory connectionFactory =
                    (QueueConnectionFactory) initCtx.lookup("java:comp/env/jms/ConnectionFactory");
            connection = connectionFactory.createQueueConnection();
            QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = (Queue) initCtx.lookup("java:comp/env/jms/queue/MailQueue");
            QueueReceiver receiver = queueSession.createReceiver(queue);
            connection.start();
            log.info("Listen JMS messages ...");
            listenerThread = new Thread(() -> {
                try {
                    while (!Thread.interrupted()) {
                        Message m = receiver.receive();
                        // TODO implement mail sending
                        if (m instanceof ObjectMessage) {
                            ObjectMessage tm = (ObjectMessage) m;
                            String body = tm.getStringProperty("body");
                            String subject = tm.getStringProperty("subject");
                            String users = tm.getStringProperty("users");
                            Attachment attachment = null;
                            try {
                                String attachName = tm.getStringProperty("attachName");
                                if(attachName!=null){
                                    byte[] bytes=(byte[]) tm.getObject();
                                    attachment = Attachments.fromByteArray(attachName, (byte[]) tm.getObject());
//                                    ByteArrayInputStream out = new ByteArrayInputStream((byte[]) tm.getObject());
                                    log.info("JmsMailListener attachment "+Arrays.toString(bytes));
                                }
                            } catch (JMSException e) {
                                log.error("error during attachment processing " + e);
                            }

                            MailServiceExecutor.sendBulk(MailWSClient.split(users), subject, body, attachment==null?Collections.emptyList():
                                    ImmutableList.of(attachment));
                            log.info("Received TextMessage with receivers '{}' subject '{}' && text '{}'",
                                    users, subject, body);
                        }
                    }
                } catch (Exception e) {
                    log.error("Receiving messages failed: " + e.getMessage(), e);
                }
            });
            listenerThread.start();
        } catch (Exception e) {
            log.error("JMS failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException ex) {
                log.warn("Couldn't close JMSConnection: ", ex);
            }
        }
        if (listenerThread != null) {
            listenerThread.interrupt();
        }
    }
}