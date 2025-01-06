package org.fbs.mcb.data.entity;

import com.pengrad.telegrambot.model.*;
import org.fbs.mcb.data.Action;
import org.fbs.mcb.data.client.Client;
import org.fbs.mcb.data.client.ClientThreads;

import java.util.ArrayList;
import java.util.Objects;

public abstract class MultiClientBot extends Bot {

    private final ArrayList<Client> clients = new ArrayList<>();

    protected MultiClientBot(String botToken, String startCommandRaw) {
        super(botToken, startCommandRaw);
    }

    protected ClientThreads getThreadsByThreadsArr(long userId, ArrayList<ClientThreads> threadsArrayList){
        for (ClientThreads threads : threadsArrayList) {
            if (threads.getUserId() == userId) {
                return threads;
            }
        }
        return null;
    }

    protected ClientThreads getThreadsByAction(long userId, Action action){
        return Objects.requireNonNull(getClientById(userId)).getThreadsByAction(action);
    }

    protected ClientThreads getThreadsOnUpdate(long userId, ArrayList<ClientThreads> threads){
        ClientThreads clientThreads = getThreadsByThreadsArr(userId, threads);

        if (clientThreads == null) {
            threads.add(new ClientThreads(userId));
            clientThreads = getThreadsByThreadsArr(userId, threads);
        }

        if (clientThreads.getUserId() == userId) {
            for (int i = 0; i < clientThreads.size(); i++) {
                clientThreads.removeClientThread(i);
            }
        }

        return clientThreads;
    }

    protected abstract void callbackQueryParse(CallbackQuery query, Client client);

    protected abstract void entitiesParse(MessageEntity[] messageEntities, Message message, Client client) ;

    protected abstract void inlineQueryParse(InlineQuery query, Client client);

    protected abstract void messageParse(Message message, Client client);

    protected abstract void onStartCommand(Message message, Client client);

    protected abstract void updateParse(Update update, Client client);

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
