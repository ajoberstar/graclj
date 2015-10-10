package org.graclj.platform.common;

import org.gradle.platform.base.Platform;

public interface ClojurePlatform extends Platform {
    String getClojureVersion();
}
