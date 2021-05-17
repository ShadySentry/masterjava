package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;
import ru.javaops.masterjava.persist.model.type.UserFlag;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class User extends BaseEntity {
    @Column("full_name")
    private @NonNull
    String fullName;
    private @NonNull
    String email;
    private @NonNull
    UserFlag flag;
    @Column("city_ref")
    private @NonNull
    String cityRef;
    private String[] groupRefs;

    public User(Integer id, String fullName, String email, UserFlag flag, String cityRef, String[] groupRefs) {
        this(fullName, email, flag, cityRef, groupRefs);
        this.id = id;
    }
}