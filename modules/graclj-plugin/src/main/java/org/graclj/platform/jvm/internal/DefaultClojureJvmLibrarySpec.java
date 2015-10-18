package org.graclj.platform.jvm.internal;

import org.graclj.platform.jvm.ClojureJvmLibrarySpec;
import org.graclj.platform.jvm.ClojureJvmPlatform;
import org.gradle.platform.base.component.BaseComponentSpec;

public class DefaultClojureJvmLibrarySpec extends BaseComponentSpec implements ClojureJvmLibrarySpec {
    private ClojureJvmPlatform platform;

    @Override
    public ClojureJvmPlatform getTargetPlatform() {
        return platform;
    }

    @Override
    public void targetPlatform(String targetPlatform) {
        this.platform = DefaultClojureJvmPlatform.parse(targetPlatform)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Clojure platform: " + targetPlatform));
    }
}
