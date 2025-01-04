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
    String command();

    /**
     * Indicates whether this command is restricted to admin users only.
     */
    boolean onlyForAdmin() default false;
}