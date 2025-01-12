package org.fbs.mcb.util;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A utility class for invoking methods with unknown arguments based on their class types.
 * This class provides a method to invoke a method with arguments of unknown types,
 * and it checks for method signature compatibility and duplicate argument classes.
 */
public class MethodInvoker {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private MethodInvoker(){}

    /**
     * Generates all possible permutations of the given list of classes, starting from the specified index.
     * The generated permutations are added to the given result list.
     *
     * @param current The list of classes to generate permutations from.
     * @param start The index to start generating permutations from.
     * @param result The list to store the generated permutations.
     *
     * @throws NullPointerException If the given list or result is null.
     */
    public static void generatePermutations(@NotNull List<Class<?>> current, int start, List<List<Class<?>>> result) {
        if (start == current.size()) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < current.size(); i++) {
            swap(current, start, i);
            generatePermutations(current, start + 1, result);
            swap(current, start, i);
        }
    }

    /**
     * Swaps the elements at the specified indices in the given list.
     *
     * @param list The list in which to swap elements.
     * @param i    The index of the first element to swap.
     * @param j    The index of the second element to swap.
     *
     * @throws NullPointerException If the given list is null.
     */
    private static void swap(@NotNull List<Class<?>> list, int i, int j) {
        Class<?> temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    /**
     * Generates all possible subsets of the given array of classes, including permutations.
     *
     * @param classes The array of classes to generate subsets from.
     * @return A list of lists, where each inner list represents a subset of the input classes.
     *         The subsets are generated with permutations, meaning that the order of the classes in each subset is considered.
     *         The generated subsets are stored in the 'result' list.
     *
     * @throws NullPointerException If the given list is null.
     *
     * @see #generateAllSubsetsOfClasses(List)
     * @see #generatePermutations(List, int, List)
     */
    @NotNull
    public static List<List<Class<?>>> generateAllSubsetsWithPermutations(List<Class<?>> classes) {
        List<List<Class<?>>> result = new ArrayList<>();
        for (List<Class<?>> subset : generateAllSubsetsOfClasses(classes)) {
            generatePermutations(subset, 0, result);
        }
        return result;
    }

    /**
     * Generates all possible subsets of the given array of classes.
     *
     * @param classes The array of classes to generate subsets from.
     * @return A list of lists, where each inner list represents a subset of the input classes.
     * <p>
     * This function uses a recursive approach to generate all subsets.
     * It starts by adding the current class to the current subset and recursively
     * generates subsets with the next class. Then, it removes the current class
     * from the current subset and recursively generates subsets without the next class.
     * The process continues until all classes in the array have been processed.
     * The generated subsets are stored in the 'result' list.
     */
    @NotNull
    public static List<List<Class<?>>> generateAllSubsetsOfClasses(@NotNull List<Class<?>> classes) {
        List<List<Class<?>>> result = new ArrayList<>();
        for (int i = 0; i < (1 << classes.size()); i++) {
            List<Class<?>> current = new ArrayList<>();
            for (int j = 0; j < classes.size(); j++) {
                if ((i & (1 << j)) > 0) {
                    current.add(classes.get(j));
                }
            }
            result.add(current);
        }
        return result;
    }

    /**
     * Checks if the given list contains duplicate elements.
     *
     * @param list The list to be checked for duplicates.
     * @return {@code true} if the list contains duplicate elements, {@code false} otherwise.
     *
     * @throws NullPointerException If the given list is null.
     */
    public static boolean hasDuplicate(@NotNull List<?> list){
        Set<Object> uniqueElements = new HashSet<>();
        for (Object num : list) {
            if (!uniqueElements.add(num)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Invokes a non-static method on the specified object with the given arguments.
     *
     * @param method the non-static method to be invoked, must not be null
     * @param args the arguments to be passed to the method
     * @throws InvocationTargetException if the method invocation throws an exception
     * @throws IllegalAccessException if the method or its class is not accessible
     *
     * @throws IllegalArgumentException if the method is static or if the number of arguments does not match the number of method parameters
     * @throws IllegalArgumentException if the argument type is incompatible with the method parameter type
     */
    public static void invokeMethod(@NotNull Method method, boolean onlyStatic, Object configurationObject, @NotNull Object... args) throws InvocationTargetException, IllegalAccessException {
        if (!onlyStatic) {
            if (isStatic(method)) {
                invokeStaticMethod(method, args);
            }
            else {
                method.setAccessible(true);
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (args.length != parameterTypes.length) {
                    throw new IllegalArgumentException("The number of arguments does not match the number of method parameters");
                }
                for (int i = 0; i < args.length; i++) {
                    if (!parameterTypes[i].isAssignableFrom(args[i].getClass())) {
                        throw new IllegalArgumentException("Argument type " + i + " incompatible with method parameter type");
                    }
                }
                method.invoke(configurationObject, args);
            }
        }
        else {
            if (isStatic(method)) {
                invokeStaticMethod(method, args);
            }
        }
    }

    /**
     * Invokes a static method on the specified class with the given arguments.
     *
     * @param method the static method to be invoked, must not be null
     * @param args the arguments to be passed to the method
     * @throws InvocationTargetException if the method invocation throws an exception
     * @throws IllegalAccessException if the method or its class is not accessible
     *
     * @throws IllegalArgumentException if the method is not static
     */
    public static void invokeStaticMethod(@NotNull Method method, Object... args) throws InvocationTargetException, IllegalAccessException {
        if (!isStatic(method)) {
            throw new IllegalArgumentException("The method is not static");
        }
        method.setAccessible(true);
        method.invoke(null, args);
    }

    /**
     * Determines if the specified method is static.
     *
     * @param method the method to check for the static modifier
     * @return {@code true} if the method is static, {@code false} otherwise
     */
    public static boolean isStatic(@NotNull Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

}
