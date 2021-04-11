package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.*;

import java.util.List;

import static ru.javaops.masterjava.persist.model.GroupType.current;
import static ru.javaops.masterjava.persist.model.GroupType.registering;

public class UserCityTestData {

    public static City spb;
    public static City msk;
    public static City hk;
    public static List<City> FIRST2_CITIES;

    public static User ADMIN;
    public static User DELETED;
    public static User FULL_NAME;
    public static User USER1;
    public static User USER2;
    public static User USER3;
    public static List<User> FIST5_USERS;

    public static void init() {

        spb = new City("spb", "Пітєр");
        msk = new City("msk", "Москва");
        hk = new City("hk", "Харків");
        FIRST2_CITIES = ImmutableList.of(msk, spb);

        ADMIN = new User("Admin", "admin@javaops.ru", UserFlag.superuser, "spb");
        DELETED = new User("Deleted", "deleted@yandex.ru", UserFlag.deleted, "msk");
        FULL_NAME = new User("Full Name", "gmail@gmail.com", UserFlag.active, "spb");
        USER1 = new User("User1", "user1@gmail.com", UserFlag.active, "spb");
        USER2 = new User("User2", "user2@yandex.ru", UserFlag.active, "spb");
        USER3 = new User("User3", "user3@yandex.ru", UserFlag.active, "msk");
        FIST5_USERS = ImmutableList.of(ADMIN, DELETED, FULL_NAME, USER1, USER2);

    }

    public static void setUp() {
        CityDao cityDao = DBIProvider.getDao(CityDao.class);
        cityDao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            FIRST2_CITIES.forEach(cityDao::insert);
            cityDao.insert(hk);
        });

        UserDao userDao = DBIProvider.getDao(UserDao.class);
        userDao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            FIST5_USERS.forEach(userDao::insert);
            userDao.insert(USER3);
        });

    }
}