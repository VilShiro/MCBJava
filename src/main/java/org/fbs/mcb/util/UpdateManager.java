package org.fbs.mcb.util;

import com.pengrad.telegrambot.model.*;
import org.fbs.mcb.annotation.Command;
import org.fbs.mcb.annotation.Feedback;
import org.fbs.mcb.data.entity.Bot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import static org.fbs.mcb.util.AnnotationHandler.getAnnotatedMethods;

/**
 * The UpdateManager class is responsible for processing updates received from a Telegram bot.
 * It categorizes the update type and performs specific actions accordingly.
 * It utilizes reflection to invoke methods annotated with {@link Feedback} and {@link Command} annotations.
 */
public class UpdateManager {

    /**
     * The ConfigurationProcessor instance used for processing updates.
     */
    private final ConfigurationProcessor processor;

    /**
     * Constructs a new UpdateManager instance.
     *
     * @param processor The ConfigurationProcessor instance used for processing updates.
     */
    public UpdateManager(ConfigurationProcessor processor) {
        this.processor = processor;
    }

    /**
     * Processes the received update from Telegram bot.
     * This method categorizes the update type and performs specific actions accordingly.
     *
     * @param update the received update from Telegram bot
     */
    public void processUpdate(Update update, Bot bot, Object configurationObject) throws InvocationTargetException, IllegalAccessException {
        updateParse(update, bot);

        if (update.message() != null && !Objects.equals(update.message().text(), "")){
            Message message = update.message();
            messageParse(message, bot);
            try {
                if (message.entities().length > 0) {
                    MessageEntity[] entities = message.entities();
                    entitiesParse(entities, message, bot);
                    if (message.text().contains(processor.getStartCommand())) {
                        onStartCommand(message, bot);
                    }
                }
            }catch (NullPointerException ignored){}
        }
        else if (update.callbackQuery() != null){
            CallbackQuery query = update.callbackQuery();
            callbackQueryParse(query, bot);
        }
        else if (update.inlineQuery() != null) {
            InlineQuery query = update.inlineQuery();
            inlineQueryParse(query, bot);
        }
    }

    /**
     * Parses and processes inline query updates received from Telegram bot.
     * This method retrieves methods annotated with {@link Feedback} and type "inline_query" from the
     * configuration object's class. It then invokes the appropriate method based on the method signature and the received
     * inline query.
     *
     * @param query the received inline query from Telegram bot
     * @throws InvocationTargetException if the method invocation throws an exception
     * @throws IllegalAccessException    if the method or its class is not accessible
     */
    private void inlineQueryParse(InlineQuery query, Bot bot) throws InvocationTargetException, IllegalAccessException {
        Method[] methods;
        try {
            methods = getAnnotatedMethods(processor.getConfigurationObject().getClass(), Feedback.class);
        }catch (NullPointerException e){
            methods = getAnnotatedMethods(processor.getConfigurationClass(), Feedback.class);
        }
        for (Method method : methods) {
            if (method.getAnnotation(Feedback.class).type().equalsIgnoreCase("inline_query")){
                MethodInvoker.invokeUnknownMethod(method, processor.isStaticBuild(), processor.getConfigurationObject(), query, bot);
            }
        }
    }

    /**
     * Parses and processes callback query updates received from Telegram bot.
     * This method retrieves methods annotated with {@link Feedback} and type "callback_query" from the
     * configuration object's class. It then invokes the appropriate method based on the method signature and the received
     * callback query.
     *
     * @param query the received callback query from Telegram bot
     * @throws InvocationTargetException if the method invocation throws an exception
     * @throws IllegalAccessException    if the method or its class is not accessible
     */
    private void callbackQueryParse(CallbackQuery query, Bot bot) throws InvocationTargetException, IllegalAccessException {
        Method[] methods;
        try {
            methods = getAnnotatedMethods(processor.getConfigurationObject().getClass(), Feedback.class);
        }catch (NullPointerException e){
            methods = getAnnotatedMethods(processor.getConfigurationClass(), Feedback.class);
        }        for (Method method : methods) {
            if (method.getAnnotation(Feedback.class).type().equalsIgnoreCase("callback_query")){
                MethodInvoker.invokeUnknownMethod(method, processor.isStaticBuild(), processor.getConfigurationObject(), query, bot);
            }
        }
    }

