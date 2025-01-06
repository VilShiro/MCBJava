package org.fbs.mcb.data;

import org.fbs.mcb.annotation.IgnoreUpdateSender;

/**
 * Enum representing different types of senders to be ignored in the application.
 * This enum is used to define the criteria for ignoring messages from specific senders.
 * @see IgnoreUpdateSender
 */
public enum IgnoreSender {

    /**
     * Represents administrators.
     * Messages from administrators will be ignored.
     */
    ADMINS,

    /**
     * Represents bots.
     * Messages from bots will be ignored.
     */
    BOTS,

    /**
     * Represents default users.
     * Messages from default users will be ignored.
     */
    DEFAULT_USERS

}
