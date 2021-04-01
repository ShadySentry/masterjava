package ru.javaops.masterjava.persist.model;

import com.sun.istack.internal.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class City extends RefEntity{
    public City(String ref,String name) {
        super(ref);
        this.name = name;
    }

    private @NotNull String name;
}
