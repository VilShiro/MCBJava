package org.fbs.mcb.data.client;

/**
 * Represents the strategies to handle a timeout event in a client-server communication.
 */
public enum OnTimeoutStrategy {

    /**
     * Throws an exception when a timeout occurs.
     */
    THROW_EXCEPTION,

    /**
     * Ends the communication when a timeout occurs.
     */
    END

}
