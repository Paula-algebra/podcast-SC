package hr.algebra.podcast.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.Set;

public class WhitelistObjectInputStream extends ObjectInputStream {

    private static final Set<String> ALLOWED_CLASSES = Set.of(
            "hr.algebra.podcast.entity.User",
            "hr.algebra.podcast.enums.Role",
            "java.lang.Long",
            "java.lang.Number",
            "java.lang.String",
            "java.time.LocalDateTime",
            "java.time.Ser",
            "java.lang.Enum"
    );

    public WhitelistObjectInputStream(InputStream inputStream)
            throws IOException {
        super(inputStream);
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc)
            throws IOException, ClassNotFoundException {

        String className = desc.getName();

        if (!ALLOWED_CLASSES.contains(className)) {
            throw new InvalidClassException(
                    "not an explicitly whitelisted class", className
            );
        }

        return super.resolveClass(desc);
    }
}