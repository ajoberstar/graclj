package org.graclj.platform.jvm.plugins;

import org.graclj.internal.GracljInternal;
import org.graclj.platform.jvm.internal.ClojureJvmPlatformResolver;
import org.graclj.platform.jvm.internal.toolchain.DefaultClojureJvmToolChainRegistry;
import org.graclj.platform.jvm.toolchain.ClojureJvmToolChainRegistry;
import org.gradle.model.Model;
import org.gradle.model.Mutate;
import org.gradle.model.RuleSource;
import org.gradle.platform.base.internal.PlatformResolvers;

@SuppressWarnings("unused")
public class ClojureJvmComponentRules extends RuleSource {
    @Model
    public ClojureJvmToolChainRegistry clojureToolChain(GracljInternal internals) {
        return new DefaultClojureJvmToolChainRegistry(internals);
    }

    @Mutate
    // TODO: Stop using internals.
    public void registerPlatformResolver(PlatformResolvers platformResolvers) {
        platformResolvers.register(new ClojureJvmPlatformResolver());
    }
}
