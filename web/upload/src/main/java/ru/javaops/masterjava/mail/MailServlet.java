package ru.javaops.masterjava.mail;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ru.javaops.masterjava.common.web.ThymeleafListener.engine;

@WebServlet(urlPatterns = "/mail")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10)
public class MailServlet extends HttpServlet {

    private static UserDao userDao = DBIProvider.getDao(UserDao.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User>users= userDao.getWithLimit(100);

        out(req, resp,users);
    }

    private void out(HttpServletRequest req, HttpServletResponse resp, List<User> users) throws IOException {
        resp.setCharacterEncoding("utf-8");

        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale(),
                ImmutableMap.of("users", users));
        engine.process("mail",webContext,resp.getWriter());
    }
}
