package org.fbs.mcb.data.entity;

import org.fbs.mcb.util.ConfigurationProcessor;

public abstract class AbstractBot <T>{

    private T bot;

    private ConfigurationProcessor configuration;

    protected void setConfiguration(Object ... args){}

    protected abstract void setUpdateListeners();

    public T getBot(){
        return bot;
    }

    protected void initBot(Object ... args){}

    protected void setBot(T bot){
        this.bot = bot;
    }

    protected ConfigurationProcessor getConfiguration(){
        return configuration;
    }

    protected void setConfiguration(ConfigurationProcessor configuration){
        this.configuration = configuration;
    }

}
