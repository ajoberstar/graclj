# Gradle Notes

Notes intended as feedback to Gradle devs about experience with new model plugins.

## Missing Pieces

### Needed

- Dependency on Gradle API from jvm-component/java-lang
- Public views (like the internal ones) so I can extend JarBinarySpec, etc.
- Public way to inherit (or use in @Managed type, preferably) DependentSourceSet
- Public way to inherit (or use in @Managed type, preferably) DependentSourceSet
- Public way to resolve dependencies (maybe as a `Classpath`)


## Maybe

- Public way to create (or use in @Managed type, preferably) Classpath


## Complications

- JvmTestSuite plugins assume JUnit or TestNG
