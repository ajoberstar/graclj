package org.graclj.internal;

public final class Util {
    private Util() {}

    public static String capitalize(String str) {
        // TODO: Figure out why I couldn't access commons-lang
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
