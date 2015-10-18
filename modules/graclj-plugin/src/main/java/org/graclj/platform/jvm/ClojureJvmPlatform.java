package org.graclj.platform.jvm;

import org.graclj.platform.GracljPlatform;
import org.graclj.platform.common.ClojurePlatform;

public interface ClojureJvmPlatform extends GracljPlatform {
    ClojurePlatform getClojurePlatform();
}
