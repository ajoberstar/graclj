# Gradle Notes

Notes intended as feedback to Gradle devs about experience with new model plugins.

## Missing Pieces

### Needed

- Public views (like the internal ones) so I can extend JarBinarySpec, etc.
- Dependency on Gradle API from jvm-component/java-lang

## Maybe

- Public way to create (or use in @Managed type, preferably) Classpath
- Public way to inherit (or use in @Managed type, preferably) DependentSourceSet

## Complications

- Binary naming schemes seem like they could be done behind the scenes (if not, should have a public way to get a name consistent with core plugins)
- JvmTestSuite plugins assume JUnit or TestNG
