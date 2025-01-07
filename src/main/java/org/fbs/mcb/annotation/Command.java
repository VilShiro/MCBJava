package org.fbs.mcb.annotation;

import java.lang.annotation.*;

/**
 * Annotation to mark methods as command handlers for a Telegram bot.
 * This annotation is used to define bot commands and their properties.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Command {

    /**
     * Specifies the command string that triggers this handler.
     */
    String value();

    /**
     * Indicates whether the command, when called, can contain a line after itself, according to the standard false
     */
    boolean additionalString() default false;

}