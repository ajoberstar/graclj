package org.graclj.platform.jvm;

import org.graclj.platform.common.ClojurePlatform;
import org.gradle.jvm.platform.JavaPlatform;
import org.gradle.platform.base.Platform;

public interface ClojureJvmPlatform extends Platform {
    ClojurePlatform getClojurePlatform();

    JavaPlatform getJavaPlatform();

    String getClojureJvmVersion();
}
