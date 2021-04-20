package ru.javaops.masterjava.service.mail.dao;

import lombok.*;
import com.bertoncelj.jdbi.entitymapper.Column;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class MailLog extends BaseEntity {
    private @NonNull String sender;
    private @NonNull String receivers;
    private String cc;
    private String subject;
    private String result;
    @Column("date_time")
    private Timestamp dateTime;
}
