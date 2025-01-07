package org.fbs.mcb.data;

import org.fbs.mcb.util.MethodInvoker;

import java.util.ArrayList;
import java.util.List;

public class ClassReorder {

    private final Class<?>[] input;
    private final Class<?>[] output;

    private final List<IndexPair> pairList = new ArrayList<>();

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

    private void parsePairs(){
        for (int i = 0; i < input.length; i++){
            if (List.of(output).contains(input[i])){
                pairList.add(new IndexPair(i, List.of(output).indexOf(input[i])));
            }
            else {
                pairList.add(new IndexPair(i, -1));
            }
        }
    }

    public Object[] getMapped(Object ... args){
        Object[] out = new Object[output.length];
        for (int i = 0; i < input.length; i++){
            IndexPair pair = pairList.get(i);
            if (pair.outputIndex() != -1){
                out[pair.outputIndex()] = args[pair.inputIndex()];
            }
        }
        return out;
    }

}
