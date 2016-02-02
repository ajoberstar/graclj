package org.graclj.test.jvm;

import org.gradle.jvm.test.JvmTestSuiteSpec;
import org.gradle.model.Managed;

@Managed
public interface ClojureTestSuiteSpec extends JvmTestSuiteSpec {
    String getClojureVersion();

    void setClojureVersion(String version);
}