    /**
     * Processes the start command received from a Telegram bot.
     * This method retrieves methods annotated with {@link Feedback} and type "start" from the
     * configuration object's class. It then invokes the appropriate method based on the method signature and the received
     * start command.
     *
     * @param message the received start command from Telegram bot
     * @throws InvocationTargetException if the method invocation throws an exception
     * @throws IllegalAccessException    if the method or its class is not accessible
     */
    private void onStartCommand(Message message, Bot bot) throws InvocationTargetException, IllegalAccessException {
        Method[] methods;
        try {
            methods = getAnnotatedMethods(processor.getConfigurationObject().getClass(), Feedback.class);
        }catch (NullPointerException e){
            methods = getAnnotatedMethods(processor.getConfigurationClass(), Feedback.class);
        }
        for (Method method : methods) {
            if (method.getAnnotation(Feedback.class).type().equalsIgnoreCase("start")){
                MethodInvoker.invokeUnknownMethod(method, processor.isStaticBuild(), processor.getConfigurationObject(), message, bot);
            }
        }
    }

    /**
     * Parses and processes the received entities from a Telegram bot message.
     * This method retrieves methods annotated with {@link Feedback} and type "entities" from the
     * configuration object's class. It then invokes the appropriate method based on the method signature and the received
     * entities and message.
     *
     * @param entities The received entities from the Telegram bot message.
     * @param message  The received message from the Telegram bot.
     * @throws InvocationTargetException If the method invocation throws an exception.
     * @throws IllegalAccessException    If the method or its class is not accessible.
     * @throws IllegalArgumentException  If the method signature is invalid for the entities' handler.
     */
    private void entitiesParse(MessageEntity[] entities, Message message, Bot bot) throws InvocationTargetException, IllegalAccessException {
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
        for (Method method : methods) {
            if (method.getAnnotation(Feedback.class).type().equalsIgnoreCase("entities")){
                MethodInvoker.invokeUnknownMethod(method, processor.isStaticBuild(), processor.getConfigurationObject(), entities,  message, bot);
            }
        }
        for (Method method : commands){
            if (Objects.equals(method.getAnnotation(Command.class).command(), message.text())){
                if (processor.isThreadSeparation()) {
                    new Thread(() -> {
                        try {
                            MethodInvoker.invokeUnknownMethod(method, processor.isStaticBuild(), processor.getConfigurationObject(), message, bot);
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }).start();
                }
                else {
                    MethodInvoker.invokeUnknownMethod(method, processor.isStaticBuild(), processor.getConfigurationObject(), message, bot);
                }
            }
        }
    }

    /**
     * Processes the received message from a Telegram bot.
     * This method retrieves methods annotated with {@link Feedback} and type "message" from the
     * configuration object's class. It then invokes the appropriate method based on the method signature and the received
     * message.
     *
     * @param message The received message from Telegram bot.
     * @throws InvocationTargetException If the method invocation throws an exception.
     * @throws IllegalAccessException    If the method or its class is not accessible.
     * @throws IllegalArgumentException  If the method signature is invalid for the message handler.
     */
    private void messageParse(Message message, Bot bot) throws InvocationTargetException, IllegalAccessException {
        Method[] methods;
        try {
            methods = getAnnotatedMethods(processor.getConfigurationObject().getClass(), Feedback.class);
        }catch (NullPointerException e){
            methods = getAnnotatedMethods(processor.getConfigurationClass(), Feedback.class);
        }
        for (Method method : methods) {
            if (method.getAnnotation(Feedback.class).type().equalsIgnoreCase("message")){
                MethodInvoker.invokeUnknownMethod(method, processor.isStaticBuild(), processor.getConfigurationObject(), message, bot);
            }
        }
    }

    /**
     * Parses and processes the received update from a Telegram bot.
     * This method categorizes the update type and performs specific actions accordingly.
     *
     * @param update The received update from Telegram bot.
     * @throws InvocationTargetException If the method invocation throws an exception.
     * @throws IllegalAccessException    If the method or its class is not accessible.
     */
    private void updateParse(Update update, Bot bot) throws InvocationTargetException, IllegalAccessException {
        Method[] methods;
        try {
            methods = getAnnotatedMethods(processor.getConfigurationObject().getClass(), Feedback.class);
        }catch (NullPointerException e){
            methods = getAnnotatedMethods(processor.getConfigurationClass(), Feedback.class);
        }

        for (Method method : methods) {
            if (method.getAnnotation(Feedback.class).type().equalsIgnoreCase("update")){
                MethodInvoker.invokeUnknownMethod(method, processor.isStaticBuild(), processor.getConfigurationObject(), update, bot);
            }
        }
    }

}
