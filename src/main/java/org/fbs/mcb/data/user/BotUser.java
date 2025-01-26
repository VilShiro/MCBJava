package org.fbs.mcb.data.user;

import com.pengrad.telegrambot.model.User;

/**
 * Represents a user in the application.
 */
public class BotUser {

    private final User user;

    private final UserThreadSet threadSet;

    private final TaskDeque tasks = new TaskDeque();

    /**
     * Constructs a new BotUser object.
     *
     * @param user The Telegram user associated with this user.
     */
    public BotUser(User user){
        this.user = user;
        threadSet = new UserThreadSet(user.id());
    }

    /**
     * Returns the Telegram user associated with this user.
     *
     * @return The Telegram user.
     */
    final public User getUser() {
        return user;
    }

    /**
     * Adds a task to the user's task queue.
     *
     * <p>This method adds a new task to the user's task queue based on the specified add strategy.
     * The task will be executed in the order it is added, according to the chosen strategy.
     *
     * @param task The task to be added to the queue. This should be an instance of {@link BotRunnable}.
     * @param addStrategy The strategy to determine where in the queue the task should be added.
     *                    This should be an instance of {@link OnAddStrategy}.
     *
     * @see BotRunnable
     * @see OnAddStrategy
     */
    final public void addTask(BotRunnable task, OnAddStrategy addStrategy){
        tasks.add(task, addStrategy);
        
    }

    /**
     * Returns the set of threads associated with the user.
     *
     * <p>This method retrieves the set of threads that are currently being managed for the user.
     * The returned set allows for the addition, removal, and retrieval of threads associated with the user.
     *
     * @return The set of threads associated with the user.
     *
     * @see UserThreadSet
     */
    final public UserThreadSet getThreadSet(){
        return threadSet;
    }

    /**
     * Returns the ID of the Telegram user associated with this user.
     *
     * @return The ID of the Telegram user.
     */
    final public long getId(){
        return user.id();
    }

}
