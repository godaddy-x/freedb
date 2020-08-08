package com.pithy.free.crud.domain;

import java.util.HashMap;
import java.util.Map;

public class RetMap {

    private Map<String, Object> map = new HashMap<>();

    public RetMap add(String key, Object val) {
        if (key != null && key.length() > 0 && val != null) {
            map.put(key, val);
        }
        return this;
    }

    public Map<String, Object> done() {
        return map;
    }

}
