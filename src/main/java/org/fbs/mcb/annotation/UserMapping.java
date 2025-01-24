package org.fbs.mcb.annotation;

import org.fbs.mcb.data.client.OnAddStrategy;
import org.fbs.mcb.data.client.OnEndStrategy;
import org.fbs.mcb.data.client.OnTimeoutStrategy;
import org.fbs.mcb.data.client.UserMapper;

import java.lang.annotation.*;

/**
 * This annotation is used to configure the behavior of the UserMapper class.
 * It provides options for handling various events during the mapping process.
 * @see UserMapper
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface UserMapping {

    /**
     * Specifies the strategy to be used when a new user is added.
     * The default strategy is {@link OnAddStrategy#INTERRUPT}.
     */
    OnAddStrategy onAdd() default OnAddStrategy.INTERRUPT;

    /**
     * Specifies the strategy to be used when the mapping process ends.
     * The default strategy is {@link OnEndStrategy#END}.
     */
    OnEndStrategy onEnd() default OnEndStrategy.END;

    /**
     * Specifies the strategy to be used when a timeout occurs during the mapping process.
     * The default strategy is {@link OnTimeoutStrategy#THROW_EXCEPTION}.
     */
    OnTimeoutStrategy onTimeout() default OnTimeoutStrategy.THROW_EXCEPTION;
    
    /**
     * This method returns the timeout value specified in the {@link UserMapping} annotation.
     * If no value is explicitly set, the default value of 0 is returned.
     */
    int timeout() default 0;
    
}
