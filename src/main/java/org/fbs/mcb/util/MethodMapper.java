package org.fbs.mcb.util;

import org.fbs.mcb.annotation.Command;
import org.fbs.mcb.annotation.Feedback;
import org.fbs.mcb.data.BotMethod;
import org.fbs.mcb.data.ClassReorder;
import org.fbs.mcb.data.MethodSignature;
import org.fbs.mcb.data.MethodType;
import org.fbs.mcb.data.meta.Constants;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fbs.mcb.util.AnnotationHandler.getAnnotatedMethods;
import static org.fbs.mcb.util.MethodInvoker.generateAllSubsetsWithPermutations;
import static org.fbs.mcb.util.MethodInvoker.hasDuplicate;

/**
 * This class is responsible for mapping methods from a given configuration class to {@link BotMethod} objects.
 * It reads methods annotated with {@link Feedback} and {@link Command}, and creates corresponding {@link BotMethod} objects.
 * The class also handles ignoring update types and senders based on provided annotations.
 */
public class MethodMapper {

    /**
     * Reads methods from the configuration class and save them to {@link BotMethod} objects.
     *
     * @param processor The {@link ConfigurationProcessor} object used to retrieve the configuration class.
     * @param argsClasses The argument classes to be considered for method mapping.
     * @return A list of {@link BotMethod} objects representing the mapped methods.
     */
    public List<BotMethod> readMethods(ConfigurationProcessor processor, Class<?> ... argsClasses){
        List<BotMethod> botMethods = new ArrayList<>();

        Method[] methods;
        try {
            methods = getAnnotatedMethods(processor.getConfigurationObject().getClass(), Feedback.class);
        }catch (NullPointerException e){
            methods = getAnnotatedMethods(processor.getConfigurationClass(), Feedback.class);
        }

        Method[] commands;
        try {
            commands = getAnnotatedMethods(processor.getConfigurationObject().getClass(), Command.class);
        }catch (NullPointerException e){
            commands = getAnnotatedMethods(processor.getConfigurationClass(), Command.class);
        }

        for (Method method: methods){
            MethodSignature signature = getSignature(method, List.of(argsClasses), Arrays.toString(argsClasses), argsClasses);
            switch (method.getAnnotation(Feedback.class).value()){
                case "update":{
                    botMethods.add(
                            new BotMethod(method,
                                    processor.isThreadSeparation(),
                                    MethodType.UPDATE,
                                    "update",
                                    new ClassReorder(
                                            Constants.UPDATE_PARAMETERS,
                                            signature.parameterTypes()
                                    ))
                    );
                    break;
                }
                case "start":{
                    botMethods.add(
                            new BotMethod(method,
                                    processor.isThreadSeparation(),
                                    MethodType.START,
                                    processor.getStartCommand(),
                                    new ClassReorder(
                                            Constants.START_PARAMETERS,
                                            signature.parameterTypes()
                                    ))
                    );
                    break;
                }
                case "inline_query":{
                    botMethods.add(
                            new BotMethod(method,
                                    processor.isThreadSeparation(),
                                    MethodType.INLINE_QUERY,
                                    "inline_query",
                                    new ClassReorder(
                                            Constants.INLINE_QUERY_PARAMETERS,
                                            signature.parameterTypes()
                                    ))
                    );
                    break;
                }
                case "callback_query":{
                    botMethods.add(
                            new BotMethod(method,
                                    processor.isThreadSeparation(),
                                    MethodType.CALLBACK_QUERY,
                                    "callback_query",
                                    new ClassReorder(
                                            Constants.CALLBACK_QUERY_PARAMETERS,
                                            signature.parameterTypes()
                                    ))
                    );
                    break;
                }
                case "message":{
                    botMethods.add(
                            new BotMethod(method,
                                    processor.isThreadSeparation(),
                                    MethodType.MESSAGE,
                                    "message",
                                    new ClassReorder(
                                            Constants.MESSAGE_PARAMETERS,
                                            signature.parameterTypes()
                                    ))
                    );
                    break;
                }
                case "entities":{
                    botMethods.add(
                            new BotMethod(method,
                                    processor.isThreadSeparation(),
                                    MethodType.ENTITIES,
                                    "entities",
                                    new ClassReorder(
                                            Constants.ENTITIES_PARAMETERS,
                                            signature.parameterTypes()
                                    ))
                    );
                    break;
                }
                default:{
                    break;
                }
            }
        }

        for (Method command: commands){
            MethodSignature signature = getSignature(command, List.of(argsClasses), Arrays.toString(argsClasses), argsClasses);
            botMethods.add(
                    new BotMethod(command,
                            processor.isThreadSeparation(),
                            MethodType.COMMAND,
                            command.getAnnotation(Command.class).value(),
                            new ClassReorder(
                                    Constants.COMMAND_PARAMETERS,
                                    signature.parameterTypes()
                            ))
            );
        }

        return botMethods;
    }

    /**
     * Checks if a method's signature matches the provided argument classes.
     *
     * @param method The method to be checked.
     * @param argsClasses2 The list of argument classes to be considered.
     * @param string The string representation of the argument classes.
     * @param argsClasses The array of argument classes.
     * @return The {@link MethodSignature} object representing the method's signature.
     * @throws IllegalArgumentException If the method signature is not supported or if duplicate argument classes are found.
     */
    @NotNull
    private MethodSignature getSignature(Method method, List<Class<?>> argsClasses2, String string, Class<?>[] argsClasses) {
        if (!hasDuplicate(List.of(argsClasses))){
            for (List<Class<?>> classes: generateAllSubsetsWithPermutations(argsClasses2)){
                MethodSignature signature = new MethodSignature(void.class, classes.toArray(new Class[0]));
                if (signature.checkMethodSignature(method)){
                    return signature;
                }
            }
            throw new IllegalArgumentException("Method signature are not supported: " + method.toGenericString() + ", supported elements of method signature: " + string);
        }
        else {
            throw new IllegalArgumentException("Not annotated duplicate argument classes in method signature: " + method.toGenericString());
        }
    }

}
