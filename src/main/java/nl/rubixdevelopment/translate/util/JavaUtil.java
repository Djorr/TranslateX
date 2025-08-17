package nl.rubixdevelopment.translate.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PingLagger on 25/07/2024
 * @project TranslateX
 */
public class JavaUtil {

    public static <E> List<E> createList(Object object, Class<E> type, boolean ignoreNulls) {
        List<E> result = new ArrayList<>();
        if (object instanceof List) {
            for (Object value : (List<Object>) object) {
                if (!ignoreNulls && value == null) {
                    result.add(null);
                    continue;
                }
                if (value != null) {
                    Class<?> clazz = value.getClass();
                    if (clazz != null) {
                        if (!type.isAssignableFrom(clazz))
                            throw new AssertionError("Cannot cast to list! Key " + value + " is not a " + type.getSimpleName());
                        result.add(type.cast(value));
                    }
                }
            }
        }
        return result;
    }
}
