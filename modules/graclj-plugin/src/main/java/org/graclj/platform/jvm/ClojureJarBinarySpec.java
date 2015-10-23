package org.graclj.platform.jvm;

import org.gradle.jvm.JarBinarySpec;
import org.gradle.platform.base.Variant;

public interface ClojureJarBinarySpec extends JarBinarySpec {
    @Variant
    String getAot();

    void setAot(boolean aot);
}
