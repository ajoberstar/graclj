package org.graclj.internal.plugins;

import org.graclj.internal.GracljInternal;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.model.Model;
import org.gradle.model.RuleSource;

public class GracljInternalRules extends RuleSource {
    @Model
    public GracljInternal gracljInternalDependencies(ExtensionContainer extensions) {
        return extensions.getByType(GracljInternal.class);
    }
}
