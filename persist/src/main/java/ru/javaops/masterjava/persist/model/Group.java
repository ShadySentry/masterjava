package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@RequiredArgsConstructor
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
public class Group extends BaseEntity{

    @NonNull
    private String name;

    @Column("group_type")
    @NonNull
    private GroupType groupType;

    @Column("project_id")
    private Integer projectId;
}
