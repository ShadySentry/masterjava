package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import com.sun.istack.internal.NotNull;
import lombok.*;

@RequiredArgsConstructor
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
public class Group extends BaseEntity{

    @NotNull
    private String name;

    @Column("group_type")
    @NotNull
    private GroupType groupType;

    @Column("project_id")
    private Integer projectId;
}
