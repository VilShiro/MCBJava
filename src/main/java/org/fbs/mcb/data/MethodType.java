package org.fbs.mcb.data;

/**
 * Represents different types of methods used in the MCBJava-core-quickstart project.
 * This enum is used to differentiate between different types of bot API methods.
 */
public enum MethodType {
    /**
     * Represents an update method.
     */
    UPDATE,

    /**
     * Represents a message method.
     */
    MESSAGE,

    /**
     * Represents an entities method.
     */
    ENTITIES,

    /**
     * Represents a callback query method.
     */
    CALLBACK_QUERY,

    /**
     * Represents an inline query method.
     */
    INLINE_QUERY,

    /**
     * Represents a start method.
     */
    START
}
