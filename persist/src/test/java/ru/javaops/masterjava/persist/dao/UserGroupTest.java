package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.GroupTestData;
import ru.javaops.masterjava.persist.UserGroupTestData;

import java.util.Set;

import static ru.javaops.masterjava.persist.GroupTestData.TOPJAVA_06_ID;
import static ru.javaops.masterjava.persist.UserGroupTestData.getByGroupId;

public class UserGroupTest extends AbstractDaoTest<UserGroupDao> {

    public UserGroupTest() {
        super(UserGroupDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        UserGroupTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        UserGroupTestData.setUp();
    }

    @Test
    public void getUserIdsTest() {
        Set<Integer> usersId = dao.getUserIds(GroupTestData.MASTERJAVA_01_ID);
        Assert.assertEquals(getByGroupId(GroupTestData.MASTERJAVA_01_ID), usersId);

        usersId = dao.getUserIds(TOPJAVA_06_ID);
        Assert.assertEquals(getByGroupId(TOPJAVA_06_ID),usersId);

    }
}
