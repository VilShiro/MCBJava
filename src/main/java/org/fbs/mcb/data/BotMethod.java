package org.fbs.mcb.data;

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

    /**
     * Constructs a new BotMethod instance.
     *
     * @param method       The method to be invoked.
     * @param signature    The method's signature.
     * @param inNewThread  Whether the method should be invoked in a new thread.
     */
    public BotMethod(Method method, MethodSignature signature, boolean inNewThread) {
        this.method = method;
        this.signature = signature;
        this.inNewThread = inNewThread;
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
                            MethodInvoker.getParamsByClasses(List.of(signature.parameterTypes()), arguments));
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
                    MethodInvoker.getParamsByClasses(List.of(signature.parameterTypes()), arguments));
        }
    }

}
