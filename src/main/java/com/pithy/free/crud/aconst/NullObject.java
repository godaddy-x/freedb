package com.pithy.free.crud.aconst;

public class NullObject {

    public static boolean valid(Object object) {
        return object != null && object instanceof NullObject;
    }

}
