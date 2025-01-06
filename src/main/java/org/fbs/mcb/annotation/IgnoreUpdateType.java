package org.fbs.mcb.annotation;

import org.fbs.mcb.data.IgnoreType;

import java.lang.annotation.*;

/**
 * This annotation is used to mark classes or methods that should be ignored during the update process.
 * It specifies the types of updates that should be ignored for the annotated element.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface IgnoreUpdateType {

    /**
     * An array of {@link IgnoreType} values representing the types of updates to be ignored.
     */
    IgnoreType[] value();
}
