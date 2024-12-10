package org.fbs.mcb.core.data.client;

import static org.fbs.mcb.core.form.Bot.getLOGGER;

public class ClientThread extends Thread{

    private final String userId;

    public ClientThread(String userId){
        this.userId = userId;
        getLOGGER().trace("ClientThread obj was created with chat id: {}", userId);
        start();
    }

    public String getUserId() {
        return userId;
    }
}
