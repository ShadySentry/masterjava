package ru.javaops.masterjava.mail;

import com.google.common.collect.ImmutableSet;
import ru.javaops.masterjava.service.mail.Addressee;
import ru.javaops.masterjava.service.mail.MailWSClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/send")
public class SendServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Set<Addressee> addresses = Arrays.stream(req.getParameter("users").split(",")).map(Addressee::new).collect(Collectors.toSet());
        String subject = req.getParameter("subject");
        String body = req.getParameter("body");
        MailWSClient.sendToGroup(addresses,ImmutableSet.of(),subject,body);
    }
}
