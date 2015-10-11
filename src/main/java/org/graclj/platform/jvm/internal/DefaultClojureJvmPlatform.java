package org.graclj.platform.jvm.internal;

import org.graclj.platform.common.ClojurePlatform;
import org.graclj.platform.common.internal.DefaultClojurePlatform;
import org.graclj.platform.jvm.ClojureJvmPlatform;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultClojureJvmPlatform implements ClojureJvmPlatform {
    private static final Pattern NAME_PATTERN = Pattern.compile("^clj(\\d+)\\.(\\d+)\\.(\\d+)(-.+)?$");
    private final ClojurePlatform clojurePlatform;
    private final String version;

    public DefaultClojureJvmPlatform(int majorVersion, int minorVersion, int patchVersion, String preReleaseVersion) {
        this.clojurePlatform = new DefaultClojurePlatform(majorVersion, minorVersion);
        if (preReleaseVersion == null) {
            this.version = String.format("%s.%s.%s", majorVersion, minorVersion, patchVersion);
        } else {
            this.version = String.format("%s.%s.%s-%s", majorVersion, minorVersion, patchVersion, preReleaseVersion);
        }
    }

    @Override
    public String getDisplayName() {
        return String.format("Clojure JVM %s", version);
    }

    @Override
    public String getName() {
        return String.format("clj%s", version);
    }

    @Override
    public ClojurePlatform getClojurePlatform() {
        return clojurePlatform;
    }

    @Override
    public String getVersion() {
        return version;
    }

    public static Optional<DefaultClojureJvmPlatform> parse(String name) {
        Matcher matcher = NAME_PATTERN.matcher(name);
        if (matcher.find()) {
            return Optional.of(new DefaultClojureJvmPlatform(
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)),
                matcher.group(4)
            ));
        } else {
            return Optional.empty();
        }
    }
}
