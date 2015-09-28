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

### Language

### Component

### ToolChain

- Is this the right place to have the clj/cljs compilers come from?
    - Based on language-scala plugin, yes.
