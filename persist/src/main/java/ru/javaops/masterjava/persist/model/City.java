package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class City extends RefEntity{
    public City(String ref,String name) {
        super(ref);
        this.name = name;
    }

    private @NonNull
    String name;
}
