package org.graclj.test.jvm;

import org.gradle.jvm.test.JvmTestSuiteBinarySpec;
import org.gradle.model.Managed;

@Managed
public interface ClojureTestSuiteBinarySpec extends JvmTestSuiteBinarySpec {

    @Override
    ClojureTestSuiteSpec getTestSuite();

    String getClojureVersion();

    void setClojureVersion(String version);
}
