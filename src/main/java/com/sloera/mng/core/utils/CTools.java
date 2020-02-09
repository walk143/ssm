package com.sloera.mng.core.utils;

import java.util.UUID;

public class CTools {
    public CTools(){}

    public static String getUUID() {
        String var0 = UUID.randomUUID().toString();
        var0 = var0.substring(0, 8) + var0.substring(9, 13) + var0.substring(14, 18) + var0.substring(19, 23) + var0.substring(24);
        return var0.toUpperCase();
    }

}
