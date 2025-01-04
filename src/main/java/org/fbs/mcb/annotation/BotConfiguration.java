package org.fbs.mcb.annotation;

import java.lang.annotation.*;

/**
 * Annotation to configure bot settings.
 * <p> 
 * This annotation is used to define configuration settings for a bot,
 * such as whether commands should be executed in separate threads and
 * what the start command should be.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface BotConfiguration {

    /**
     * Indicates whether the command should be executed in a separate thread.
     */
    boolean threadSeparation() default false;

    /**
     * Specifies the start command for the bot.
     */
    String startCommand() default "/start";

    /**
     * Retrieves the bot token from the annotated class.
     * <p>
     * This method is used to obtain the bot token specified in the {@link BotConfiguration} annotation.
     * The bot token is a unique identifier provided by the Telegram Bot API, which is required for
     * authenticating and interacting with the bot.
     */
    String botToken() default "";

    /**
     * Specifies whether double dispatch should be used for command handling.
     * <p>
     * Double dispatch is a design pattern that allows a method to be called on an object,
     * and the appropriate method implementation to be selected based on the runtime type of the object.
     * In the context of this bot, double dispatch can be used to handle different types of commands
     * (e.g., text commands, callback queries) in a more efficient and flexible manner.
     * <p>
     * By default, double dispatch is disabled. If enabled, the bot will use double dispatch to handle commands,
     * allowing for more specific and efficient command processing.
     */
    boolean doubleDispatch() default false;

    /**
     * Specifies whether the bot should be built statically.
     * <p>
     * Static build configuration allows the bot to be compiled and packaged into a single executable file,
     * eliminating the need for a separate runtime environment. This can be useful for deploying the bot
     * in environments where a runtime environment is not available or not desirable.
     * <p>
     * By default, static build is disabled. If enabled, the bot will be built as a static executable,
     * reducing the overall size and complexity of the deployment process.
     */
    boolean staticBuild() default false;
}