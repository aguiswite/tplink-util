package com.guiswitehome.tplinkutil.model;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.NoSuchElementException;

@AllArgsConstructor
public enum PoeStatus {
    Enabled("Enable", 1), Disabled("Disable", 0);

    private final String rawName;
    private final Integer value;

    public static PoeStatus getPoeStatusByRawName(String rawName) {
        return Arrays.stream(PoeStatus.values())
                .filter(p -> StringUtils.equals(rawName, p.rawName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Cannot find PoeStatus by rawName: " + rawName));
    }

    public static PoeStatus getPoeStatusByValue(Integer value) {
        return Arrays.stream(PoeStatus.values())
                .filter(p -> p.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Cannot find PoeStatus by value: " + value));
    }

    public static PoeStatus getPoeStatusByValue(String value) {
        return getPoeStatusByValue(Integer.parseInt(value));
    }
}
