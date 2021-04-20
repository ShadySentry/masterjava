package ru.javaops.masterjava.service.mail.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;

import java.util.Collection;
import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class MailLogDao implements AbstractDao {

    @SqlUpdate("TRUNCATE mail_sender CASCADE")
    @Override
    public abstract void clean();

    @SqlQuery("SELECT * from mail_sender ORDER BY date_time")
    public abstract List<MailLog> getAll();

    @SqlUpdate("INSERT INTO mail_sender (sender, receivers, cc, subject, result) VALUES (:sender, :receivers, :cc, :subject, :result)")
    public abstract void insert(@BindBean MailLog mailLog);

    @SqlBatch("INSERT INTO mail_sender (sender, receivers, cc, subject, result) VALUES (:sender, :receivers, :cc, :subject, :result)")
    public abstract void insertBatch(@BindBean Collection<MailLog> mailLogs);
}
