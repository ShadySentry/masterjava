package ru.javaops.masterjava.service.mail.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.service.mail.Addressee;

import java.util.List;

public class MailCaseDaoTestData {
    public static MailCase logRecord1;
    public static MailCase logRecord2;
    public static MailCase logRecord3;
    public static MailCase logRecord4;
    public static List<MailCase> CASES;

    public static final List<Addressee> TO_ADDRESSEES = ImmutableList.of(
            new Addressee("user1","user1@mail.ru"),
            new Addressee("user2","user2@gmail.com"));

    public static final List<Addressee> CC_ADDRESSEES = ImmutableList.of(
            new Addressee("user3","user3@i.ua"),
            new Addressee("user4","user4@gmail.com"));

    public static void init(){
        logRecord1 = new MailCase("admin@javaops.ru","User1@gmail.com","User2@yandex.ru",
                "subject","<1376462019.0.1619074967547@localhost>",
                null);
        logRecord2 = MailCase.of(TO_ADDRESSEES,CC_ADDRESSEES,"subject2",
                "<1111112019.0.1619074967547@localhost>","state2");
        logRecord3 = MailCase.of(TO_ADDRESSEES,CC_ADDRESSEES,"subject3",
                "<1111112019.0.1619074967547@localhost>","state3");
        CASES = ImmutableList.of(logRecord2,logRecord2,logRecord3);
    }

    public static void setUp(){
        MailCaseDao dao= DBIProvider.getDao(MailCaseDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction(((conn, status) -> {
            dao.insert(logRecord1);
            CASES.forEach(dao::insert);
        }));
    }

}
