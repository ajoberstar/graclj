# Gradle Notes

Notes intended as feedback to Gradle devs about experience with new model plugins.

Sorry for the wall of text...

## General

My adventures so far on the new Clojure plugin have refreshed some pain points I've
frequently had with Gradle as a plugin author. Some of my examples here are specific
to the model plugins, which I understand are very much a work-in-progress, but these
points have been true in the old-style plugins for years. This is mainly apparent when
trying to build a plugin that you want to look and feel like a core plugin (e.g.
language plugins).

- Poor documentation (at least for plugin authors). In short, we're forced to read the
  how (almost all of it), to understand the what or the why.
  - User guide docs for plugin authoring is only geared towards simple use cases, such
    as companies applying common configuration.
  - Plugin-oriented APIs and base plugins have no documented contract. You have to
    read the source to understand what they provide.
  - The source typically has no comments, leaving the reader to discover any
    context by reading all related source (including internals).
- Way too much magic. I'm still hoping the model drives improvement here, but there's
  little to no way to understand where (or when, from a lifecycle standpoint) features
  come from.
  - As one recent example, it took an entire afternoon of digging around to find out how
    the project's extension container is visible in the model rules, but other containers
    (configurations, dependencies) are not.
- Overuse of internals in core plugins. Not making core plugins live by the public APIs
  makes it effectively impossible to make a 3rd party plugin with a first-class experience
  without using or duplicating the internals.
  - Much of this is due to core abstractions being hidden in internals. Many benefits
    highlighted in the recent release notes are inaccessible (via public APIs) to 3rd
    party plugins.
  - Some examples, with use cases for 3rd party plugins:
    - Dependency resolution infrastructure -- supporting new repos or dependency types,
      e.g. bower
    - (Compiler)Daemon infrastructure -- any forked process that could be reused between
      builds
    - Compiler infrastructure -- language plugins

I'd like to see Gradle get to the point where it isn't just a polyglot build system, but
one that is seriously spoken of as a competitor within each language's community (e.g.
grunt/gulp vs Gradle for JS or boot/lein vs Gradle for Clojure). Achieving this is largely
predicated on the community bearing that burden. With stronger public APIs, improvements
you make to the internals will benefit everyone instead of just the core plugins.

## Model

- It might be nice if you could hide the internal model elements, so that reports such as the model task don't show them.

## Platform

## Language

- Why is @LanguageType it's own annotation rather than a standard model rule annotation?
- LanguageTransform seems pretty important for any language plugin, but it's still internal?
    - Can this be moved to public, but incubating?

## Component

- What is the purpose of Artifact relative to the Component subclasses (Library, Application)?

## JVM

- How do you plan to compose variants on binaries (or in general)?
    - Best example is binaries that reflect multiple platform variants
    - e.g. Scala will have a Scala platform and a Java platform variant (I see a comment reflecting this in master)
- Why is EmptClasspath noted as temporary? Seems like we need some
- Need third-party dependency support.

## Clojure

## Clojurescript
