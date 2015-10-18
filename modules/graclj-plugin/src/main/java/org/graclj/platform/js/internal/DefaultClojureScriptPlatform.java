package org.graclj.platform.js.internal;

import org.graclj.platform.common.ClojurePlatform;
import org.graclj.platform.common.internal.DefaultClojurePlatform;
import org.graclj.platform.js.ClojureScriptPlatform;

public class DefaultClojureScriptPlatform implements ClojureScriptPlatform {
    private final ClojurePlatform clojurePlatform;
    private final String version;

    public DefaultClojureScriptPlatform(int majorVersion, int minorVersion, int patchVersion, String preReleaseVersion) {
        this.clojurePlatform = new DefaultClojurePlatform(majorVersion, minorVersion);
        if (preReleaseVersion == null) {
            this.version = String.format("%s.%s.%s", majorVersion, minorVersion, patchVersion);
        } else {
            this.version = String.format("%s.%s.%s-%s", majorVersion, minorVersion, patchVersion, preReleaseVersion);
        }
    }

    @Override
    public String getDisplayName() {
        return String.format("ClojureScript %s", version);
    }

    @Override
    public String getName() {
        return String.format("cljs%s", version);
    }

    @Override
    public ClojurePlatform getClojurePlatform() {
        return clojurePlatform;
    }

    @Override
    public String getVersion() {
        return version;
    }


}
