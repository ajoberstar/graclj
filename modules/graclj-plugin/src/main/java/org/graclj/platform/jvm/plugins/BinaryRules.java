package org.graclj.platform.jvm.plugins;

import org.graclj.internal.DependencyExtension;
import org.graclj.platform.jvm.ClojureJvmBinarySpec;
import org.graclj.platform.jvm.ClojureJvmLibrarySpec;
import org.gradle.model.Defaults;
import org.gradle.model.RuleSource;

import java.io.File;

public class BinaryRules extends RuleSource {
    @Defaults
    void binaryDefaults(ClojureJvmLibrarySpec component, DependencyExtension dependencies) {
        System.out.println("binaryDefaults");
        File binariesDir = new File(dependencies.getBuildDir(), "binaries");
        File classesDir = new File(dependencies.getBuildDir(), "classes");
        component.getBinaries().withType(ClojureJvmBinarySpec.class, binary -> {
            binary.setJarFile(binariesDir.toPath().resolve(binary.getName() + ".jar").toFile());
            binary.setClassesDir(classesDir.toPath().resolve(binary.getName()).toFile());
        });
    }
}
