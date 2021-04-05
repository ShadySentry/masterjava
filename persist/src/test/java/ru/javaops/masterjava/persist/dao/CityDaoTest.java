package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.UserCitiTestData;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

import static ru.javaops.masterjava.persist.UserCitiTestData.FIRST2_CITIES;

public class CityDaoTest extends AbstractDaoTest<CityDao> {

    public CityDaoTest() {
        super(CityDao.class);
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        UserCitiTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        UserCitiTestData.setUp();
    }

    @Test
    public void getWithLimit() {
        List<City> cities = dao.getWithLimit(2);
        Assert.assertEquals(FIRST2_CITIES,dao.getWithLimit(2));
    }

    @Test
    public void insertBatch() throws Exception {
        dao.clean();
        dao.insertBatch(FIRST2_CITIES,2);
        Assert.assertEquals(2,dao.getWithLimit(100).size());
    }
}
