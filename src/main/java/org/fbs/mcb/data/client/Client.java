package org.fbs.mcb.data.client;

import com.pengrad.telegrambot.model.User;
import org.fbs.mcb.data.Action;

/**
 * Represents a client in the application.
 */
public class Client {

    private final User user;

    private final ClientThreads parsingThreadsUpdates;
    private final ClientThreads parsingThreadsMessages;
    private final ClientThreads parsingThreadsEntities;
    private final ClientThreads parsingThreadsCallbackBack;
    private final ClientThreads parsingThreadsInline;
    private final ClientThreads parsingThreadsStart;

    /**
     * Constructs a new Client object.
     *
     * @param user The Telegram user associated with this client.
     */
    public Client(User user){
        this.user = user;
        parsingThreadsUpdates = new ClientThreads(user.id());
        parsingThreadsMessages = new ClientThreads(user.id());
        parsingThreadsEntities = new ClientThreads(user.id());
        parsingThreadsCallbackBack = new ClientThreads(user.id());
        parsingThreadsInline = new ClientThreads(user.id());
        parsingThreadsStart = new ClientThreads(user.id());
    }

    /**
     * Returns the Telegram user associated with this client.
     *
     * @return The Telegram user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the client threads associated with the given action.
     *
     * @param action The action to get the threads for.
     * @return The client threads associated with the given action.
     */
    public ClientThreads getThreadsByAction(Action action){
        return switch (action) {
            case UPDATE -> parsingThreadsUpdates;
            case MESSAGE -> parsingThreadsMessages;
            case ENTITIES -> parsingThreadsEntities;
            case CALLBACK_QUERY -> parsingThreadsCallbackBack;
            case INLINE_QUERY -> parsingThreadsInline;
            case START -> parsingThreadsStart;
        };
    }

    /**
     * Returns a client thread associated with the given action and runnable.
     * Removes all threads from the corresponding action and adds the given runnable.
     *
     * @param action The action to get the thread for.
     * @param runnable The runnable to be executed in the thread.
     * @return The client thread associated with the given action and runnable.
     */
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

    /**
     * Returns the ID of the Telegram user associated with this client.
     *
     * @return The ID of the Telegram user.
     */
    public long getId(){
        return user.id();
    }

}
