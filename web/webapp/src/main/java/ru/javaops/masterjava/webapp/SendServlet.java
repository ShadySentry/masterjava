package ru.javaops.masterjava.webapp;

import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.service.mail.GroupResult;
import ru.javaops.masterjava.service.mail.MailWSClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/send")
@MultipartConfig
@Slf4j
public class SendServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result;
        try {
            log.info("Start sending");
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            String users = req.getParameter("users");
            String subject = req.getParameter("subject");
            String body = req.getParameter("body");
            Part part = req.getPart("attachment");

            File attachment = null;
            if (part != null) {
                log.info("Processing file with attachment: {}", part.getSubmittedFileName());
                attachment = upload(part);
                part.delete();
            }
            GroupResult groupResult = MailWSClient.sendBulk(MailWSClient.split(users), subject, body, attachment);
            result = groupResult.toString();
            log.info("Processing finished with result: {}", result);
        } catch (Exception e) {
            log.error("Processing failed", e);
            result = e.toString();
        }
        resp.getWriter().write(result);
    }

    private static File upload(Part part) throws IOException {
        if (part == null) {
            return null;
        }

        String uploadDir = (new File(".")).getCanonicalPath();
        InputStream input = part.getInputStream();
        File savedFile = new File(uploadDir + "/" + part.getSubmittedFileName());
        FileOutputStream output = new FileOutputStream(savedFile);
        byte[] data = new byte[input.available()];
        input.read(data);
        output.write(data);
        input.close();
        output.close();

        log.info("File {} uploaded to {}", part.getSubmittedFileName(), savedFile.getAbsolutePath());

        return savedFile;
    }
}
