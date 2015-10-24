package org.graclj.platform.jvm;

import org.gradle.jvm.JarBinarySpec;
import org.gradle.model.Managed;
import org.gradle.platform.base.internal.BinarySpecInternal;

// TODO stop using internals
@Managed
public interface ClojureAotJarBinarySpec extends JarBinarySpec, BinarySpecInternal {}
