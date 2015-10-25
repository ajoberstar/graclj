package org.graclj.platform.jvm.toolchain;

import org.gradle.api.file.FileCollection;
import org.gradle.platform.base.ToolChain;

public interface ClojureJvmToolChain extends ToolChain {
    FileCollection getCompiler();
}
