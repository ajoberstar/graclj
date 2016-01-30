# Gradle Notes

Notes intended as feedback to Gradle devs about experience with new model plugins.

## Missing Pieces

- Public views (like the internal ones) so I can extend JarBinarySpec, etc.
- Public way to create (or use in @Managed type, preferably) Classpath
- Public way to inherit (or use in @Managed type, preferably) DependentSourceSet
- Dependency on Gradle API from jvm-component/java-lang
-
