# Implementation Notes

## Graclj

### General



### Cljc

- How should this be abstracted?
    - A single project may use cljc for both clj and cljs
    - That implies needing language level support, not just an include statement.

### Clojure

- Should the AOT setting be on the source or the binary?
    - Seems like the binary is a good place for it so you could have one source set with two variants (AOT, non-AOT).
    - Problems is how does that compose with the existing JVM binaries?
- Should uber-jars be a new type of binary?
    - If so, how to compose that with the other binary features?

### Clojurescript

- Is Classpath an accurate term in CLJS?
    - How is the collection of CLJS dependencies referenced?

#### JavaScript

- Will I need to implement a JavaScript platform?
    - How will Google Closure play into that?
- Should there be an equivalant of JvmByteCode?
       - What about minification (i.e. cljs optimization options)?
- Should there be a JvmResource equivalent?

### ClojureCLR

- Is this worth targeting?

## Gradle

### Model

### Platform

- Do I need a platform for Clojure?
    - Would people target different versions of Clojure from their build?
    - Same question goes for ClojureScript
- Can I just put the platforms on PlatformContainer?
    - Does that cause a problem with versioned platforms?

### Language

### Component

### ToolChain

- Is this the right place to have the clj/cljs compilers come from?
    - Based on language-scala plugin, yes.

### Play

Seems like a more complete example of how this could work.

- Use my own binary types?
    - Could include an AOT JAR and a Source-Only JAR?
    - BinaryTypeBuilder (not internal, yay!)
    - Would these extend JvmBinarySpec still?
- Might want my own component types too?
    - Could have a ClojureLibrarySpec
        - Include binaries for both AOT and source (maybe AOT w/ source too)
    - ClojureScriptLibrarySpec
    - Mixed clojure support somehow?
    - ClojureApplicationSpec
        - Use uberjar?
    - Look into what other types of projects Lein supports.
- @ComponentBinaries
- Play has its own wrapper around configurations. Registers its artifacts there. May also be used for deps.
- Separate configs for:
    - compile
    - runtime (?)
    - dev (?)
    - docs (?)
    - test (?)
