# Gradle Notes

Notes intended as feedback to Gradle devs about experience with new model plugins.

## Model Language Plugin Feedback

Sorry for yet another long post... but this is my model-specific feedback so far.

Overall, I like where the model approach will take Gradle. Particularly, since it highlights the key differentiating factor of Gradle. What's described in the user guide for the model is a very clean and fairly straightforward API. Unfortunately, it seems to get more complicated when looking at the application of it in the core language plugins. Largely, this is just due to the volatile nature of the model and the language plugins at this stage. However, I do want to reiterate that 3rd party plugins have many of the same use cases as core Gradle plugins. It is important to ensure that the features deemed necessary to implement a core language plugin are also made publicly available for 3rd party use(even if incubating).

Now for specific questions/feedback on practical usage.

## Model Visibility

It may be helpful to have some additional scopes in the model. There are 3 audiences where a model element may be useful:

End user or build script author
Other plugin authors
Author of a specific plugin (or family of plugins), be they core or 3rd party, with internal/private model elements.

With additional scopes, the model report could, by default, only show those relevant to the user. Flags could be passed to the model task to include additional scopes in the report. The goal here is to promote the relevance of the model report for inspecting the build by ensuring its filtered to the elements that matter to each audience.

There are already some elements hidden from the model report (such as ExtensionContainer), so it seems like this need is already acknowledged to some extent.

## Custom Rule Annotations

The core language plugins have frequent use of rules that use custom annotations (e.g. @LanguageType, @ComponentBinaries). Is this a mechanism that will stick around or will these be refactored to use standard rules? If they do stick around, will this mechanism be documented and available to 3rd party plugins, if similar needs arise?

## RulesSource Application

I couldn't find any documentation for the frequent usage of a package-protected inner Rules class being automatically applied. What are the rules for when a RulesSource will be applied? The user guide only covers that they can be applied directly like a Plugin<Project>.

Also, there are places where rules applied using the internal ModelRegistry (see example below from JvmComponentPlugin). I'm not following the reason this isn't just applied as a normal RuleSource plugin. Is this just a leftover from an earlier iteration or is this addressing some other limitation of the current model API?

modelRegistry.getRoot().applyToAllLinksTransitive(ModelType.of(ComponentSpec.class), JarBinaryRules.class);

## Composition/Extension of Software Model

First question (slightly unrelated) is whether components/binaries will be managed types in the future? If so, why not? I'm thinking the rationale will be helpful clarification for when managed/unmanaged are appropriate to use, in general.

The new java library user guide chapter makes some mentions of custom variants, but isn't very clear on how one would approach this. Some example variants in my plugin are:

Platforms for Clojure versions
Whether the JAR should include the source and/or AOT-compiled classes
Whether JAR is an "uberjar" (fatjar) including all dependencies

It appears that the component is the correct place for a user to configure the variants, though the binaries still need to reflect them. Is that accurate?

The end of the jvm library chapter indicates these should be subtypes of the binary. Would we need sub-interfaces for every combination of variants that might occur? This seems to become a composition nightmare if anyone ever wanted to combine more than two languages together. Alternatively, would it be possible just to register new variants on a type of component?

Either way, I haven't been able to find where the per-variant binaries get created. Is this actually in place now, or is it limited to the JavaPlatform variant?

Along with JVM Clojure, I plan to support ClojureScript. I would expect that it won't be inheriting from the whole JVM component/binary structure which seems to be where some features come from right now.

## Rough Corners

I don't expect these to be news to you, I just want to highlight the ones I ran into.

It's hard to tell which interfaces/classes are from older iterations and which still have a future, since each plugin uses different combinations. It would be helpful to have some type of annotation (maybe just @Deprecated) indicating those that are on their way out.
Dependency resolution being limited to other libraries. For now, I'll try the workaround that Play is using.
Platforms:
Right now the usage is kind of unclear. PlatformRequirements and PlatformResolvers are internal.
Seems like a lot to have to implement, when we mostly just need a map from a platform name to an implementation of a platform.
ToolChains
ToolChainRegistry (which is public) helps with making them available to tasks.
ToolProviders are internal. It's not clear how other plugins should approach this. ToolProvider or roll your own approach?
LanguageTransform, in general, but primarily the fact that it's internal.
Using LanguageTransform with new component/binary types requires overriding a method on BaseComponentSpec specified by an internal interface (getInputTypes).

## Remaining

- Other artifacts: source, documentation
- Testing integration
