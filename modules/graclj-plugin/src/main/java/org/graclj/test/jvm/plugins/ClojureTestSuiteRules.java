package org.graclj.test.jvm.plugins;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import org.graclj.test.jvm.ClojureTestSuiteBinarySpec;
import org.graclj.test.jvm.ClojureTestSuiteSpec;
import org.gradle.jvm.JvmBinarySpec;
import org.gradle.jvm.JvmComponentSpec;
import org.gradle.jvm.test.JvmTestSuiteSpec;
import org.gradle.jvm.test.internal.JvmTestSuiteRules;
import org.gradle.jvm.toolchain.JavaToolChainRegistry;
import org.gradle.model.*;
import org.gradle.platform.base.*;
import org.gradle.platform.base.internal.*;

public class ClojureTestSuiteRules extends RuleSource {
    @ComponentType
    public void register(ComponentTypeBuilder<ClojureTestSuiteSpec> builder) {
    }

    @BinaryType
    public void registerBinary(BinaryTypeBuilder<ClojureTestSuiteBinarySpec> builder) {
    }

    @ComponentBinaries
    public static void createBinaries(ModelMap<BinarySpec> binaries, ClojureTestSuiteSpec testSuite) {
        // TODO stop using internals
        BinaryNamingScheme namingScheme = DefaultBinaryNamingScheme.component(testSuite.getName())
            .withBinaryType("binary")
            .withRole("assembly", true)
            .withVariantDimension(platform, selectedPlatforms);


        testSuite.getTestedComponent().getBinaries().withType(BinarySpecInternal.class, testedBinary -> {
            BinaryNamingScheme namingScheme = DefaultBinaryNamingScheme.component(testSuite.getName())
                .withBinaryType("binary")
                .withRole("assembly", true)
                .withVariantDimension(testedBinary.getProjectScopedName());

            binaries.create(namingScheme.getBinaryName(), ClojureTestSuiteBinarySpec.class, spec -> {
                spec.set
           }) ;
        });
    }

//    @Defaults
//    void defaultSuiteClojureVersion(@Each ClojureTestSuiteSpec testSuite) {
//        JvmComponentSpec component = testSuite.getTestedComponent();
//
//    }

    @Validate
    void validateSuiteClojureVersion(@Each ClojureTestSuiteSpec testSuite) {
        if (testSuite.getClojureVersion() == null) {
            throw new InvalidModelException(String.format("Test suite '%s' doesn't declare a Clojure version. Please specify it with `clojureVersion '1.8'` for example.", testSuite.getName()));
        }
    }

    @Defaults
    void defaultBinaryClojureVersion(@Each ClojureTestSuiteBinarySpec testSuiteBinary) {
        testSuiteBinary.setClojureVersion(testSuiteBinary.getTestSuite().getClojureVersion());
    }

    @Validate
    void validateBinaryClojureVersion(@Each ClojureTestSuiteBinarySpec testSuiteBinary) {
        if (testSuiteBinary.getClojureVersion() == null) {
            throw new InvalidModelException(String.format("Test suite binary '%s' doesn't declare a Clojure version. Please specify it with `clojureVersion '1.8'` for example.", testSuiteBinary.getName()));
        }
    }

    @Finalize
    void addBinaryClojureDependency(@Each ClojureTestSuiteBinarySpec testSuiteBinary) {
        testSuiteBinary.getDependencies().add(new DefaultModuleDependencySpec("org.clojure", "clojure", testSuiteBinary.getClojureVersion()));
    }
}
