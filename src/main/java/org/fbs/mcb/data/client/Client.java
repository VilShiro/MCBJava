package org.fbs.mcb.data.client;

import com.pengrad.telegrambot.model.User;
import org.fbs.mcb.data.Action;

public class Client {

    private final User user;

    private final ClientThreads parsingThreadsUpdates;
    private final ClientThreads parsingThreadsMessages;
    private final ClientThreads parsingThreadsEntities;
    private final ClientThreads parsingThreadsCallbackBack;
    private final ClientThreads parsingThreadsInline;
    private final ClientThreads parsingThreadsStart;

    public Client(User user){
        this.user = user;
        parsingThreadsUpdates = new ClientThreads(user.id());
        parsingThreadsMessages = new ClientThreads(user.id());
        parsingThreadsEntities = new ClientThreads(user.id());
        parsingThreadsCallbackBack = new ClientThreads(user.id());
        parsingThreadsInline = new ClientThreads(user.id());
        parsingThreadsStart = new ClientThreads(user.id());
    }

    public User getUser() {
        return user;
    }

    public ClientThreads getThreadsByAction(Action action){
        switch (action){
            case UPDATE:
                return parsingThreadsUpdates;
            case MESSAGE:
                return parsingThreadsMessages;
            case ENTITIES:
                return parsingThreadsEntities;
            case CALLBACK_QUERY:
                return parsingThreadsCallbackBack;
            case INLINE_QUERY:
                return parsingThreadsInline;
            case START:
                return parsingThreadsStart;
        }
        return null;
    }

    public ClientThread getThreadByAction(Action action, Runnable runnable){
        switch (action){
            case UPDATE:
                parsingThreadsUpdates.removeAllThreads();
                parsingThreadsUpdates.addClientThread(runnable);
                return parsingThreadsUpdates.get(parsingThreadsUpdates.size() - 1);
            case MESSAGE:
                parsingThreadsMessages.removeAllThreads();
                parsingThreadsMessages.addClientThread(runnable);
                return parsingThreadsMessages.get(parsingThreadsMessages.size() - 1);
            case ENTITIES:
                parsingThreadsEntities.removeAllThreads();
                parsingThreadsEntities.addClientThread(runnable);
                return parsingThreadsEntities.get(parsingThreadsEntities.size() - 1);
            case CALLBACK_QUERY:
                parsingThreadsCallbackBack.removeAllThreads();
                parsingThreadsCallbackBack.addClientThread(runnable);
                return parsingThreadsCallbackBack.get(parsingThreadsCallbackBack.size() - 1);
            case INLINE_QUERY:
                parsingThreadsInline.removeAllThreads();
                parsingThreadsInline.addClientThread(runnable);
                return parsingThreadsInline.get(parsingThreadsInline.size() - 1);
            case START:
                parsingThreadsStart.removeAllThreads();
                parsingThreadsStart.addClientThread(runnable);
                return parsingThreadsStart.get(parsingThreadsStart.size() - 1);
        }
        return null;
    }

    public long getId(){
        return user.id();
    }

}
