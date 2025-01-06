package org.fbs.mcb.data;

import org.fbs.mcb.annotation.IgnoreUpdateType;

/**
 * Represents different types of data that can be ignored in the application.
 * This enum is used to define the various conditions under which certain data should be ignored.
 * @see IgnoreUpdateType
 */
public enum IgnoreType {
    /**
     * Represents a command that should be ignored.
     */
    COMMAND,

    /**
     * Represents a message that should be ignored.
     */
    MESSAGE,

    /**
     * Represents entities (e.g., users, groups) that should be ignored.
     */
    ENTITIES,

    /**
     * Represents the start event that should be ignored.
     */
    START,

    /**
     * Represents a callback query that should be ignored.
     */
    CALLBACK_QUERY,

    /**
     * Represents an inline query that should be ignored.
     */
    INLINE_QUERY
}
