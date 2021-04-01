package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.model.City;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao{


    @SqlQuery("SELECT nextval('city_seq)")
    abstract int getNextVal();

    @Transaction
    public int getAndSkip(int step){
        int id=getNextVal();
        DBIProvider.getDBI().useHandle(h->h.execute("ALTER SEQUENCE city_seq RESTART WITH "+id+step));
        return id;
    }

    @SqlQuery("INSERT INTO cities (ref, name) VALUES (:ref, :name)")
    @GetGeneratedKeys
    abstract void insertGeneratedId(@BindBean City city);

    @SqlQuery("")
    abstract void insertWithId(@BindBean City city);


    @SqlUpdate("TRUNCATE cities")
    @Override
    public void clean() {

    }
}
