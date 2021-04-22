package ru.javaops.masterjava.service.mail.persist;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.dao.AbstractDaoTest;

import java.util.List;

public class MailCaseDaoTest extends AbstractDaoTest<MailCaseDao> {

    public MailCaseDaoTest() {
        super(MailCaseDao.class);
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        MailCaseDaoTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        MailCaseDaoTestData.setUp();
    }

    @Test
    public void getAll() {
        final List<MailCase> logs = dao.getAll();
    }
}
