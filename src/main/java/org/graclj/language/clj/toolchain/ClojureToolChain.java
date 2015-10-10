package org.graclj.language.clj.toolchain;

import org.gradle.platform.base.ToolChain;
import org.gradle.jvm.Classpath;
import org.graclj.language.clj.ClojurePlatform;

public interface ClojureToolChain extends ToolChain {
  Classpath getClojure(ClojurePlatform platform);
}
