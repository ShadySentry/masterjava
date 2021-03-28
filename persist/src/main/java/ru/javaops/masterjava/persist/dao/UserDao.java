package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class UserDao implements AbstractDao {

    public User insert(User user) {
        if (user.isNew()) {
            int id = insertGeneratedId(user);
            user.setId(id);
        } else {
            insertWitId(user);
        }
        return user;
    }

    public List<User> insertAll(List<User> users,int chunkSize){
        List<User> savedUsers = new ArrayList<>();
        Iterator<User> iterator =users.iterator();
        insertBatch(iterator,chunkSize);

        savedUsers=getWithLimit(users.size());

        return savedUsers;
    }

    @SqlBatch("insert into users (full_name, email, flag)" +
            " values (:fullName, :email, CAST(:flag AS user_flag)) " +
            "ON CONFLICT (email) DO NOTHING")
    abstract void insertBatch(@BindBean Iterator<User> users,@BatchChunkSize int chunkSize);

    @SqlUpdate("INSERT INTO users (full_name, email, flag) VALUES (:fullName, :email, CAST(:flag AS user_flag)) ON CONFLICT (email) DO NOTHING")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean User user);

    @SqlUpdate("INSERT INTO users (id, full_name, email, flag) VALUES (:id, :fullName, :email, CAST(:flag AS user_flag)) ON CONFLICT (email) DO NOTHING")
    abstract void insertWitId(@BindBean User user);

    @SqlQuery("SELECT * FROM users ORDER BY full_name, email LIMIT :it")
    public abstract List<User> getWithLimit(@Bind int limit);

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE users")
    @Override
    public abstract void clean();
}