package org.graclj.test.clj;

import org.gradle.jvm.test.JvmTestSuiteBinarySpec;
import org.gradle.model.Managed;

@Managed
public interface ClojureTestSuiteBinarySpec extends JvmTestSuiteBinarySpec {

    @Override
    ClojureTestSuiteSpec getTestSuite();

    String getClojureVersion();

    void setClojureVersion(String version);
}
