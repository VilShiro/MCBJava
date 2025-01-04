package org.fbs.mcb.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * This utility class provides methods for working with annotations in Java.
 */
public class AnnotationHandler {

    /**
     * Retrieves the specified annotation from the given class.
     *
     * @param <T> the type of the annotation to be retrieved
     * @param clazz the class from which the annotation is to be retrieved, must not be null
     * @param annotationClass the class object corresponding to the annotation type, must not be null
     * @return the annotation of the specified type if present on the class, or {@code null} if not present
     */
    @Contract(pure = true)
    public static  <T extends Annotation> T getAnnotation(@NotNull Class<?> clazz, Class<T> annotationClass) {
        return clazz.getAnnotation(annotationClass);
    }

    /**
     * Retrieves an array of methods from the specified class that are annotated with the given annotation type.
     *
     * @param clazz the class from which the methods are to be retrieved, must not be null
     * @param annotationType the class object corresponding to the annotation type, must not be null
     * @return an array of methods annotated with the given annotation type, or an empty array if no such methods are found
     * @throws NullPointerException if either {@code clazz} or {@code annotationType} is null
     */
    @NotNull
    public static Method[] getAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotationType) {
        List<Method> annotatedMethods = new ArrayList<>();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(annotationType)) {
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods.toArray(new Method[0]);
    }

}
