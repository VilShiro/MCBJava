package org.fbs.mcb.data.entity;

import org.fbs.mcb.util.ConfigurationProcessor;

/**
 * This is an abstract class representing a bot entity. It provides common functionality for bot-related operations.
 *
 * @param <T> The type of the bot.
 */
public abstract class AbstractBot <T>{

    private T bot;

    private ConfigurationProcessor configuration;

    /**
     * Sets the configuration for the bot.
     *
     * @param args Variable number of arguments for configuration.
     */
    protected abstract void setConfiguration(Object ... args);

    /**
     * Returns the bot instance.
     *
     * @return The bot instance.
     */
    public T getBot(){
        return bot;
    }

    /**
     * Initializes the bot with the given arguments.
     *
     * @param args Variable number of arguments for initialization.
     */
    protected abstract void initBot(Object ... args);

    /**
     * Sets the bot instance.
     *
     * @param bot The bot instance to be set.
     */
    protected void setBot(T bot){
        this.bot = bot;
    }

    /**
     * Returns the configuration processor for the bot.
     *
     * @return The configuration processor.
     */
    protected ConfigurationProcessor getConfiguration(){
        return configuration;
    }

    /**
     * Sets the configuration processor for the bot.
     *
     * @param configuration The configuration processor to be set.
     */
    protected void setConfiguration(ConfigurationProcessor configuration){
        this.configuration = configuration;
    }

}
