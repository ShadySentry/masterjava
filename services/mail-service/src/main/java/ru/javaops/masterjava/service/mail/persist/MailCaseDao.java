package ru.javaops.masterjava.service.mail.persist;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.dao.AbstractDao;

import java.util.Collection;
import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class MailCaseDao implements AbstractDao {

    @SqlUpdate("TRUNCATE mail_log CASCADE")
    @Override
    public abstract void clean();

    @SqlQuery("SELECT * from  mail_log ORDER BY date_time")
    public abstract List<MailCase> getAll();

    @SqlUpdate("INSERT INTO  mail_log (receivers, cc, subject, result, state) VALUES (:receivers, :cc, :subject, :result, :state)")
    public abstract void insert(@BindBean MailCase mailCase);

    @SqlBatch("INSERT INTO  mail_log (receivers, cc, subject, result, state) VALUES (:receivers, :cc, :subject, :result, :state)")
    public abstract void insertBatch(@BindBean Collection<MailCase> mailCases);
}
