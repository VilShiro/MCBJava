package org.fbs.mcb.data;

import org.fbs.mcb.annotation.Command;
import org.fbs.mcb.util.ConfigurationProcessor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manages a set of bot methods that can be called based on different types of events.
 * It provides methods to add bot methods and invoke them based on the event type.
 */
public class BotMethodSet {

    private final List<BotMethod> updateMethodList = new ArrayList<>();
    private final List<BotMethod> messageMethodList = new ArrayList<>();
    private final List<BotMethod> entitiesMethodList = new ArrayList<>();
    private final List<BotMethod> startMethodList = new ArrayList<>();
    private final List<BotMethod> callbackQueryMethodList = new ArrayList<>();
    private final List<BotMethod> inlineQueryMethodList = new ArrayList<>();
    private final List<BotMethod> commandMethodList = new ArrayList<>();

    private final ConfigurationProcessor processor;

    /**
     * Constructs a new BotMethodSet with the given ConfigurationProcessor.
     *
     * @param processor the ConfigurationProcessor to be used for invoking bot methods
     */
    public BotMethodSet(ConfigurationProcessor processor){
        this.processor = processor;
    }

    /**
     * Adds a bot method to the appropriate list based on its type.
     *
     * @param method the BotMethod to be added
     */
    public void addMethod(@NotNull BotMethod method){
        switch (method.getType()){
            case UPDATE:
                updateMethodList.add(method);
                break;
            case MESSAGE:
                messageMethodList.add(method);
                break;
            case ENTITIES:
                entitiesMethodList.add(method);
                break;
            case START:
                startMethodList.add(method);
                break;
            case CALLBACK_QUERY:
                callbackQueryMethodList.add(method);
                break;
            case INLINE_QUERY:
                inlineQueryMethodList.add(method);
                break;
            case COMMAND:
                commandMethodList.add(method);
                break;
        }
    }

    /**
     * Calls all bot methods registered for the UPDATE event type with the given arguments.
     *
     * @param args the arguments to be passed to the bot methods
     *
     * @throws RuntimeException if an error occurs while invoking a bot method
     */
    public void callUpdate(Object ... args){
        for (BotMethod method : updateMethodList) {
            try {
                method.invoke(processor.getConfigurationObject(), args);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Calls all bot methods registered for the MESSAGE event type with the given arguments.
     *
     * @param args the arguments to be passed to the bot methods. These arguments will be passed to each bot method
     *             in the order they are provided.
     *
     * @throws RuntimeException if an error occurs while invoking a bot method. The exception will contain the
     *                          original cause of the error.
     */
    public void callMessage(Object ... args){
        for (BotMethod method : messageMethodList) {
            try {
                method.invoke(processor.getConfigurationObject(), args);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Calls all bot methods registered for the ENTITIES event type with the given arguments.
     *
     * @param args the arguments to be passed to the bot methods. These arguments will be passed to each bot method
     *             in the order they are provided.
     *
     * @throws RuntimeException if an error occurs while invoking a bot method. The exception will contain the
     *                          original cause of the error.
     */
    public void callEntities(Object ... args){
        for (BotMethod method : entitiesMethodList) {
            try {
                method.invoke(processor.getConfigurationObject(), args);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Calls all bot methods registered for the CALLBACK_QUERY event type with the given arguments.
     *
     * @param args the arguments to be passed to the bot methods. These arguments will be passed to each bot method
     *             in the order they are provided.
     *
     * @throws RuntimeException if an error occurs while invoking a bot method. The exception will contain the
     *                          original cause of the error.
     */
    public void callCallbackQuery(Object ... args){
        for (BotMethod method : callbackQueryMethodList) {
            try {
                method.invoke(processor.getConfigurationObject(), args);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Calls all bot methods registered for the INLINE_QUERY event type with the given arguments.
     *
     * @param args the arguments to be passed to the bot methods. These arguments will be passed to each bot method
     *             in the order they are provided.
     *
     * @throws RuntimeException if an error occurs while invoking a bot method. The exception will contain the
     *                          original cause of the error.
     */
    public void callInlineQuery(Object ... args){
        for (BotMethod method : inlineQueryMethodList) {
            try {
                method.invoke(processor.getConfigurationObject(), args);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Calls all bot methods registered for the START event type with the given arguments.
     *
     * @param args the arguments to be passed to the bot methods. These arguments will be passed to each bot method
     *             in the order they are provided.
     *
     * @throws RuntimeException if an error occurs while invoking a bot method. The exception will contain the
     *                          original cause of the error.
     */
    public void callStart(Object ... args){
        for (BotMethod method : startMethodList) {
            try {
                method.invoke(processor.getConfigurationObject(), args);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Calls all bot methods registered for the COMMAND event type with the given arguments.
     *
     * @param args the arguments to be passed to the bot methods. These arguments will be passed to each bot method
     *             in the order they are provided.
     *
     * @throws RuntimeException if an error occurs while invoking a bot method. The exception will contain the
     *                          original cause of the error.
     */
    public void callCommands(String message, Object ... args){
        for (BotMethod method : commandMethodList) {
            try {
                if (method.getAnnotation(Command.class).additionalString()) {
                    if (message.startsWith(method.getKey().strip())) {
                        method.invoke(processor.getConfigurationObject(), args);
                    }
                }
                else {
                    if (message.strip().equals(method.getKey().strip())) {
                        method.invoke(processor.getConfigurationObject(), args);
                    }
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
