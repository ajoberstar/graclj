package org.graclj.platform.jvm;

import org.graclj.platform.common.ClojureApplicationSpec;

public interface ClojureJvmApplicationSpec extends ClojureApplicationSpec, ClojureJvmComponentSpec {
    String getMainNamespace();

    void setMainNamespace(String namespace);
}
