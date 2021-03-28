package ru.javaops.masterjava.upload;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.model.UserFlag;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class UserProcessor {

    public List<User> process(final InputStream is) throws XMLStreamException {
        final StaxStreamProcessor processor = new StaxStreamProcessor(is);
        List<User> users = new ArrayList<>();

        while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
            final String email = processor.getAttribute("email");
            final UserFlag flag = UserFlag.valueOf(processor.getAttribute("flag"));
            final String fullName = processor.getReader().getElementText();
            final User user = new User(fullName, email, flag);
            users.add(user);
        }
        return users;
    }

    public void persist(List<User> users){
        persist(users,10);
    }

    public void persist(List<User> users, int chunkSize)
    {
        if(users.isEmpty()){
            return;
        }
        List<User> usersImmutable = ImmutableList.copyOf(users);
        UserDao dao= DBIProvider.getDao(UserDao.class);
        DBIProvider.getDBI().useTransaction((conn, status) -> {
//            usersImmutable.forEach(dao::insert);
            dao.insertAll(users,chunkSize);
        });
    }

    public List<User> getUsersSorted(int limit){
        if(limit<=0){
            limit=10;
        }

        AtomicReference<List<User>> foundUsers = new AtomicReference<List<User>>();
        UserDao dao=DBIProvider.getDao(UserDao.class);
        int finalLimit = limit;

        DBIProvider.getDBI().useTransaction((conn, status)->{
            foundUsers.set(dao.getWithLimit(finalLimit));
        });

        return foundUsers.get();
    }


}
