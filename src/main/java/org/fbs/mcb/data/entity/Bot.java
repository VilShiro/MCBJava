package org.fbs.mcb.data.entity;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import org.fbs.mcb.util.ConfigurationProcessor;
import org.fbs.mcb.util.MethodMapper;
import org.fbs.mcb.util.UpdateManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class Bot extends AbstractBot<TelegramBot>{

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
        initBot(botToken);
        setUpdateListeners();
        this.startCommand = startCommand;
    }

    protected Bot(@NotNull String botToken){
        initBot(botToken);
        setUpdateListeners();
        startCommand = "/start";
    }

    protected Bot(Class<?> configurationClass){
        setConfiguration(configurationClass);
        if (Objects.equals(getConfiguration().getBotToken(), "")){
            throw new RuntimeException("Configuration bot token is null or empty");
        }
        initBot(configurationClass);
        setUpdateListeners();
    }

    protected void setUpdateListeners() {
        getBot().setUpdatesListener(list -> {
            Update lastUpdate = list.get(list.size()-1);
            if (getConfiguration() != null) {
                if (!getConfiguration().isDoubleDispatch()) {
                    getConfiguration().handle(lastUpdate, this);
                } else {
                    getConfiguration().handle(lastUpdate, this);
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

    @Override
    protected void initBot(Object... args) {
        if (args[0] instanceof Class<?>){
            setConfiguration(args[0]);
            setBot(new TelegramBot(getConfiguration().getBotToken()));
        }
        else if (args[0] instanceof String) {
            setBot(new TelegramBot((String) args[0]));
        }
    }

    @Override
    public void setConfiguration(Object ... args){
        if (args[0] instanceof Class<?>) {
            try {
                setConfiguration(new ConfigurationProcessor((Class<?>) args[0], new UpdateManager(), new MethodMapper()));
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            startCommand = getConfiguration().getStartCommand();
        }
    }

    protected void onStartCommand(Message message){}

    protected void updateParse(Update update){}

    protected void callbackQueryParse(CallbackQuery query){}

    protected void entitiesParse(MessageEntity[] messageEntities, Message message){}

    protected void messageParse(Message message){}

    protected void inlineQueryParse(InlineQuery query){}

}
