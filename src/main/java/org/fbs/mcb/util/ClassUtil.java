package org.fbs.mcb.util;

import java.lang.reflect.Type;

/**
 * Utility class for working with classes.
 */
public class ClassUtil {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ClassUtil(){}

    /**
     * Checks if the specified class is either the same as, or is a superclass or superinterface of, the specified class.
     *
     * @param parent The class to be checked for being a parent of the {@code child} class.
     * @param child The class to be checked for being a child of the {@code parent} class.
     * @return {@code true} if the {@code parent} class is either the same as, or is a superclass or superinterface of, the {@code child} class;
     *         {@code false} otherwise.
     */
    public static boolean isAssignableFrom(Class<?> parent, Class<?> child) {
        Class<?> currentClass = child;
        if (parent == child){
            return true;
        }
        while (currentClass != null) {
            if (currentClass.equals(parent)) {
                return true;
            }
            Type[] interfaces = currentClass.getGenericInterfaces();
            for (Type iFace : interfaces) {
                if (isAssignableFrom(parent, (Class<?>) iFace)) {
                    return true;
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return false;
    }
}
