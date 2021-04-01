package ru.javaops.masterjava.persist.model;

import com.sun.istack.internal.NotNull;
import lombok.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor

@EqualsAndHashCode
public class RefEntity {
    @Getter
    @NotNull
    private String ref;
}
