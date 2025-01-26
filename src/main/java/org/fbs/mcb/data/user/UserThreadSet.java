package org.fbs.mcb.data.user;

import java.util.ArrayList;

/**
 * This class manages a collection of client threads associated with a specific user.
 */
public class UserThreadSet {

    private final long userId;
    private final ArrayList<UserThread> threads = new ArrayList<>();

    /**
     * Constructs a new instance of ClientThreads for the given user ID.
     *
     * @param userId The unique identifier of the user.
     */
    public UserThreadSet(long userId){
        this.userId = userId;
    }

    /**
     * Adds a new client thread to the collection. The new thread is created from the given Runnable.
     *
     * @param runnable The Runnable to be executed by the new client thread.
     */
    public void addClientThread(Runnable runnable){
        threads.add(new UserThread(userId + ""){
            @Override
            public void run() {
                runnable.run();
            }
        });
    }

    /**
     * Retrieves the client thread at the specified index.
     *
     * @param i The index of the client thread to retrieve.
     * @return The client thread at the specified index.
     */
    public UserThread get(int i){
        return threads.get(i);
    }

    /**
     * Retrieves the user ID associated with this collection of client threads.
     *
     * @return The user ID.
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Removes the specified client thread from the collection.
     *
     * @param thread The client thread to remove.
     */
    public void removeClientThread(UserThread thread){
        threads.remove(thread);
    }

    /**
     * Removes the client thread at the specified index from the collection.
     *
     * @param i The index of the client thread to remove.
     */
    public void removeClientThread(int i){
        threads.remove(i);
    }

    /**
     * Removes all client threads from the collection.
     */
    public void removeAllThreads(){
        threads.clear();
    }

    /**
     * Retrieves the number of client threads in the collection.
     *
     * @return The number of client threads.
     */
    public int size(){
        return threads.size();
    }

}
