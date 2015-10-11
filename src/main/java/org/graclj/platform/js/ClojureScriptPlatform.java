package org.graclj.platform.js;

import org.graclj.platform.GracljPlatform;
import org.graclj.platform.common.ClojurePlatform;

public interface ClojureScriptPlatform extends GracljPlatform {
    ClojurePlatform getClojurePlatform();
}
