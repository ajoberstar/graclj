package org.graclj.platform.jvm.internal;

import org.graclj.platform.jvm.ClojureJarBinarySpec;
import org.gradle.jvm.internal.DefaultJarBinarySpec;

public class DefaultClojureJarBinarySpec extends DefaultJarBinarySpec implements ClojureJarBinarySpec {
    private boolean aot = false;

    @Override
    public String getAot() {
        return aot ? "aot" : "";
    }

    @Override
    public void setAot(boolean aot) {
        this.aot = aot;
    }
}
