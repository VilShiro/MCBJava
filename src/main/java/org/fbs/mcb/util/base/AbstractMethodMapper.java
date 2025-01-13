package org.fbs.mcb.util.base;

import org.fbs.mcb.data.BotMethod;
import org.fbs.mcb.util.ConfigurationProcessor;

import java.util.List;

/**
 * This is an abstract class that serves as a base for mapping bot methods.
 * It provides a method for reading bot methods and a mechanism for accessing the configuration processor.
 */
public abstract class AbstractMethodMapper {
    
    private ConfigurationProcessor processor;

    /**
     * Returns the configuration processor associated with this instance.
     *
     * @return the configuration processor
     */
    public ConfigurationProcessor getProcessor() {
        return processor;
    }

    /**
     * Sets the configuration processor for this instance.
     *
     * @param processor the configuration processor to set
     */
    public void setProcessor(ConfigurationProcessor processor) {
        this.processor = processor;
    }

    /**
     * Reads and returns a list of bot methods based on the provided arguments.
     * The specific implementation of this method is left to subclasses.
     *
     * @param args the arguments to be used for reading bot methods
     * @return a list of bot methods
     */
    public abstract List<BotMethod> readMethods(Object ... args);
    
}
