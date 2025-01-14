package org.fbs.mcb.data.client;

/**
 * Represents the strategies for handling the addition of a new client to the system.
 * This enum is used to define the behavior when a client is being added while another
 * client addition process is already in progress.
 */
public enum OnAddStrategy {
    
    /**
     * Interrupts the current client addition process and starts a new one.
     * This strategy is useful when the system needs to prioritize the addition of new clients.
     */
    INTERRUPT,
    
    /**
     * Allows the addition of new clients while another client addition process is in progress.
     * This strategy is useful when the system can handle multiple client additions simultaneously.
     */
    STACK_ADDITION,
    
    /**
     * Ignores the addition of new clients while another client addition process is in progress.
     * This strategy is useful when the system needs to avoid blocking the addition of new clients.
     */
    IGNORING_WHILE_IN_PROGRESS

}