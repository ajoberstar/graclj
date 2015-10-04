# Gradle Notes

Notes intended as feedback to Gradle devs about experience with new model plugins.

## General

- Lack of comments in the vast majority of the code makes it very difficult to understand.
    - There's no way to get any context for how or why certain classes/interfaces are used
      without literally reading all of the code.
    - Pervasive use of empty interfaces that extend other empty interfaces is also confusing.
      Hard to tell what those are meant to provide (if anything) for my code.
- Is there any reason (besides backwards-compatibility) that the name "main" is still a special-case?
    - For example, compileJava would be compileMainJava without specializing "main".

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

- Using "resources" as the language name sames a little broad. How do other platforms with similar
  needs name their equivalents? e.g. HTML files, images, etc. for a JS-pipeline.
- How do you plan to compose variants on binaries (or in general)?
    - Best example is binaries that reflect multiple platform variants
    - e.g. Scala will have a Scala platform and a Java platform variant (I see a comment reflecting this in master)
- Why is EmptClasspath noted as temporary? Seems like we need some 
- Is it true that you can't add declare/resolve any dependencies that aren't just other Library components within the same multi-project build?

## Clojure

## Clojurescript
