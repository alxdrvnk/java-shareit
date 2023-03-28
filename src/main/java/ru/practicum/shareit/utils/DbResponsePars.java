package ru.practicum.shareit.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class DbResponsePars {

    public static Map<String, Integer> aliasToIndexMap(String[] aliases) {
        Map<String, Integer> aliasToIndexMap = new LinkedHashMap<>();

        for (int i = 0; i < aliases.length; i++) {
            aliasToIndexMap.put(aliases[i].toLowerCase(), i);
        }
        return aliasToIndexMap;
    }
}
