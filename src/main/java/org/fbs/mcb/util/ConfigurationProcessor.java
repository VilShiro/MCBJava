package org.fbs.mcb.util;

import com.pengrad.telegrambot.model.Update;
import lombok.Getter;
import org.fbs.mcb.annotation.BotConfiguration;
import org.fbs.mcb.data.entity.Bot;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.fbs.mcb.util.AnnotationHandler.getAnnotation;

/**
 * A class responsible for processing bot configuration settings and creating configuration objects.
 * The class uses reflection to instantiate objects based on the specified class and its annotations.
 */
public class ConfigurationProcessor {

    /**
     * Holds the instance of the configuration object created from the specified class.
     * If the {@link BotConfiguration#staticBuild()} is set to true, this field will be null.
     * Otherwise, it will be initialized during the construction of the {@link ConfigurationProcessor} instance.
     *
     * @see BotConfiguration#staticBuild()
     */
    @Getter
    private final Object configurationObject;

    @Getter
    private final Class<?> configurationClass;

    /**
     * The object instance of the class specified in the constructor.
     * This instance is created using the default constructor of the specified class.
     * The class must have a public default constructor for this instance to be created successfully.
     *
     * @see BotConfiguration
     */
    private final BotConfiguration configuration;

    /**
     * Constructs a new instance of AnnotationHandler for the specified class.
     *
     * @param configurationClass the class for which the AnnotationHandler will be created.
     * @throws InvocationTargetException if the constructor invocation throws an exception.
     * @throws InstantiationException if the class cannot be instantiated.
     * @throws IllegalAccessException if the class or its default constructor is not accessible.
     * @throws NoSuchMethodException if the class does not have a default constructor.
     */
    public ConfigurationProcessor(Class<?> configurationClass) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        configuration = getAnnotation(configurationClass, BotConfiguration.class);
        this.configurationClass = configurationClass;
        if (configuration != null) {
            if (!configuration.staticBuild() && !ClassUtil.isAssignableFrom(Bot.class, configurationClass)) {
                configurationObject = createObject(configurationClass);
            }
            else {
                configurationObject = null;
            }
        }
        else {
            throw new IllegalArgumentException("The specified class does not have the BotConfiguration annotation");
        }
    }

    public void handle(Update update, Bot bot) throws InvocationTargetException, IllegalAccessException {
        new UpdateManager(this).processUpdate(update, bot, configurationObject);
    }

    /**
     * Creates a new instance of the specified class using its default constructor.
     *
     * @param clazz the class of the object to be instantiated, must not be null
     * @return a new instance of the specified class
     * @throws InstantiationException if the class cannot be instantiated
     * @throws IllegalAccessException if the class or its default constructor is not accessible
     * @throws InvocationTargetException if the constructor invocation throws an exception
     * @throws NoSuchMethodException if the class does not have a default constructor
     */
    @NotNull
    public static Object createObject(@NotNull Class<?> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    /**
     * Checks if the bot's configuration specifies double dispatch.
     * Double dispatch is a design pattern where a method call is resolved at runtime based on the actual type of the object.
     *
     * @return {@code true} if the bot's configuration specifies double dispatch, {@code false} otherwise.
     *
     * @see BotConfiguration#doubleDispatch()
     */
    public boolean isDoubleDispatch(){
        return configuration.doubleDispatch();
    }
    
    /**
     * Checks if the bot's configuration specifies static build.
     * Static build means that the configuration object is created during the construction of the
     * {@link ConfigurationProcessor} instance, regardless of the value of the {@link BotConfiguration#staticBuild()} annotation.
     * If the annotation value is true or if the specified class is assignable from {@link Bot}, the configuration object
     * will be created. Otherwise, it will be null.
     *
     * @return {@code true} if the bot's configuration specifies static build, {@code false} otherwise.
     *
     * @see BotConfiguration#staticBuild()
     * @see Bot
     * @see ConfigurationProcessor
     */
    public boolean isStaticBuild(){
        return configuration.staticBuild() || ClassUtil.isAssignableFrom(Bot.class, configurationClass);
    }
    
    /**
     * Checks if the bot's configuration specifies thread separation.
     * Thread separation is a design pattern where different parts of the bot's functionality are executed in separate threads.
     * This allows for better performance and responsiveness, especially when dealing with long-running tasks.
     *
     * @return {@code true} if the bot's configuration specifies thread separation, {@code false} otherwise.
     *
     * @see BotConfiguration#threadSeparation()
     */
    public boolean isThreadSeparation(){
        return configuration.threadSeparation();
    }
    
    /**
     * Retrieves the start command specified in the bot's configuration.
     * The start command is used to initiate specific actions when a user sends it.
     *
     * @return the start command specified in the bot's configuration.
     * If the start command is not specified or is an empty string, the default value is returned.
     *
     * @see BotConfiguration#startCommand()
     */
    public String getStartCommand() {
        return configuration.startCommand();
    }

    /**
     * Retrieves the bot token specified in the bot's configuration.
     * The bot token is used to authenticate the bot with the Telegram API.
     *
     * @return the bot token specified in the bot's configuration.
     * If the bot token is not specified or is an empty string, the default value is returned.
     *
     * @see BotConfiguration#botToken()
     */
    public String getBotToken(){
        //
        return configuration.botToken();
    }

    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object.
     * The string representation consists of the name of the class of this object,
     * the '@' character, the unsigned hexadecimal representation of the hash
     * code of this object, and the string representation of the configuration
     * and configurationObject fields.
     */
    @Override
    public String toString() {
        return "AnnotationHandler{\n" +
                "configuration=" + configuration +
                ",\nconfigurationObject=" + configurationObject +
                "\n}";
    }
}
