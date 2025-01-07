package org.fbs.mcb.data;

import org.fbs.mcb.util.ClassUtil;
import org.fbs.mcb.util.MethodInvoker;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for reordering input arguments based on a given input and output array of classes.
 * It provides a method to map the input arguments to the output array based on their class types.
 */
public class ClassReorder {

    private final Class<?>[] input;
    private final Class<?>[] output;

    private final List<IndexPair> pairList = new ArgsList<>();

    /**
     * Constructs a new ClassReorder instance.
     *
     * @param input  The array of input classes. Cannot contain duplicate classes.
     * @param output The array of output classes. Cannot contain duplicate classes.
     * @throws IllegalArgumentException If the input or output array contains duplicate classes.
     */
    public ClassReorder(Class<?>[] input, Class<?>[] output){
        if (MethodInvoker.hasDuplicate(List.of(input))){
            throw new IllegalArgumentException("Input array cannot contain duplicate classes.");
        }
        if (MethodInvoker.hasDuplicate(List.of(output))){
            throw new IllegalArgumentException("Output array cannot contain duplicate classes.");
        }
        this.input = input;
        this.output = output;
        parsePairs();
    }

    /**
     * Parses the input and output arrays to create a list of index pairs.
     * Each pair represents the corresponding indices of the same class type in the input and output arrays.
     * If a class type is not found in the output array, the output index of the pair is set to -1.
     */
    private void parsePairs(){
        for (int i = 0; i < input.length; i++){
            // Check if the current input class is present in the output array
            if (List.of(output).contains(input[i])){
                // If present, add a new IndexPair with the input and output indices
                pairList.add(new IndexPair(i, List.of(output).indexOf(input[i])));
            }
            else {
                // If not present, add a new IndexPair with the input index and -1 as the output index
                pairList.add(new IndexPair(i, -1));
            }
        }
    }

    /**
     * Maps the input arguments to the output array based on their class types.
     *
     * @param args The input arguments. Must have the same length as the output array.
     * @return The mapped output array.
     * @throws IllegalArgumentException If the input array does not have the same length as the output array.
     */
    public Object[] getMapped(@NotNull Object ... args){
        Object[] out = new Object[output.length];
        if (args.length != input.length){
            throw new IllegalArgumentException("Input array must have the same length as the output array.");
        }
        for (int i = 0; i < input.length; i++){
            IndexPair pair = pairList.get(i);
            if (pair.outputIndex() != -1){
                out[pair.outputIndex()] = args[pair.inputIndex()];
            }
        }
        return out;
    }

    /**
     * This is a custom ArrayList implementation that extends ArrayList<T> and overrides the contains() and indexOf() methods.
     * The overridden methods provide additional functionality to check for class inheritance relationships when searching for elements.
     *
     * @param <T> The type of elements in the list.
     */
    private static class ArgsList<T> extends ArrayList<T> {

        /**
         * Checks if this list contains the specified element or any of its subclasses.
         *
         * @param o The element to be checked for containment.
         * @return {@code true} if this list contains the specified element or any of its subclasses; {@code false} otherwise.
         */
        @Override
        public boolean contains(Object o) {
            boolean isContain = indexOf(o) != -1;
            boolean isContainChild = false;
            for (int i = 0; i < size(); i++) {
                if (ClassUtil.isAssignableFrom(o.getClass(), get(i).getClass())) {
                    isContainChild = true;
                    break;
                }
            }

            return isContain || isContainChild;
        }

        /**
         * Returns the index of the first occurrence of the specified element or any of its subclasses in this list,
         * or -1 if this list does not contain the element.
         *
         * @param o The element to search for.
         * @return The index of the first occurrence of the specified element or any of its subclasses, or -1 if not found.
         */
        @Override
        public int indexOf(Object o) {
            for (int i = 0; i < size(); i++) {
                if (ClassUtil.isAssignableFrom(o.getClass(), get(i).getClass())) {
                    return i;
                }
            }
            return -1;
        }
    }

}
