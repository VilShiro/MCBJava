package org.fbs.mcb.data.client;

public class ClientThread extends Thread{

    private final String userId;

    public ClientThread(String userId){
        this.userId = userId;
        start();
    }

    public String getUserId() {
        return userId;
    }
}
