package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.UserCityTestData;
import ru.javaops.masterjava.persist.model.User;

import java.util.List;

import static ru.javaops.masterjava.persist.UserCityTestData.FIST5_USERS;

public class UserDaoTest extends AbstractDaoTest<UserDao> {

    public UserDaoTest() {
        super(UserDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        UserCityTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        UserCityTestData.setUp();
    }

    @Test
    public void getWithLimit() {
        List<User> users = dao.getWithLimit(5);
        Assert.assertEquals(FIST5_USERS, users);
    }

    @Test
    public void insertBatch() throws Exception {
        dao.clean();
        dao.insertBatch(FIST5_USERS, 5);
        Assert.assertEquals(5, dao.getWithLimit(100).size());
    }
}