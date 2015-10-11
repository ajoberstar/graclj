package org.graclj.platform.common.internal;

import org.graclj.platform.common.ClojurePlatform;

public class DefaultClojurePlatform implements ClojurePlatform {
    private final String version;

    public DefaultClojurePlatform(int majorVersion, int minorVersion) {
        this.version = String.format("%s.%s", majorVersion, minorVersion);
    }

    public String getDisplayName() {
        return String.format("Clojure %s", version);
    }

    public String getName() {
        return String.format("cljc%s", version);
    }

    public String getClojureVersion() {
        return version;
    }
}
