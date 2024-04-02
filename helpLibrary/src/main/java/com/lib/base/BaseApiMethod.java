package com.lib.base;

public class BaseApiMethod {

    public static boolean isEqual(String source, String... urlEndPoints) {
        if (source != null && urlEndPoints != null) {
            for (String urlEndPoint : urlEndPoints) {
                if (source.equals(urlEndPoint)) {
                    return true;
                }
            }
        }
        return false;
    }

}
