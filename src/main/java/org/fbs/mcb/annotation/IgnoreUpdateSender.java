package org.fbs.mcb.annotation;

import org.fbs.mcb.data.IgnoreSender;

import java.lang.annotation.*;

/**
 * This annotation is used to mark methods or classes that should be ignored when updating the sender in the game.
 * It is used in conjunction with the {@link IgnoreSender} enum to specify which sender types should be ignored.
 *
 * <p>To use this annotation, simply add it to the method or class from whom you want to ignore incoming updates from the specified senders. The {@link IgnoreSender} enum
 * should be used to specify the sender types to be ignored. Multiple sender types can be specified by using the
 * array syntax.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface IgnoreUpdateSender {

    /**
     * An array of {@link IgnoreSender} enum values representing the sender types to be ignored.
     */
    IgnoreSender[] value();

}
