package org.fbs.mcb.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used to mark methods that handle feedback messages in a Telegram bot.
 * It provides a way to specify the type of feedback that a method can handle.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Feedback {

    /**
     * Specifies the type of feedback that the annotated method can handle.
     */
    String type();
}
