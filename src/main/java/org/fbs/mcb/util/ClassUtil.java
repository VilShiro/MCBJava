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
     * @param ancestor The class to be checked for being an ancestor of the {@code descendant} class.
     * @param descendant The class to be checked for being a descendant of the {@code ancestor} class.
     * @return {@code true} if the {@code ancestor} class is either the same as, or is a superclass or superinterface of, the {@code descendant} class;
     *         {@code false} otherwise.
     */
    public static boolean isAssignableFrom(Class<?> ancestor, Class<?> descendant) {
        Class<?> currentClass = descendant;
        if (ancestor == descendant){
            return true;
        }
        while (currentClass != null) {
            if (currentClass.equals(ancestor)) {
                return true;
            }
            Type[] interfaces = currentClass.getGenericInterfaces();
            for (Type iFace : interfaces) {
                if (isAssignableFrom(ancestor, (Class<?>) iFace)) {
                    return true;
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return false;
    }
}
