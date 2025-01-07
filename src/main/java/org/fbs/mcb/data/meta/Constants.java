package org.fbs.mcb.data.meta;

import com.pengrad.telegrambot.model.*;
import org.fbs.mcb.data.entity.Bot;
import org.fbs.mcb.util.MethodMapper;

/**
 * This class contains arrays of parameter classes that can accept methods in the configuration depending on their function.
 * @see MethodMapper
 */
final public class Constants {
    
    /**
     * Array of objects representing the parameter types for handling updates.
     */
    public static final Class<?>[] UPDATE_PARAMETERS = new Class<?>[]{
            Update.class,
            Bot.class
    };
    
    /**
     * Array of objects representing the parameter types for handling messages.
     */
    public static final Class<?>[] MESSAGE_PARAMETERS = new Class<?>[]{
            Update.class,
            Message.class,
            Bot.class
    };
    
    /**
     * Array of objects representing the parameter types for handling start command.
     */
    public static final Class<?>[] START_PARAMETERS = new Class<?>[]{
            Update.class,
            Message.class,
            Bot.class
    };
    
    /**
     * Array of objects representing the parameter types for handling message entities.
     */
    public static final Class<?>[] ENTITIES_PARAMETERS = new Class<?>[]{
            Message.class,
            MessageEntity[].class,
            Update.class,
            Bot.class
    };
    
    /**
     * Array of objects representing the parameter types for handling callback queries.
     */
    public static final Class<?>[] CALLBACK_QUERY_PARAMETERS = new Class<?>[]{
            CallbackQuery.class,
            Update.class,
            Bot.class
    };
    
    /**
     * Array of objects representing the parameter types for handling inline queries.
     */
    public static final Class<?>[] INLINE_QUERY_PARAMETERS = new Class<?>[]{
            InlineQuery.class,
            Update.class,
            Bot.class
    };
    
    /**
     * Array of objects representing the parameter types for handling commands.
     */
    public static final Class<?>[] COMMAND_PARAMETERS = new Class<?>[]{
            Message.class,
            MessageEntity[].class,
            Update.class,
            Bot.class
    };
    
}
