package io.github.gaming32.dataexport;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExportUtil {
    @SuppressWarnings("unchecked")
    public static <T> void collectConstants(
        Class<T> clazz,
        ExceptionBiConsumer<String, ? super T> action
    ) throws Exception {
        for (final Field field : clazz.getDeclaredFields()) {
            final int mods = field.getModifiers();
            if (!Modifier.isPublic(mods) || !Modifier.isStatic(mods) || !Modifier.isFinal(mods)) continue;
            if (!clazz.isAssignableFrom(field.getType())) continue;
            action.accept(field.getName().toLowerCase(Locale.ROOT), (T)field.get(null));
        }
    }

    public static <T> Map<T, String> collectConstantsToMap(Class<T> clazz) throws Exception {
        final Map<T, String> result = new HashMap<>();
        collectConstants(clazz, (name, value) -> result.put(value, name));
        return result;
    }
}
