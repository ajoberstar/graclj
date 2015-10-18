package org.graclj.platform.jvm.internal;

import org.graclj.platform.jvm.ClojureJvmPlatform;
import org.gradle.platform.base.internal.PlatformRequirement;
import org.gradle.platform.base.internal.PlatformResolver;

// TODO: Stop using internals.
public class ClojureJvmPlatformResolver implements PlatformResolver<ClojureJvmPlatform> {
    @Override
    public Class<ClojureJvmPlatform> getType() {
        return ClojureJvmPlatform.class;
    }

    @Override
    public ClojureJvmPlatform resolve(PlatformRequirement platformRequirement) {
        return DefaultClojureJvmPlatform
            .parse(platformRequirement.getPlatformName())
            .orElse(null);
    }
}
