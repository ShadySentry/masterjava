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

public class UserCitiTestData {

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

//    public static Project project1;
//    public static Project project2;
//    public static Project project3;
//    public static List<Project> FIRST2_PROJECTS;
//
//    public static Group group1;
//    public static Group group2;
//    public static Group group3;
//    public static Group group4;
//    public static List<Group> FIRST3_GROUPS;
//
//    public static UserGroup userGroup1;
//    public static UserGroup userGroup2;

    public static void init() {
//        project1 = new Project(1, "Topjava", "topjava description");
//        project2 = new Project(2, "Masterjava", "masterjava description");
//        project3 = new Project(3, "rnd", "rnd decription");
//        FIRST2_PROJECTS = ImmutableList.of(project1, project2);
//
//        group1 = new Group("topjava 6", REGISTERING, project1.getId());
//        group2 = new Group("topjava 21", REGISTERING, project1.getId());
//        group3 = new Group("topjava 20", CURRENT, project1.getId());
//        group4 = new Group("masterjava 21", CURRENT, project2.getId());
//        FIRST3_GROUPS = ImmutableList.of(group1, group2, group3);

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
//        ProjectDao projectDao = DBIProvider.getDao(ProjectDao.class);
//        projectDao.clean();
//        DBIProvider.getDBI().useTransaction((conn, status) -> {
//            FIRST2_PROJECTS.forEach(projectDao::insert);
//            projectDao.insert(project3);
//        });
//
//        GroupDao groupDao = DBIProvider.getDao(GroupDao.class);
//        groupDao.clean();
//        DBIProvider.getDBI().useTransaction((conn, status) -> {
//            FIRST3_GROUPS.forEach(groupDao::insert);
//            groupDao.insert(group4);
//        });

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
