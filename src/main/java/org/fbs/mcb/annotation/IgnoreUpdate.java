package org.fbs.mcb.annotation;

import org.fbs.mcb.data.IgnoreType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface IgnoreUpdate {

    IgnoreType[] value();

}
