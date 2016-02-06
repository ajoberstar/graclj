package org.graclj.language.clj;

import org.gradle.jvm.JarBinarySpec;
import org.gradle.jvm.internal.JarBinarySpecInternal;
import org.gradle.jvm.internal.WithJvmAssembly;
import org.gradle.model.Managed;
import org.gradle.platform.base.internal.BinarySpecInternal;

@Managed
public interface ClojureAotJarBinarySpec extends JarBinarySpecInternal {
}
