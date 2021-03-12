package ru.javaops.masterjava.xml;

import com.google.common.io.Resources;
import one.util.streamex.StreamEx;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;
import ru.javaops.masterjava.xml.schema.Project;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import java.net.URL;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class MainXml {
    private final Comparator userComparator = Comparator.comparing(User::getFullName).thenComparing(User::getEmail);
    public static void main(String[] args) throws Exception {
//        if(args.length<1){
//            throw new IllegalArgumentException("mast contain projectName");
//        }
        String projectName = "masterJava";
        URL payloadUrl = Resources.getResource("payload.xml");
        MainXml main = new MainXml();
        Set<User> users = main.getUsers(projectName);
//        String out = outHtml(users,projectName, Paths.get("out/usersJaxb.html"));


    }

    public Set<User> getUsers(String projectName) throws Exception {
        JaxbParser parser = new JaxbParser(ObjectFactory.class);
        parser.setSchema(Schemas.ofClasspath("payload.xsd"));
        Payload payload = parser.unmarshal(
                Resources.getResource("payload.xml").openStream());
        String strPayload = parser.marshal(payload);

        Set<User> users = new TreeSet<>(userComparator);
        Project project = StreamEx.of(payload.getProjects().getProject())
                .filter(p -> p.getName().equals(projectName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Invalid project name"));
        Set<Project.Group> groups = new HashSet<>(project.getGroup());

        users = StreamEx.of(payload.getUsers().getUser())
                .filter(u -> StreamEx.of(u.getGroupRefs())
                        .findAny(groups::contains)
                        .isPresent())
                .collect(Collectors.toSet());


        return users;
    }
}
