package org.fbs.mcb.data;

import org.fbs.mcb.util.ClassUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

/**
 * Represents a method signature consisting of a return type and parameter types.
 *
 * @param returnType The return type of the method.
 * @param parameterTypes The parameter types of the method.
 */
public record MethodSignature(Class<?> returnType, Class<?>... parameterTypes) {

    /**
     * Checks if the given method's signature matches the current {@link MethodSignature} instance.
     *
     * @param method The method to check.
     * @return {@code true} if the method's signature matches the current instance, {@code false} otherwise.
     *
     * <p>The method signature is considered to match if:
     * <ul>
     *     <li>The method's return type is assignable from the current instance's return type.</li>
     *     <li>The method's parameter types match the current instance's parameter types.</li>
     * </ul>
     *
     * <p>The method's parameter types are considered to match if:
     * <ul>
     *     <li>The number of parameters in the method matches the number of parameter types in the current instance.</li>
     *     <li>For each parameter, the parameter type in the method is assignable from the corresponding parameter type in the current instance.</li>
     * </ul>
     */
    public boolean checkMethodSignature(@NotNull Method method) {
        return ClassUtil.isAssignableFrom(method.getReturnType(), returnType) &&
                checkParameter(method);
    }

    /**
     * Checks if the given method's parameter types match the current {@link MethodSignature} instance.
     *
     * @param method The method to check.
     * @return {@code true} if the method's parameter types match the current instance, {@code false} otherwise.
     *
     * <p>The method's parameter types are considered to match if:
     * <ul>
     *     <li>The number of parameters in the method matches the number of parameter types in the current instance.</li>
     *     <li>For each parameter, the parameter type in the method is assignable from the corresponding parameter type in the current instance.</li>
     * </ul>
     *
     * @throws ArrayIndexOutOfBoundsException If the number of parameters in the method does not match the number of parameter types in the current instance.
     */
    private boolean checkParameter(@NotNull Method method){
        Class<?>[] params = method.getParameterTypes();
        for (int i = 0; i < params.length; i++){
            try {
                if (!ClassUtil.isAssignableFrom(params[i], parameterTypes[i])) {
                    return false;
                }
            }catch (ArrayIndexOutOfBoundsException e){
                return false;
            }
        }
        return true;
    }

}
