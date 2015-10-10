package org.graclj.platform.js;

import org.graclj.platform.common.ClojurePlatform;
import org.gradle.platform.base.Platform;

public interface ClojureScriptPlatform extends Platform {
    ClojurePlatform getClojurePlatform();

    String getClojureScriptVersion();
}
