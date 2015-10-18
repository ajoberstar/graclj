package org.graclj.platform.jvm.internal.toolchain;

import org.graclj.internal.DependencyExtension;
import org.graclj.platform.jvm.ClojureJvmPlatform;
import org.graclj.platform.jvm.toolchain.ClojureJvmToolChain;
import org.graclj.platform.jvm.toolchain.ClojureJvmToolChainRegistry;

public class DefaultClojureJvmToolChainRegistry implements ClojureJvmToolChainRegistry {
    private final DependencyExtension dependencies;

    public DefaultClojureJvmToolChainRegistry(DependencyExtension dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public ClojureJvmToolChain getForPlatform(ClojureJvmPlatform targetPlatform) {
        return new DownloadingClojureJvmToolChain(targetPlatform, dependencies);
    }
}
