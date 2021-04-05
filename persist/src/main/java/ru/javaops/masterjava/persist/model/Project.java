package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Project extends BaseEntity {

    @NonNull private String name;

    @NonNull private String description;

    public Project(Integer id,String name, String description){
        super(id);
        this.name=name;
        this.description=description;
    }


}
