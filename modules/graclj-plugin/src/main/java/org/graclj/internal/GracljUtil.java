package org.graclj.internal;

import org.graclj.internal.GracljUtil;

import java.io.InputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;

public final class GracljUtil {
    private static final String gracljVersion;

    static {
        Properties props = new Properties();
        try (InputStream stream = GracljUtil.class.getClassLoader().getResourceAsStream("/org/graclj/version.properties")) {
            props.load(stream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        gracljVersion = props.get("version").toString();
    }

    private GracljUtil() {
        // hide constructor
    }

    public static String getGracljVersion() {
        return gracljVersion;
    }
}
