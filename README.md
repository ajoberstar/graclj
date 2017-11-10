# graclj

[![Build Status](https://travis-ci.org/graclj/graclj.svg?branch=master)](https://travis-ci.org/graclj/graclj)
[![Quality Gate](https://sonarqube.ajoberstar.com/api/badges/gate?key=org.graclj:graclj)](https://sonarqube.ajoberstar.com/dashboard/index/org.graclj:graclj)
[![Join the chat at https://gitter.im/graclj/graclj](https://badges.gitter.im/graclj/graclj.svg)](https://gitter.im/graclj/graclj?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

**NOTE: Use [gradle-clojure](https://github.com/gradle-clojure/gradle-clojure) instead. This is no longer maintained.**

Clojure plugin for Gradle's Software Model

Just want to see how to use it? Try [learning-graclj](https://github.com/graclj/learning-graclj/tree/learning-0.1.0)

**DISCLAIMERS:**

1. Graclj should not be considered stable until 1.0.0. Until then, minor versions (e.g. 0.1.0 to 0.2.0) will contain breaking changes.
2. Currently Graclj only works on nightly versions of Gradle. See the [releases](https://github.com/graclj/graclj/releases) to find the appropriate Gradle version.

## Goals

- Provide a Gradle plugin for Clojure that feels native to Gradle and provides the features the Clojure community has
  come to expect from [Leiningen](http://leiningen.org/) and [Boot](http://boot-clj.com/).
- Implement using the new [model space](https://docs.gradle.org/nightly/userguide/new_model.html) in Gradle.
- Determine if/how any work here can be ported or shared with Clojuresque. In the initial stages, this will not be
  attempted in order to preserve the flexibility of Graclj.

## Background

Gradle has had very low adoption in the Clojure community: 2% as of the [2014 State of Clojure](https://cognitect.wufoo.com/reports/state-of-clojure-2014-results/) and [2015 State of Clojure](https://www.surveymonkey.com/results/SM-QKBJ2C5J/).
Clojure support is currently provided by [Clojuresque](https://bitbucket.org/clojuresque/), however it's development has stagnated recently.

Additionally, Gradle continues to evolve with the "foundation of Gradle 3.0" being built on the model space. Which promotes
Gradle's long-standing goal of modelling the build space, while trying to make the interactions between configuration
from various plugins and build scripts more understandable.

See [the original thread](https://groups.google.com/forum/#!topic/clojuresque/1j24yiOGa30) on the Clojuresque mailing list for
more detail.

## Current Features

### Clojure

- Packaging into JARs
- AOT compilation
- clojure.test execution
- Publishing to any repo supported by Gradle (including Clojars)

### Clojurescript

*Coming soon...*

## Roadmap

- [0.2.0](https://github.com/graclj/graclj/milestones/0.2.0) will enhance Clojure support with things like uberjar and REPL support
- [0.3.0](https://github.com/graclj/graclj/milestones/0.3.0) will target ClojureScript builds with the same goal. This is not intended to reconcile the relationship between the two.
- ...
- [1.0.0](https://github.com/graclj/graclj/milestones/1.0.0) ... many more great things ...
