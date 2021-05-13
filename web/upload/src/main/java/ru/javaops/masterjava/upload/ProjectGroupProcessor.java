package ru.javaops.masterjava.upload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import one.util.streamex.StreamEx;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.dao.ProjectGroupDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.Project;
import ru.javaops.masterjava.persist.model.ProjectGroup;
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
    @Getter
    @Setter
    @AllArgsConstructor
    class Pair {
        private final Project project;
        private final Group group;

        @Override
        public int hashCode() {
            return group.hashCode() ^ project.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Pair)) {
                return false;
            }
            Pair that = (Pair) o;
            return this.group.equals(that.group) && this.project.equals(that.project);
        }
    }

    public Map<String, Group> process(StaxStreamProcessor processor) throws XMLStreamException {
        log.info("starting processing Projects and Groups");


        Map<String, Project> projects = projectDao.getAsMap();
        List<Project> newProjects = new ArrayList<>();
        Map<String, Group> groups = groupDao.getAsMap();
        List<Group> newGroups = new ArrayList<>();
//        Map<ProjectGroup> projectGroups = projectGroupDao.getAll();
        val loadedProjectGroups = new HashMap<String, Pair>();


        while (processor.startElement("Project", "Projects")) {
            val projectName = processor.getAttribute("name");
            int event=processor.getReader().next();
//            String description = processor.getText();
            String description  = processor.getElementValue("description");
            Project loadedProject = new Project(projectName, description);
            if (!projects.containsKey(projectName)) {
                newProjects.add(loadedProject);
            }
            event=processor.getReader().next();
            while (processor.startElement("Group", "Project")) {
                String groupName = processor.getAttribute("name");
                Group loadedGroup = new Group();
                loadedGroup.setName(groupName);
                loadedGroup.setType(GroupType.valueOf(processor.getAttribute("type")));

                if (!groups.containsKey(groupName)) {
                    newGroups.add(loadedGroup);
                }
                Pair projectGroupPair = new Pair(loadedProject,loadedGroup);

                String key  = projectGroupPair.getProject().getName()
                        .concat(projectGroupPair.getGroup().getName());
                if (!loadedProjectGroups.containsKey(key)){
                    loadedProjectGroups.put(key,projectGroupPair);
                }
            }
            //todo don`t need to check if projectGroup exists
        }

        projectDao.insertBatch(newProjects);
        groupDao.insertBatch(newGroups);

        projects=projectDao.getAsMap();
        groups=groupDao.getAsMap();

        return StreamEx.of(newGroups).toMap(Group::getName, g -> g);
    }

    private void persistProjectsGroups(HashMap<String, Project> projects, HashMap<String,Group> groups, Map<String, Pair> projectGroups){
        List<ProjectGroup> newProjectGroups = new ArrayList<>();
        for (Pair pair: projectGroups.values()){
            Integer projectId = projects.get(pair.getProject().getName()).getId();
            Integer groupId = groups.get(pair.getGroup().getName()).getId();
            newProjectGroups.add(new ProjectGroup(projectId,groupId));
        }
        projectGroupDao.insertBatch(newProjectGroups);
    }
}
