package org.fbs.mcb.util;

import com.pengrad.telegrambot.model.Update;
import org.fbs.mcb.annotation.BotConfiguration;
import org.fbs.mcb.annotation.Command;
import org.fbs.mcb.annotation.Feedback;
import org.fbs.mcb.data.BotMethod;
import org.fbs.mcb.data.BotMethodSet;
import org.fbs.mcb.data.entity.AbstractBot;
import org.fbs.mcb.data.entity.Bot;
import org.fbs.mcb.data.meta.Constants;
import org.fbs.mcb.util.base.AbstractConfigurationProcessor;
import org.fbs.mcb.util.base.AbstractMethodMapper;
import org.fbs.mcb.util.base.AbstractUpdateManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.fbs.mcb.util.AnnotationUtil.getAnnotation;

/**
 * A class responsible for processing bot configuration settings and creating configuration objects.
 * The class uses reflection to instantiate objects based on the specified class and its annotations.
 */
public class ConfigurationProcessor extends AbstractConfigurationProcessor {

    /**
     * Holds the instance of the configuration object created from the specified class.
     * If the {@link BotConfiguration#staticBuild()} is set to true, this field will be null.
     * Otherwise, it will be initialized during the construction of the {@link ConfigurationProcessor} instance.
     *
     * @see BotConfiguration#staticBuild()
     */
    private final Object configurationObject;

    /**
     * Holds the reference to the class specified in the constructor.
     * This class is used to create configuration objects based on the specified class and its annotations.
     *
     * @see ConfigurationProcessor#ConfigurationProcessor(Class, AbstractUpdateManager, AbstractMethodMapper)
     * @see BotConfiguration
     */
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
     * Holds the reference to the {@link UpdateManager} instance responsible for managing and processing updates.
     * This instance is created during the construction of the {@link ConfigurationProcessor} instance.
     *
     * @see UpdateManager
     */
    private final AbstractUpdateManager updateManager;

    /**
     * Holds the reference to the set of bot methods associated with the configuration.
     *
     * @see BotMethodSet
     * @see MethodMapper
     */
    private final BotMethodSet methodSet;

    /**
     * Constructs a new instance of AnnotationHandler for the specified class.
     *
     * @param configurationClass the class for which the AnnotationHandler will be created.
     * @throws InvocationTargetException if the constructor invocation throws an exception.
     * @throws InstantiationException if the class cannot be instantiated.
     * @throws IllegalAccessException if the class or its default constructor is not accessible.
     * @throws NoSuchMethodException if the class does not have a default constructor.
     */
    public ConfigurationProcessor(Class<?> configurationClass, AbstractUpdateManager updateManager, AbstractMethodMapper methodMapper) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        configuration = getAnnotation(configurationClass, BotConfiguration.class);
        this.configurationClass = configurationClass;
        if (configuration != null) {
            if (!configuration.staticBuild() && !ClassUtil.isAssignableFrom(Bot.class, configurationClass)) {
                configurationObject = createObject(configurationClass);
            }
            else {
                configurationObject = null;
            }
            methodSet = new BotMethodSet(this);
            this.updateManager = updateManager;
            this.updateManager.setProcessor(this);

            methodMapper.setProcessor(this);
            for (BotMethod method: methodMapper.readMethods(this, Constants.getAllUniqueClasses())){
                methodSet.addMethod(method);
            }
        }
        else {
            throw new IllegalArgumentException("The specified class does not have the BotConfiguration annotation");
        }
    }

    @Override
    public void handle(Object ... args){
        Update update = null;
        AbstractBot<?> bot = null;
        if (args[0] instanceof Update){
            update = (Update) args[0];
        }
        else {
            throw new RuntimeException("This update handler must take a non-null Update value as its first argument");
        }

        if (args[1] instanceof AbstractBot<?>){
            bot = (AbstractBot<?>) args[1];
        }
        else {
            throw new RuntimeException("This update handler must take a non-null AbstractBot value as its second argument");
        }
        updateManager.processUpdate(update, bot);
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
    private static Object createObject(@NotNull Class<?> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    /**
     * Retrieves the configuration object created from the specified class.
     * If the {@link BotConfiguration#staticBuild()} is set to true, this method will return null.
     * Otherwise, it will return the initialized configuration object during the construction of the
     * {@link ConfigurationProcessor} instance.
     *
     * @return the configuration object created from the specified class.
     * If the {@link BotConfiguration#staticBuild()} is set to true or if the specified class is not
     * assignable from {@link Bot}, this method will return null.
     *
     * @see BotConfiguration#staticBuild()
     * @see Bot
     * @see ConfigurationProcessor
     */
    public Object getConfigurationObject() {
        return configurationObject;
    }

    /**
     * Retrieves the class specified in the constructor of the ConfigurationProcessor.
     * This class is used to create configuration objects based on the specified class and its annotations.
     *
     * @return the class specified in the constructor of the ConfigurationProcessor.
     *
     * @see ConfigurationProcessor#ConfigurationProcessor(Class, AbstractUpdateManager, AbstractMethodMapper)
     * @see BotConfiguration
     */
    public Class<?> getConfigurationClass() {
        return configurationClass;
    }

    
    /**
     * Retrieves the set of bot methods associated with the configuration.
     * The bot methods are mapped from the classes and methods annotated with the {@link Feedback} or {@link Command} annotation.
     *
     * @return the set of bot methods associated with the configuration.
     * The returned {@link BotMethodSet} object contains all the bot methods that have been mapped from the specified classes.
     *
     * @see BotMethodSet
     * @see Feedback
     * @see Command
     * @see MethodMapper
     */
    public BotMethodSet getMethodSet() {
        return methodSet;
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
