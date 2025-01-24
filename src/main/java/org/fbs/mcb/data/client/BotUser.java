package org.fbs.mcb.data.client;

import com.pengrad.telegrambot.model.User;

/**
 * Represents a user in the application.
 */
public class BotUser {

    private final User user;

    private final UserThreadSet threadSet;

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
    public User getUser() {
        return user;
    }


    public UserThreadSet getThreadSet(){
        return threadSet;
    }

    /**
     * Returns the ID of the Telegram user associated with this user.
     *
     * @return The ID of the Telegram user.
     */
    public long getId(){
        return user.id();
    }

}
