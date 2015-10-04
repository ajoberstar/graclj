# graclj
Clojure plugin for Gradle

## Background

Gradle is continuing to evolve its APIs, with the major new change being the [model space](https://docs.gradle.org/nightly/userguide/new_model.html).
This will be an entirely new set of APIs geared towards ensuring that interactions and dependencies between configuration are appropriately
captured. This will both making it easier to reason about and allow Gradle to introspect the model and its relations to benefits such as improved
performance.

Gradle has had very low adoption in the Clojure community (2% as of the [2014 State of Clojure](https://cognitect.wufoo.com/reports/state-of-clojure-2014-results/).
[Clojuresque](https://bitbucket.org/clojuresque/) does exist, but has become fairly stagnant recently.

The goal of Graclj is to provide support for Clojure and Clojurescript builds within Gradle, targeting the new model space. Ideally,
this will provide feature parity with [Lein](http://leiningen.org/) and [Boot](http://boot-clj.com/) and entice some of the Clojure
community over to Gradle.

It is a goal to determine how this work can be ported back into Clojuresque or shared in some way. In the initial stages, this
will not be attempted in order to preserve the flexibility of Graclj.

See [the thread](https://groups.google.com/forum/#!topic/clojuresque/1j24yiOGa30) on the Clojuresque mailing list for
more detail.

## Roadmap

* [0.1.0](https://github.com/graclj/graclj/milestones/0.1.0) will target Clojure builds at a very basic level, primarily just covering compilation and packaging.
* [0.2.0](https://github.com/graclj/graclj/milestones/0.2.0) will target Clojurescript builds with the same goal. This is not intended to reconcile the relationship between the two.
* [0.3.0](https://github.com/graclj/graclj/milestones/0.3.0) will try to make the combined Clojure/Clojurescript space mesh together, including `cljc` support.
* ...
* [1.0.0](https://github.com/graclj/graclj/milestones/1.0.0) ... many great things ...
