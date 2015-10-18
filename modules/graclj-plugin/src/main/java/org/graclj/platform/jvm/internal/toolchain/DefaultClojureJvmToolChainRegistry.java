package org.graclj.platform.jvm.internal.toolchain;

import org.graclj.internal.GracljInternal;
import org.graclj.platform.jvm.ClojureJvmPlatform;
import org.graclj.platform.jvm.toolchain.ClojureJvmToolChain;
import org.graclj.platform.jvm.toolchain.ClojureJvmToolChainRegistry;

public class DefaultClojureJvmToolChainRegistry implements ClojureJvmToolChainRegistry {
    private final GracljInternal dependencies;

    public DefaultClojureJvmToolChainRegistry(GracljInternal dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public ClojureJvmToolChain getForPlatform(ClojureJvmPlatform targetPlatform) {
        return new DownloadingClojureJvmToolChain(targetPlatform, dependencies);
    }
}
