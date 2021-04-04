package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import one.util.streamex.StreamEx;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.persist.model.Project;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class ProjectDao implements AbstractDao {
    @SqlQuery("SELECT nextval('common_seq')")
    abstract int getNextVal();

    @Transaction
    public int getAndSkip(int step) {
        int id = getNextVal();
        DBIProvider.getDBI().useHandle(h -> h.execute("ALTER SEQUENCE common_seq RESTART WITH " + id + step));
        return id;
    }

    @SqlUpdate("insert into project (ref, name, description) values(:ref, :name, :description)")
    abstract void insert(@BindBean Project project);

    @SqlBatch("INSERT INTO project (ref, name, description) VALUES (:ref, :name, :description)")
    abstract void insertBatch(@BindBean Collection<Project> projects);

    @SqlQuery("Select * from project Limit :limit ")
    abstract List<Project> getWithLimit(@Bind int limit);

    @SqlQuery("select * from project")
    public abstract List<Project> getAll();

    public Map<String, Project> getAsMap() {
        return StreamEx.of(getAll()).toMap(Project::getRef, Function.identity());
    }

    @SqlQuery("TRUNCATE project CASCADE")
    @Override
    public void clean() {

    }
}
