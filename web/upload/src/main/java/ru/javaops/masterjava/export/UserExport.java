package ru.javaops.masterjava.export;

import ru.javaops.masterjava.model.User;
import ru.javaops.masterjava.model.UserFlag;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UserExport {
    public List<User> process(final InputStream is) throws XMLStreamException{
        StaxStreamProcessor processor =  new StaxStreamProcessor(is);
        List<User> users = new ArrayList<>();
        while (processor.doUntil(XMLEvent.START_ELEMENT,"User")){
//            final Integer id=Integer.valueOf(processor.getAttribute("id"));
            final String email=processor.getAttribute("email");
            final UserFlag flag = UserFlag.valueOf(processor.getAttribute("flag"));
            final String name = processor.getReader().getElementText();

            users.add(new User(name,email,flag));
        }



        return users;
    }
}