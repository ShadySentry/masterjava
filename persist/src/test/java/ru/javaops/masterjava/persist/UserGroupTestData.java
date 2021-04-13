package ru.javaops.masterjava.persist;

import ru.javaops.masterjava.persist.dao.UserGroupDao;
import ru.javaops.masterjava.persist.model.type.UserGroup;

import java.util.List;
import java.util.Set;

import static ru.javaops.masterjava.persist.GroupTestData.*;
import static ru.javaops.masterjava.persist.UserCityTestData.ADMIN;
import static ru.javaops.masterjava.persist.dao.UserGroupDao.toUserGroups;

public class UserGroupTestData {
    public static List<UserGroup> USER_GROUPS;


    public static void init(){
        GroupTestData.init();
        GroupTestData.setUp();

        UserCityTestData.init();
        UserCityTestData.setUp();

        USER_GROUPS = toUserGroups (ADMIN.getId(),TOPJAVA_06_ID,TOPJAVA_07_ID,TOPJAVA_08_ID,MASTERJAVA_01_ID);
        USER_GROUPS.addAll(toUserGroups(UserCityTestData.FULL_NAME.getId(), TOPJAVA_07_ID, MASTERJAVA_01_ID));
        USER_GROUPS.addAll(toUserGroups(UserCityTestData.USER1.getId(), TOPJAVA_06_ID, MASTERJAVA_01_ID));
        USER_GROUPS.add(new UserGroup(UserCityTestData.USER2.getId(), MASTERJAVA_01_ID));
        USER_GROUPS.add(new UserGroup(UserCityTestData.USER3.getId(), MASTERJAVA_01_ID));
    }

    public static void setUp(){
        UserGroupDao dao = DBIProvider.getDao(UserGroupDao.class);
        dao.clean();

        DBIProvider.getDBI().useTransaction(((conn, status) -> {
            dao.insertBatch(USER_GROUPS,5);
        }));
    }

    public static Set<Integer> getByGroupId(int groupId){
        return UserGroupDao.getByGroupId(groupId,USER_GROUPS);
    }
}
