package org.fbs.mcb.data.entity;

import com.pengrad.telegrambot.model.*;
import org.fbs.mcb.data.client.BotUser;
import org.fbs.mcb.data.client.UserThreadSet;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class MultiClientBot extends Bot {

    private final ArrayList<BotUser> botUsers = new ArrayList<>();

    protected MultiClientBot(String botToken, String startCommandRaw) {
        super(botToken, startCommandRaw);
    }

    protected MultiClientBot(@NotNull String botToken) {
        super(botToken);
    }

    protected MultiClientBot(Class<?> configurationClass) {
        super(configurationClass);
    }

    protected UserThreadSet getThreadsByThreadsArr(long userId, ArrayList<UserThreadSet> threadsArrayList){
        for (UserThreadSet threads : threadsArrayList) {
            if (threads.getUserId() == userId) {
                return threads;
            }
        }
        return null;
    }

    protected UserThreadSet getThreadsByAction(long userId){
        return Objects.requireNonNull(getClientById(userId)).getThreadSet();
    }

    protected UserThreadSet getThreadsOnUpdate(long userId, ArrayList<UserThreadSet> threads){
        UserThreadSet userThreadSet = getThreadsByThreadsArr(userId, threads);

        if (userThreadSet == null) {
            threads.add(new UserThreadSet(userId));
            userThreadSet = getThreadsByThreadsArr(userId, threads);
        }

        if (userThreadSet.getUserId() == userId) {
            for (int i = 0; i < userThreadSet.size(); i++) {
                userThreadSet.removeClientThread(i);
            }
        }

        return userThreadSet;
    }

    protected void callbackQueryParse(CallbackQuery query, BotUser botUser) {}

    protected void entitiesParse(MessageEntity[] messageEntities, Message message, BotUser botUser) {}

    protected void inlineQueryParse(InlineQuery query, BotUser botUser) {}

    protected void messageParse(Message message, BotUser botUser) {}

    protected void onStartCommand(Message message, BotUser botUser) {}

    protected void updateParse(Update update, BotUser botUser) {}

    @Deprecated
    @Override
    protected void callbackQueryParse(CallbackQuery query){
        BotUser botUser = getClientById(query.from().id());
        if (botUser == null) {
            botUser = new BotUser(query.from());
            botUsers.add(botUser);
        }
        callbackQueryParse(query, botUser);
    }

    @Deprecated
    @Override
    protected void entitiesParse(MessageEntity[] messageEntities, Message message){
        BotUser botUser = getClientById(message.from().id());
        if (botUser == null) {
            botUser = new BotUser(message.from());
            botUsers.add(botUser);
        }
        entitiesParse(messageEntities, message, botUser);
    }

    @Deprecated
    @Override
    protected void inlineQueryParse(InlineQuery query){
        BotUser botUser = getClientById(query.from().id());
        if (botUser == null) {
            botUser = new BotUser(query.from());
            botUsers.add(botUser);
        }
        inlineQueryParse(query, getClientById(query.from().id()));
    }

    @Deprecated
    @Override
    protected void messageParse(Message message){
        BotUser botUser = getClientById(message.from().id());
        if (botUser == null) {
            botUser = new BotUser(message.from());
            botUsers.add(botUser);
        }
        messageParse(message, getClientById(message.from().id()));
    }

    @Deprecated
    @Override
    protected void onStartCommand(Message message){
        BotUser botUser = getClientById(message.from().id());
        if (botUser == null) {
            botUser = new BotUser(message.from());
            botUsers.add(botUser);
        }
        onStartCommand(message, getClientById(message.from().id()));
    }

    @Deprecated
    @Override
    protected void updateParse(Update update){
        BotUser botUser = getClientById(update.myChatMember().from().id());
        if (botUser == null) {
            botUser = new BotUser(update.myChatMember().from());
            botUsers.add(botUser);
        }
        updateParse(update, botUser);
    }

    private BotUser getClientById(long id){
        for (BotUser botUser : botUsers) {
            if (botUser.getId() == id) {
                return botUser;
            }
        }
        return null;
    }

}

