package org.fbs.mcb.util.base;

/**
 * This is an abstract class that provides a basic structure for processing configurations.
 * It defines an abstract method called {@link #handle(Object...)} which should be implemented by subclasses.
 */
public abstract class AbstractConfigurationProcessor {

    /**
     * This method should be implemented by subclasses to handle configurations.
     * It accepts a variable number of arguments of type {@link Object}.
     *
     * @param args The arguments to be processed.
     */
    public abstract void handle(Object ... args);

}