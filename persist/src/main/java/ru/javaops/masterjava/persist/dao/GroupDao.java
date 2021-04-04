package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import one.util.streamex.StreamEx;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.persist.model.Group;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class GroupDao implements AbstractDao {

    @SqlUpdate("TRUNCATE groups CASCADE")
    @Override
    public void clean() {
    }

    @SqlQuery("SELECT nextval 'common_seq")
    abstract int getNextVal();

    @Transaction
    public int getAndSkip(int step) {
        int id = getNextVal();
        DBIProvider.getDBI().useHandle(h -> h.execute("ALTER SEQUENCE common_seq RESTART WITH " + id + step));
        return id;
    }

    @SqlQuery("INSERT INTO groups (name, project_id,group_type) VALUES (:name, :projectId, : CAST(groupType AS GROUP_TYPE)")
    abstract void insert(@BindBean Group group);

    @SqlQuery("INSERT INTO groups (name, project_id,group_type) VALUES (:name, :projectId, : CAST(groupType AS GROUP_TYPE)")
    abstract void insertBatch(@BindBean Collection<Group> group);

    @SqlQuery("SELECT * FROM groups LIMIT :limit")
    abstract List<Group> getWIthLimit(@Bind int limit);

    @SqlQuery("SELECT * FROM groups")
    abstract List<Group> getAll();

    public Map<Integer, Group> getAsMap() {
        return StreamEx.of(getAll()).toMap(Group::getId, Function.identity());
    }


}
