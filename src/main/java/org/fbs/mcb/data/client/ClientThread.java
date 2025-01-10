package org.fbs.mcb.data.client;

/**
 * This class represents a client thread that extends the standard Java Thread class.
 * It is responsible for handling client-related operations.
 */
public class ClientThread extends Thread {

    /**
     * The unique identifier of the client associated with this thread.
     */
    private final String userId;

    /**
     * Constructs a new ClientThread object with the given user ID.
     * The thread is automatically started upon creation.
     *
     * @param userId The unique identifier of the client associated with this thread.
     */
    public ClientThread(String userId) {
        this.userId = userId;
        start();
    }

    /**
     * Returns the unique identifier of the client associated with this thread.
     *
     * @return The unique identifier of the client.
     */
    public String getUserId() {
        return userId;
    }
}
