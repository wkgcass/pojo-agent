package io.vproxy.pojoagent.api;

import io.vproxy.pojoagent.api.internal.PojoAgentInternal;
import io.vproxy.pojoagent.api.internal.RequirePojoCallerException;

@SuppressWarnings("unused")
public class PojoAgent {
    private PojoAgent() {
    }

    public static boolean fieldIsSet(Object o) {
        throw new RequirePojoCallerException();
    }

    public static boolean fieldIsSet(int o) {
        throw new RequirePojoCallerException();
    }

    public static boolean fieldIsSet(long o) {
        throw new RequirePojoCallerException();
    }

    public static boolean fieldIsSet(float o) {
        throw new RequirePojoCallerException();
    }

    public static boolean fieldIsSet(double o) {
        throw new RequirePojoCallerException();
    }

    public static boolean fieldIsSet(short o) {
        throw new RequirePojoCallerException();
    }

    public static boolean fieldIsSet(byte o) {
        throw new RequirePojoCallerException();
    }

    public static boolean fieldIsSet(char o) {
        throw new RequirePojoCallerException();
    }

    public static boolean fieldIsSet(boolean o) {
        throw new RequirePojoCallerException();
    }

    public static void unsetField(Object o) {
        throw new RequirePojoCallerException();
    }

    public static void unsetField(int o) {
        throw new RequirePojoCallerException();
    }

    public static void unsetField(long o) {
        throw new RequirePojoCallerException();
    }

    public static void unsetField(float o) {
        throw new RequirePojoCallerException();
    }

    public static void unsetField(double o) {
        throw new RequirePojoCallerException();
    }

    public static void unsetField(short o) {
        throw new RequirePojoCallerException();
    }

    public static void unsetField(byte o) {
        throw new RequirePojoCallerException();
    }

    public static void unsetField(char o) {
        throw new RequirePojoCallerException();
    }

    public static void unsetField(boolean o) {
        throw new RequirePojoCallerException();
    }

    /**
     * ensure pojo-agent is loaded
     *
     * @throws RequirePojoAgentException thrown when pojo-agent is not loaded
     * @since 1.1.0
     */
    public static void ensurePojoAgent() throws RequirePojoAgentException {
        PojoAgentInternal.ensurePojoAgent();
        //noinspection ResultOfMethodCallIgnored
        Version.stringifyVersion(Version.VERSION_INT); // ensure version is loaded
    }

    /**
     * get pojo-agent api version
     *
     * @return long value representing the api version
     * @see Version#stringifyVersion(long)
     * @since 1.1.3
     */
    public static long getApiVersion() {
        return Version.VERSION_INT;
    }

    /**
     * get pojo-agent agent version
     *
     * @return long value representing the agent version
     * @see Version#stringifyVersion(long)
     * @since 1.1.3
     */
    public static long getAgentVersion() {
        return PojoAgentInternal.getAgentVersion();
    }
}
