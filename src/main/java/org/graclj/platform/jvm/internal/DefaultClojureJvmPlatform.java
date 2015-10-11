package org.graclj.platform.jvm.internal;

import org.graclj.platform.common.ClojurePlatform;
import org.graclj.platform.common.internal.DefaultClojurePlatform;
import org.graclj.platform.jvm.ClojureJvmPlatform;
import org.gradle.jvm.platform.JavaPlatform;

public class DefaultClojureJvmPlatform implements ClojureJvmPlatform {
    private final ClojurePlatform clojurePlatform;
    private final JavaPlatform javaPlatform;
    private final String version;

    public DefaultClojureJvmPlatform(int majorVersion, int minorVersion, int patchVersion, String preReleaseVersion, JavaPlatform javaPlatform) {
        this.clojurePlatform = new DefaultClojurePlatform(majorVersion, minorVersion);
        this.javaPlatform = javaPlatform;
        if (preReleaseVersion == null) {
            this.version = String.format("%s.%s.%s", majorVersion, minorVersion, patchVersion);
        } else {
            this.version = String.format("%s.%s.%s-%s", majorVersion, minorVersion, patchVersion, preReleaseVersion);
        }
    }

    @Override
    public String getDisplayName() {
        return String.format("Clojure JVM %s", version);
    }

    @Override
    public String getName() {
        return String.format("clojure%s", version);
    }

    @Override
    public ClojurePlatform getClojurePlatform() {
        return clojurePlatform;
    }

    @Override
    public JavaPlatform getJavaPlatform() {
        return javaPlatform;
    }

    @Override
    public String getClojureJvmVersion() {
        return version;
    }


}
