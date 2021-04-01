package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import com.sun.istack.internal.NotNull;
import lombok.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class User extends BaseEntity {
    @Column("full_name")
    private @NonNull String fullName;
    private @NonNull String email;
    private @NonNull UserFlag flag;
    @Column("city_ref")
    @NotNull private String cityRef;

    public User(Integer id, String fullName, String email, UserFlag flag,String cityRef) {
        this(fullName, email, flag, cityRef);
        this.id=id;
    }
}