package io.vproxy.pojoagent.api;

@SuppressWarnings("UnusedReturnValue")
public class Version {
    public static final String VERSION = "1.1.0-dev"; // _THE_VERSION_
    public static final long VERSION_INT = -1_001_000L;

    private Version() {
    }

    public static String stringifyVersion(long ver) {
        boolean isDev = ver < 0;
        if (isDev) {
            ver = -ver;
        }
        long maj = ver / 1_000_000;
        long min = (ver % 1_000_000) / 1_000;
        long patch = ver % 1_000;
        String str = maj + "." + min + "." + patch;
        if (isDev) {
            str += "-dev";
        }
        return str;
    }
}
