package com.pithy.free.spring.utils;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {

    private Map<String, Object> map = new HashMap<>();

    public MapUtil Add(String key, Object val) {
        map.put(key, val);
        return this;
    }

    public Map<String, Object> Map() {
        return map;
    }

}
