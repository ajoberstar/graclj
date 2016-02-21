package org.graclj.platform.clj;

import org.gradle.jvm.JvmComponentSpec;
import org.gradle.platform.base.GeneralComponentSpec;

public interface ClojureGeneralComponentSpec extends GeneralComponentSpec, JvmComponentSpec {
    boolean isAot();
    void setAot(boolean aot);
}
