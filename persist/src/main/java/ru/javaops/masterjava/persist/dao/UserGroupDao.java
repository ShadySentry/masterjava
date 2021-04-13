package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import one.util.streamex.StreamEx;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.type.UserGroup;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class UserGroupDao implements AbstractDao {

    @SqlUpdate("TRUNCATE users_groups CASCADE")
    @Override
    public abstract void clean();

    @SqlUpdate("INSERT INTO users_groups (user_id, group_id) VALUES(:userId, :groupId)")
    public abstract int insert(@BindBean UserGroup userGroup);

    @SqlBatch("INSERT INTO users_groups (user_id, group_id) VALUES(:userId, :groupId)")
    public abstract void insertBatch(@BindBean Collection<UserGroup> userGroup, @BatchChunkSize int chunkSize);

    @SqlBatch("INSERT INTO user_group (user_id, group_id) VALUES (:userId, :groupId)")
    public abstract void insertBatch(@BindBean List<UserGroup> userGroups);

    @SqlQuery("SELECT user_id FROM users_groups WHERE group_id =:it")
    public abstract Set<Integer> getUserIds(@Bind int groupId);

    @SqlQuery("SELECT * FROM users_groups ORDER BY group_id")
    public abstract Set<Integer> getAll();

    public static List<UserGroup> toUserGroups(int userId, Integer... groupIds) {
        return StreamEx.of(groupIds).map(groupId -> new UserGroup(userId, groupId)).toList();
    }

    public static Set<Integer> getByGroupId(int groupId, List<UserGroup> userGroups) {
        return StreamEx.of(userGroups)
                .filter(userGroup -> userGroup.getGroupId() == groupId)
                .map(UserGroup::getUserId)
                .toSet();
    }
}
