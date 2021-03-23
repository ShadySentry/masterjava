package ru.javaops.masterjava.export;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.model.User;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.WebConnection;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/", loadOnStartup = 1)
@MultipartConfig(fileSizeThreshold = 1024*1024*10)
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static Logger LOG = LoggerFactory.getLogger(UploadServlet.class);

    private final UserExport userExport = new UserExport();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale());
        final TemplateEngine templateEngine = ThymeleafUtil.getTemplateEngine(getServletContext());
        templateEngine.process("upload",webContext,response.getWriter());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale());
        final TemplateEngine templateEngine = ThymeleafUtil.getTemplateEngine(getServletContext());
        final ServletFileUpload upload = new ServletFileUpload();
        try {
            final FileItemIterator itemIterator =upload.getItemIterator(request);
            while (itemIterator.hasNext()){
                FileItemStream fileItemStream = itemIterator.next();
                if(!fileItemStream.isFormField()){
                    try(InputStream is = fileItemStream.openStream()){
                        List<User> users = userExport.process(is);
                        users.forEach(u->LOG.info(u.toString()));
                        webContext.setVariable("users",users);
                        templateEngine.process("result",webContext,response.getWriter());
                    }
                    break;
                }
            }
            LOG.info("XMl successfully uploaded");
        }catch (Exception e){
            LOG.info(e.getMessage());
            templateEngine.process("exception",webContext,response.getWriter());
        }
//        response.sendRedirect(request.getContextPath());
    }
}
  