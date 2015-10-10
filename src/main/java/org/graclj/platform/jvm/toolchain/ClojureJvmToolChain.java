package org.graclj.platform.jvm.toolchain;

import org.gradle.jvm.Classpath;
import org.gradle.platform.base.ToolChain;

public interface ClojureJvmToolChain extends ToolChain {
    Classpath getCompiler();
}
