package org.fbs.mcb.data.client;

import com.pengrad.telegrambot.model.User;
import org.fbs.mcb.data.MethodType;

/**
 * Represents a client in the application.
 */
public class Client {

    private final User user;

    private final ClientThreadSet parsingThreadSetUpdates;
    private final ClientThreadSet parsingThreadSetMessages;
    private final ClientThreadSet parsingThreadSetEntities;
    private final ClientThreadSet parsingThreadSetCallbackBack;
    private final ClientThreadSet parsingThreadSetInline;
    private final ClientThreadSet parsingThreadSetStart;

    /**
     * Constructs a new Client object.
     *
     * @param user The Telegram user associated with this client.
     */
    public Client(User user){
        this.user = user;
        parsingThreadSetUpdates = new ClientThreadSet(user.id());
        parsingThreadSetMessages = new ClientThreadSet(user.id());
        parsingThreadSetEntities = new ClientThreadSet(user.id());
        parsingThreadSetCallbackBack = new ClientThreadSet(user.id());
        parsingThreadSetInline = new ClientThreadSet(user.id());
        parsingThreadSetStart = new ClientThreadSet(user.id());
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
     * @param type The action to get the threads for.
     * @return The client threads associated with the given action.
     */
    public ClientThreadSet getThreadSetByAction(MethodType type){
        return switch (type) {
            case UPDATE -> parsingThreadSetUpdates;
            case MESSAGE -> parsingThreadSetMessages;
            case ENTITIES -> parsingThreadSetEntities;
            case CALLBACK_QUERY -> parsingThreadSetCallbackBack;
            case INLINE_QUERY -> parsingThreadSetInline;
            case START -> parsingThreadSetStart;
            case COMMAND -> null;
        };
    }

    /**
     * Returns a client thread associated with the given action and runnable.
     * Removes all threads from the corresponding action and adds the given runnable.
     *
     * @param type The action to get the thread for.
     * @param runnable The runnable to be executed in the thread.
     * @return The client thread associated with the given action and runnable.
     */
    public ClientThread getThreadByAction(MethodType type, Runnable runnable){
        switch (type){
            case UPDATE:
                parsingThreadSetUpdates.removeAllThreads();
                parsingThreadSetUpdates.addClientThread(runnable);
                return parsingThreadSetUpdates.get(parsingThreadSetUpdates.size() - 1);
            case MESSAGE:
                parsingThreadSetMessages.removeAllThreads();
                parsingThreadSetMessages.addClientThread(runnable);
                return parsingThreadSetMessages.get(parsingThreadSetMessages.size() - 1);
            case ENTITIES:
                parsingThreadSetEntities.removeAllThreads();
                parsingThreadSetEntities.addClientThread(runnable);
                return parsingThreadSetEntities.get(parsingThreadSetEntities.size() - 1);
            case CALLBACK_QUERY:
                parsingThreadSetCallbackBack.removeAllThreads();
                parsingThreadSetCallbackBack.addClientThread(runnable);
                return parsingThreadSetCallbackBack.get(parsingThreadSetCallbackBack.size() - 1);
            case INLINE_QUERY:
                parsingThreadSetInline.removeAllThreads();
                parsingThreadSetInline.addClientThread(runnable);
                return parsingThreadSetInline.get(parsingThreadSetInline.size() - 1);
            case START:
                parsingThreadSetStart.removeAllThreads();
                parsingThreadSetStart.addClientThread(runnable);
                return parsingThreadSetStart.get(parsingThreadSetStart.size() - 1);
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
