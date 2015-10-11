package org.graclj.platform;

import org.gradle.platform.base.Platform;

public interface GracljPlatform extends Platform {
    String getVersion();
}
