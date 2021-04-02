package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class Project extends RefEntity {

    @NonNull private String name;

    @NonNull private String description;

}
