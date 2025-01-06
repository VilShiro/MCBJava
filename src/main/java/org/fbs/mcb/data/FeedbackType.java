package org.fbs.mcb.data;

/**
 * Represents different types of feedback that can be received from a Telegram bot.
 * This enum is used to categorize the type of feedback and perform specific actions accordingly.
 */
public enum FeedbackType {
    /**
     * Represents an update received from Telegram.
     */
    UPDATE,

    /**
     * Represents a message sent by a user to the bot.
     */
    MESSAGE,

    /**
     * Represents entities (e.g., URLs, mentions, hashtags) found in a message.
     */
    ENTITIES,

    /**
     * Represents a callback query sent by a user to the bot.
     */
    CALLBACK_QUERY,

    /**
     * Represents an inline query sent by a user to the bot.
     */
    INLINE_QUERY,

    /**
     * Represents a start command sent by a user to the bot.
     */
    START
}
