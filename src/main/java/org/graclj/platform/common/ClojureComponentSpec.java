package org.graclj.platform.common;

import org.gradle.platform.base.Platform;
import org.gradle.platform.base.PlatformAwareComponentSpec;

public interface ClojureComponentSpec extends PlatformAwareComponentSpec {
    Platform getTargetPlatform();
}
