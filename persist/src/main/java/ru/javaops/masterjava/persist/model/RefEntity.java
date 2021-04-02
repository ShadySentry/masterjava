package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RefEntity {
    @Getter
    @NonNull
    private String ref;
}
