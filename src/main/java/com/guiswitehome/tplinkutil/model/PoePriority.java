package com.guiswitehome.tplinkutil.model;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum PoePriority {
    High(0), Low(2);

    PoePriority(Integer value) {
        this.value = value;
    }

    private final Integer value;

    public static PoePriority getByValue(Integer value) {
        return Arrays.stream(PoePriority.values())
                .filter(p -> p.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Cannot find PoePriority by value: " + value));
    }

    public static PoePriority getByValue(String value) {
        return getByValue(Integer.parseInt(value));
    }
}
