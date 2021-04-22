package ru.javaops.masterjava.service.mail.persist;

import com.bertoncelj.jdbi.entitymapper.Column;
import com.google.common.base.Joiner;
import lombok.*;
import ru.javaops.masterjava.persist.model.BaseEntity;
import ru.javaops.masterjava.service.mail.Addressee;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class MailCase extends BaseEntity {
    private @NonNull
    String receivers;
    private String cc;
    private String subject;
    private String result;
    private String state;
    @Column("date_time")
    private Timestamp dateTime;

    public static MailCase of(List<Addressee> to, List<Addressee> cc, String subject, String result, String state) {
        return new MailCase(Joiner.on(", ").join(to), Joiner.on(", ").join(cc), subject, state, result,
                new Timestamp(System.currentTimeMillis()));
    }
}
