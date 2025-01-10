package org.fbs.mcb.data.entity;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import org.fbs.mcb.util.ConfigurationProcessor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public abstract class Bot {

    private final TelegramBot bot;

    private ConfigurationProcessor configuration;

    private String startCommand = "";

    protected Bot(@NotNull String botToken, @NotNull String startCommandRaw){
        StringBuilder stringBuilder = new StringBuilder();
        String startCommand;

        if (startCommandRaw.toCharArray()[0] == '/'){
            for (int i = 1; i < startCommandRaw.length(); i++) {
                stringBuilder.append(startCommandRaw.toCharArray()[i]);
            }
            startCommand = stringBuilder.toString();
        }
        else {
            startCommand = startCommandRaw;
        }

        bot = new TelegramBot(botToken);
        this.startCommand = startCommand;
        setListener();
    }

    protected Bot(@NotNull String botToken){
        bot = new TelegramBot(botToken);
        startCommand = "/start";
        setListener();
    }

    protected Bot(Class<?> configurationClass){
        setConfiguration(configurationClass);
        if (Objects.equals(configuration.getBotToken(), "")){
            throw new RuntimeException("Configuration bot token is null or empty");
        }
        bot = new TelegramBot(configuration.getBotToken());
        setListener();
    }

    private void setListener(){
        bot.setUpdatesListener(list -> {

            Update lastUpdate = list.get(list.size()-1);
            if (configuration != null) {
                if (!configuration.isDoubleDispatch()) {
                    try {
                        configuration.handle(lastUpdate, this);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                } else {

                    try {
                        configuration.handle(lastUpdate, this);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    updateParse(lastUpdate);

                    if (lastUpdate.message() != null && !Objects.equals(lastUpdate.message().text(), "")) {
                        Message message = lastUpdate.message();
                        messageParse(message);
                        try {
                            if (message.entities().length > 0) {
                                MessageEntity[] entities = message.entities();
                                entitiesParse(entities, message);
                                if (message.text().contains(startCommand)) {
                                    onStartCommand(message);
                                }
                            }
                        } catch (NullPointerException ignored) {
                        }
                    } else if (lastUpdate.callbackQuery() != null) {
                        CallbackQuery query = lastUpdate.callbackQuery();
                        callbackQueryParse(query);
                    } else if (lastUpdate.inlineQuery() != null) {
                        InlineQuery query = lastUpdate.inlineQuery();
                        inlineQueryParse(query);
                    }
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;

            }, e -> {
                if (e.response() != null) {
                    e.response().errorCode();
                    e.response().description();
                }
            }
        );
    }

    public TelegramBot getBot() {
        return bot;
    }

    public void setConfiguration(Class<?> clazz){
        try {
            configuration = new ConfigurationProcessor(clazz);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        startCommand = configuration.getStartCommand();
    }

    protected void onStartCommand(Message message){}

    protected void updateParse(Update update){}

    protected void callbackQueryParse(CallbackQuery query){}

    protected void entitiesParse(MessageEntity[] messageEntities, Message message){}

    protected void messageParse(Message message){}

    protected void inlineQueryParse(InlineQuery query){}

}
