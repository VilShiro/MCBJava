package org.fbs.mcb.util;

import org.fbs.mcb.annotation.Command;
import org.fbs.mcb.annotation.Feedback;
import org.fbs.mcb.data.BotMethod;
import org.fbs.mcb.data.ClassReorder;
import org.fbs.mcb.data.MethodSignature;
import org.fbs.mcb.data.MethodType;
import org.fbs.mcb.data.meta.Constants;
import org.fbs.mcb.util.base.AbstractMethodMapper;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fbs.mcb.util.AnnotationUtil.getAnnotatedMethods;
import static org.fbs.mcb.util.MethodInvoker.generateAllSubsetsWithPermutations;
import static org.fbs.mcb.util.MethodInvoker.hasDuplicate;

/**
 * This class extends AbstractMethodMapper and provides functionality to map methods annotated with
 * {@link Feedback} and {@link Command} to {@link BotMethod} objects.
 */
public class MethodMapper extends AbstractMethodMapper {

    /**
     * Sets the {@link ConfigurationProcessor} instance for this class.
     * This method overrides the superclass's method to provide a specific implementation.
     *
     * @param processor The {@link ConfigurationProcessor} instance to be set.
     *                  This instance is used to process configuration data and provide necessary functionality.
     */
    @Override
    public void setProcessor(ConfigurationProcessor processor) {
        super.setProcessor(processor);
    }

    /**
     * This method reads methods annotated with {@link Feedback} and {@link Command} from the provided arguments and maps them to
     * {@link BotMethod} objects. It also handles ignoring update types and senders based on provided annotations.
     *
     * @param args Varargs of Object type representing the arguments to be considered when mapping methods.
     * @return A List of {@link BotMethod} objects representing the mapped methods.
     * @throws NullPointerException If the ConfigurationProcessor's configuration object or class is null.
     * @throws IllegalArgumentException If the method signature is not supported or if duplicate argument classes are found.
     */
    @Override
    public List<BotMethod> readMethods(Object ... args){
        Class<?>[] argsClasses = null;
        for (Object arg : args) {
            if (arg.getClass() == Class[].class) {
                argsClasses = (Class<?>[]) arg;
                break;
            }
        }
        if (argsClasses == null){
            throw new IllegalArgumentException("Invalid arguments. Expected an array of Class<?> objects.");
        }
        
        List<BotMethod> botMethods = new ArrayList<>();

        Method[] methods;
        try {
            methods = getAnnotatedMethods(getProcessor().getConfigurationObject().getClass(), Feedback.class);
        }catch (NullPointerException e){
            methods = getAnnotatedMethods(getProcessor().getConfigurationClass(), Feedback.class);
        }

        Method[] commands;
        try {
            commands = getAnnotatedMethods(getProcessor().getConfigurationObject().getClass(), Command.class);
        }catch (NullPointerException e){
            commands = getAnnotatedMethods(getProcessor().getConfigurationClass(), Command.class);
        }

        for (Method method: methods){
            MethodSignature signature = getSignature(method, List.of(argsClasses), Arrays.toString(argsClasses), argsClasses);
            switch (method.getAnnotation(Feedback.class).value()){
                case "update":{
                    botMethods.add(
                            new BotMethod(method,
                                    getProcessor().isThreadSeparation(),
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
                                    getProcessor().isThreadSeparation(),
                                    MethodType.START,
                                    getProcessor().getStartCommand(),
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
                                    getProcessor().isThreadSeparation(),
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
                                    getProcessor().isThreadSeparation(),
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
                                    getProcessor().isThreadSeparation(),
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
                                    getProcessor().isThreadSeparation(),
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
                            getProcessor().isThreadSeparation(),
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
                if (signature.checkParameters(method)){
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
