package com.guiswitehome.tplinkutil.model;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum PoePowerStatus {
    On(2), Off(0);

    PoePowerStatus(Integer value) {
        this.value = value;
    }

    private final Integer value;

    public static PoePowerStatus getByValue(Integer value) {
        return Arrays.stream(PoePowerStatus.values())
                .filter(p -> p.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Cannot find PoePowerStatus by value: " + value));
    }

    public static PoePowerStatus getByValue(String value) {
        return getByValue(Integer.parseInt(value));
    }
}
