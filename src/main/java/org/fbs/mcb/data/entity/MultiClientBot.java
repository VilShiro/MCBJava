package org.fbs.mcb.data.entity;

import com.pengrad.telegrambot.model.*;
import org.fbs.mcb.data.MethodType;
import org.fbs.mcb.data.client.Client;
import org.fbs.mcb.data.client.ClientThreadSet;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class MultiClientBot extends Bot {

    private final ArrayList<Client> clients = new ArrayList<>();

    protected MultiClientBot(String botToken, String startCommandRaw) {
        super(botToken, startCommandRaw);
    }

    protected MultiClientBot(@NotNull String botToken) {
        super(botToken);
    }

    protected MultiClientBot(Class<?> configurationClass) {
        super(configurationClass);
    }

    protected ClientThreadSet getThreadsByThreadsArr(long userId, ArrayList<ClientThreadSet> threadsArrayList){
        for (ClientThreadSet threads : threadsArrayList) {
            if (threads.getUserId() == userId) {
                return threads;
            }
        }
        return null;
    }

    protected ClientThreadSet getThreadsByAction(long userId, MethodType type){
        return Objects.requireNonNull(getClientById(userId)).getThreadSetByAction(type);
    }

    protected ClientThreadSet getThreadsOnUpdate(long userId, ArrayList<ClientThreadSet> threads){
        ClientThreadSet clientThreadSet = getThreadsByThreadsArr(userId, threads);

        if (clientThreadSet == null) {
            threads.add(new ClientThreadSet(userId));
            clientThreadSet = getThreadsByThreadsArr(userId, threads);
        }

        if (clientThreadSet.getUserId() == userId) {
            for (int i = 0; i < clientThreadSet.size(); i++) {
                clientThreadSet.removeClientThread(i);
            }
        }

        return clientThreadSet;
    }

    protected void callbackQueryParse(CallbackQuery query, Client client) {}

    protected void entitiesParse(MessageEntity[] messageEntities, Message message, Client client) {}

    protected void inlineQueryParse(InlineQuery query, Client client) {}

    protected void messageParse(Message message, Client client) {}

    protected void onStartCommand(Message message, Client client) {}

    protected void updateParse(Update update, Client client) {}

    @Deprecated
    @Override
    protected void callbackQueryParse(CallbackQuery query){
        Client client = getClientById(query.from().id());
        if (client == null) {
            client = new Client(query.from());
            clients.add(client);
        }
        callbackQueryParse(query, client);
    }

    @Deprecated
    @Override
    protected void entitiesParse(MessageEntity[] messageEntities, Message message){
        Client client = getClientById(message.from().id());
        if (client == null) {
            client = new Client(message.from());
            clients.add(client);
        }
        entitiesParse(messageEntities, message, client);
    }

    @Deprecated
    @Override
    protected void inlineQueryParse(InlineQuery query){
        Client client = getClientById(query.from().id());
        if (client == null) {
            client = new Client(query.from());
            clients.add(client);
        }
        inlineQueryParse(query, getClientById(query.from().id()));
    }

    @Deprecated
    @Override
    protected void messageParse(Message message){
        Client client = getClientById(message.from().id());
        if (client == null) {
            client = new Client(message.from());
            clients.add(client);
        }
        messageParse(message, getClientById(message.from().id()));
    }

    @Deprecated
    @Override
    protected void onStartCommand(Message message){
        Client client = getClientById(message.from().id());
        if (client == null) {
            client = new Client(message.from());
            clients.add(client);
        }
        onStartCommand(message, getClientById(message.from().id()));
    }

    @Deprecated
    @Override
    protected void updateParse(Update update){
        Client client = getClientById(update.myChatMember().from().id());
        if (client == null) {
            client = new Client(update.myChatMember().from());
            clients.add(client);
        }
        updateParse(update, client);
    }

    private Client getClientById(long id){
        for (Client client : clients) {
            if (client.getId() == id) {
                return client;
            }
        }
        return null;
    }

}

