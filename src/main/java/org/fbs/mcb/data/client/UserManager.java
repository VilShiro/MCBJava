package org.fbs.mcb.data.client;

/**
 * This interface provides methods for managing user-related operations.
 */
public interface UserManager {
    
    /**
     * Adds one or more objects to the user management system.
     *
     * @param args Variable number of objects to be added.
     *             The type and number of arguments are not restricted.
     */
    void add(Object ... args);
    
    /**
     * Performs any necessary actions to finalize user management operations.
     * This method should be called when the user management process is completed.
     */
    void end();
    
    /**
     * Handles the case when a user management operation times out.
     * This method should be called when the user management process exceeds a specified time limit.
     */
    void timeout();
    
}
