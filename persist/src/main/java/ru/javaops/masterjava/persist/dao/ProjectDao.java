package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import one.util.streamex.StreamEx;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.model.Project;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class ProjectDao implements AbstractDao {
    public Project insert(Project project){
        if(project.isNew()){
            int id = insertGeneratedId(project);
            project.setId(id);
        }else {
            insertWIthId(project);
        }
        return project;
    }

    @SqlQuery("SELECT nextval('common_seq')")
    abstract int getNextVal();

    @Transaction
    public int getAndSkip(int step) {
        int id = getNextVal();
        DBIProvider.getDBI().useHandle(h -> h.execute("ALTER SEQUENCE common_seq RESTART WITH " + id + step));
        return id;
    }

    @SqlUpdate("insert into project (id, name, description) values(:id, :name, :description)")
    abstract void insertWIthId(@BindBean Project project);

    @SqlUpdate("insert into project (name, description) values(:name, :description)")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean Project project);

    @SqlBatch("INSERT INTO project (id, name, description) VALUES (:id, :name, :description)")
    public abstract void insertBatch(@BindBean Collection<Project> projects, @BatchChunkSize int chunkSize);

    @SqlQuery("Select * FROM project ORDER BY name Limit :it")
    public abstract List<Project> getWithLimit(@Bind int limit);

    @SqlQuery("select * from project")
    public abstract List<Project> getAll();

    public Map<String, Project> getAsMap() {
        return StreamEx.of(getAll()).toMap(Project::getName, Function.identity());
    }

    @SqlUpdate("TRUNCATE project cascade")
    @Override
    public abstract void clean();
}
