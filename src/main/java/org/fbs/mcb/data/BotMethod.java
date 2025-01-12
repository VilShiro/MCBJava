package org.fbs.mcb.data;

import org.fbs.mcb.util.MethodInvoker;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Represents a method that can be invoked by a bot.
 */
public class BotMethod {

    private final Method method;
    private final boolean inNewThread;
    private final ClassReorder reorder;

    private final MethodType type;
    private final String key;
    
    /**
     * Represents a method that can be invoked by a bot.
     *
     * @param method The method to be associated with this BotMethod instance.
     * @param inNewThread Indicates whether the method should be invoked in a new thread.
     * @param type The type of method (e.g., UPDATE, MESSAGE).
     * @param key A unique identifier for the bot method.
     * @param reorder An instance of ClassReorder to handle class reordering.
     */
    public BotMethod(Method method, boolean inNewThread, MethodType type, String key, ClassReorder reorder) {
        this.method = method;
        this.inNewThread = inNewThread;
        this.type = type;
        this.key = key;
        this.reorder = reorder;
    }

    /**
     * Invokes the method associated with this BotMethod instance.
     *
     * @param configObject  The object on which the method will be invoked.
     * @param args          The arguments to be passed to the method.
     * @throws InvocationTargetException  If the method invocation throws an exception.
     * @throws IllegalAccessException   If the method invocation is not allowed.
     */
    public void invoke(Object configObject, @NotNull Object ... args) throws InvocationTargetException, IllegalAccessException {
        if (inNewThread) {
            new Thread(() -> {
                try {
                    MethodInvoker.invokeMethod(
                            method,
                            MethodInvoker.isStatic(method),
                            configObject,
                            reorder.getMapped(args));
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        else {
            MethodInvoker.invokeMethod(
                    method,
                    MethodInvoker.isStatic(method),
                    configObject,
                    reorder.getMapped(args));
        }
    }
    
    /**
     * Retrieves the annotation of the specified type associated with the method.
     *
     * @param annotationClass The type of annotation to retrieve. This class must extend {@link Annotation}.
     * @param <T> The type of annotation to retrieve.
     *
     * @return The annotation of the specified type associated with the method, or {@code null} if no such annotation is present.
     *
     * @throws IllegalArgumentException If the specified annotation class is not an annotation type.
     * @throws SecurityException If a security manager, if present, denies reflective access to the method.
     */
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass){
        return method.getAnnotation(annotationClass);
    }

    /**
     * Returns the type of method associated with this BotMethod instance.
     *
     * @return The type of method (e.g., UPDATE, MESSAGE).
     */
    public MethodType getType() {
        return type;
    }

    /**
     * Returns the unique identifier associated with this BotMethod instance.
     *
     * @return The unique identifier for the bot method.
     */
    public String getKey() {
        return key;
    }

}
