package org.fbs.mcb.util.base;

import org.fbs.mcb.util.ConfigurationProcessor;

/**
 * This is an abstract class that provides a basic structure for managing updates.
 * It contains a {@link ConfigurationProcessor} instance for processing update-related tasks.
 */
public abstract class AbstractUpdateManager {
    
    /**
     * The {@link ConfigurationProcessor} instance used for processing update-related tasks.
     */
    private ConfigurationProcessor processor;

    /**
     * Retrieves the {@link ConfigurationProcessor} instance.
     *
     * @return the {@link ConfigurationProcessor} instance
     */
    public ConfigurationProcessor getProcessor() {
        return processor;
    }

    /**
     * Sets the {@link ConfigurationProcessor} instance.
     *
     * @param processor the {@link ConfigurationProcessor} instance to be set
     */
    public void setProcessor(ConfigurationProcessor processor) {
        this.processor = processor;
    }
    
    /**
     * This method is intended to be overridden by subclasses to process update-related tasks.
     *
     * @param args an array of objects representing the arguments for the update processing
     */
    public abstract void processUpdate(Object ... args);
    
}
