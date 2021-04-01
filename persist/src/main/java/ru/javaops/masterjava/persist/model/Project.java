package ru.javaops.masterjava.persist.model;

import com.sun.istack.internal.NotNull;
import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class Project extends RefEntity {

    @NotNull private String name;

    @NotNull private String description;

}
