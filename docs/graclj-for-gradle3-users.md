# Graclj for Gradle 3 Users

See full samples under [src/samples/src/projects](../samples/src/projects).

## Plugins

The plugin is published in JCenter (not yet on the Gradle Plugin Portal).

```groovy
buildscript {
    repositories { jcenter() }
    dependencies { classpath 'org.graclj:graclj-plugin:0.1.0-rc.1' }
}

// adds support for the Clojure language itself
apply plugin: 'org.graclj.clojure-lang'
// adds support for clojure.test tests
apply plugin: 'org.graclj.clojure-test-suite'
```

## Components

The `org.graclj.clojure-lang` plugin will add a Clojure source set to any `JvmComponentSpec` (e.g. `JvmLibrarySpec`).

```groovy
repositories {
    mavenCentral()
    maven {
        name = 'clojars'
        url = 'https://clojars.org/repo'
    }
}

model {
    components {
        main(JvmLibrarySpec) {
            dependencies {
                module 'org.clojure:clojure:1.8.0'
                module 'com.stuartsierra:component:0.3.1'
            }
        }
    }
}
```

In this case your code would go under `src/main/clojure`.

## Test Suites

Graclj leverages Gradle's JUnit support to execute `clojure.test` tests through a JUnit runner. You configure the test
suite like normal and it detects any tests in your suite's source set that use `clojure.test`.

```groovy
model {
    testSuites {
        unit(JUnitTestSuiteSpec) {
            jUnitVersion '4.12'
        }
    }
}
```

In this case your tests would go under `src/unit/clojure`.
