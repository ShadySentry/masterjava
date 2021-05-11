package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import one.util.streamex.StreamEx;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.ProjectGroup;
import ru.javaops.masterjava.persist.model.UserGroup;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class ProjectGroupDao implements AbstractDao{

    @SqlUpdate("Truncate project_group CASCADE")
    @Override
    public void clean() {

    }

    @SqlBatch("INSERT INTO project_groups (project_id, group_id) VALUES (:projectId, :groupId)")
    public abstract void insertBatch(@BindBean List<ProjectGroup> projectGroups);

    @SqlQuery("SELECT * FROM project_groups")
    public abstract Set<ProjectGroup> getAll();

    @SqlQuery("SELECT project_id FROM project_groups where group_id =:it")
    public abstract Set<Integer> getProjectIds(@Bind int groupId);

    public static List<ProjectGroup> toProjectGroups(int projectId, Integer... groupIds){
        return StreamEx.of(groupIds).map(groupId->new ProjectGroup(projectId,groupId)).toList();
    }

    public static Set<Integer> getByProjectId(int projectId, List<ProjectGroup> projectGroups){
        return StreamEx.of(projectGroups).filter(projectGroup -> projectGroup.getProjectId()==projectId).map(ProjectGroup::getGroupId).toSet();
    }
}
