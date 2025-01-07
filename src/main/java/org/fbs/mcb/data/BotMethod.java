package org.fbs.mcb.data;

import lombok.Getter;
import org.fbs.mcb.util.MethodInvoker;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Represents a method that can be invoked by a bot.
 */
public class BotMethod {

    private final Method method;
    private final MethodSignature signature;
    private final boolean inNewThread;
    private final ClassReorder reorder;
    
    @Getter
    private final IgnoreType[] ignoreTypes;
    @Getter
    private final IgnoreSender[] ignoreSenders;
    @Getter
    private final MethodType type;
    @Getter
    private final String key;
    
    /**
     * Represents a method that can be invoked by a bot.
     *
     * @param method The method to be associated with this BotMethod instance.
     * @param signature The signature of the method.
     * @param inNewThread Indicates whether the method should be invoked in a new thread.
     * @param ignoreTypes An array of IgnoreType values indicating which types of messages should be ignored when invoking the method.
     * @param ignoreSenders An array of IgnoreSender values indicating which senders should be ignored when invoking the method.
     * @param type The type of method (e.g., UPDATE, MESSAGE).
     * @param key A unique identifier for the bot method.
     * @param reorder An instance of ClassReorder to handle class reordering.
     */
    public BotMethod(Method method, MethodSignature signature, boolean inNewThread, IgnoreType[] ignoreTypes, IgnoreSender[] ignoreSenders, MethodType type, String key, ClassReorder reorder) {
        this.method = method;
        this.signature = signature;
        this.inNewThread = inNewThread;
        this.ignoreTypes = ignoreTypes;
        this.ignoreSenders = ignoreSenders;
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
        List<Object> arguments = List.of(args);
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

}
