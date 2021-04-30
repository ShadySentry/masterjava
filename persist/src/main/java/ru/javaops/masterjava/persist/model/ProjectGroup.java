package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectGroup {
    @NonNull
    @Column("project_id")
    private Integer projectId;

    @NonNull
    @Column("group__id")
    private Integer groupId;
}
