package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import one.util.streamex.StreamEx;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.model.City;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao {


    @SqlQuery("SELECT nextval('common_seq)")
    abstract int getNextVal();

    @Transaction
    public int getAndSkip(int step) {
        int id = getNextVal();
        DBIProvider.getDBI().useHandle(h -> h.execute("ALTER SEQUENCE common_seq RESTART WITH " + id + step));
        return id;
    }

    @SqlUpdate("insert into city (ref,name) values(:ref, :name)")
    abstract void insert(@BindBean City city);

    @SqlBatch("INSERT INTO city (ref, name) VALUES (:ref, :name)")
    abstract void insertBatch(@BindBean Collection<City> cities);

    @SqlQuery("Select * from city Limit :limit ")
    abstract void getWithLimit(@Bind int limit);

    @SqlQuery("select * from city")
    public abstract List<City> getAll();

    public Map<String, City> getAsMap() {
        return StreamEx.of(getAll()).toMap(City::getRef, Function.identity());
    }

    @SqlUpdate("TRUNCATE city cascade")
    @Override
    public abstract void clean();
}
