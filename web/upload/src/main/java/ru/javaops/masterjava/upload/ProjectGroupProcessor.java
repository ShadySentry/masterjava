package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.dao.ProjectGroupDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.Project;
import ru.javaops.masterjava.persist.model.type.GroupType;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ProjectGroupProcessor {
    private final ProjectDao projectDao = DBIProvider.getDao(ProjectDao.class);
    private final GroupDao groupDao = DBIProvider.getDao(GroupDao.class);
    private final ProjectGroupDao projectGroupDao = DBIProvider.getDao(ProjectGroupDao.class);

    public Map<Group, Project> process(StaxStreamProcessor processor) throws XMLStreamException {
        log.info("starting processing Projects and Groups");

        Map<String, Project> projects = projectDao.getAsMap();
        List<Project> newProjects = new ArrayList<>();
        Map<String, Group> groups = groupDao.getAsMap();
        List<Group> newGroups = new ArrayList<>();
        val projectGroups = projectGroupDao.getAll();
        val newProjectGroups = new HashMap<Project, Group>();

        while (processor.startElement("Project", "Projects")) {
            val projectName = processor.getAttribute("name");
            if(!projects.containsKey(projectName)){
                processor.getReader().nextTag();
                String description = processor.getText();
                newProjects.add(new Project(projectName,description));

            }
            String groupName = processor.getAttribute("name");
            while (processor.startElement("Group","Project")){

                if(!groups.containsKey(groupName)){
                    val gr= new Group();
                    gr.setName(groupName);
                    gr.setType(GroupType.valueOf(processor.getAttribute("type")));
                    newGroups.add(gr);
                }
            }

            //todo don`t need to check if projectGroup exists

        }

        projectDao.insertBatch(newProjects);
        groupDao.insertBatch(newGroups);
        //get id's to create projectGroup relations
        return null;
    }
}
