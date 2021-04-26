package ru.javaops.masterjava.service.mail.persist;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.dao.AbstractDaoTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MailCaseDaoTest extends AbstractDaoTest<MailCaseDao> {
    List<MailCase> CASES  = ImmutableList.copyOf(MailCaseDaoTestData.CASES);


//    private final List<MailCase> CASES=ImmutableList.Builder();

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
        int totalCases = CASES.size();
        final List<MailCase> logs = dao.getAll();
        assertEquals(totalCases, logs.size());
        for(MailCase mailCase:CASES){
            assertTrue(containsCase(mailCase,CASES));
        }
    }



    private boolean containsCase(MailCase thiCase, List<MailCase> thatCases){
        for(MailCase thatCase:thatCases){
            if(thiCase.getReceivers().compareTo(thatCase.getReceivers())==0 &&
                    thiCase.getCc().compareTo(thatCase.getCc())==0 &&
                    thiCase.getResult().compareTo(thatCase.getResult())==0 &&
                    thiCase.getState().compareTo(thatCase.getState())==0){
                return true;
            }
        }
        return false;
    }
}
