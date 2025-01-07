package org.fbs.mcb.data.meta;

import com.pengrad.telegrambot.model.*;
import org.fbs.mcb.data.entity.Bot;
import org.fbs.mcb.util.MethodMapper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
    
    /**
     * This method returns an array of unique parameter classes used in the bot's event handling methods.
     * The array is constructed by merging the parameter arrays of different event handling methods.
     *
     * @return An array of unique parameter classes.
     */
    @NotNull
    public static Class<?>[] getAllUniqueClasses(){
        List<Class<?>> classes = new ArrayList<>();
        addUnique(classes, UPDATE_PARAMETERS);
        addUnique(classes, MESSAGE_PARAMETERS);
        addUnique(classes, START_PARAMETERS);
        addUnique(classes, ENTITIES_PARAMETERS);
        addUnique(classes, CALLBACK_QUERY_PARAMETERS);
        addUnique(classes, INLINE_QUERY_PARAMETERS);
        addUnique(classes, COMMAND_PARAMETERS);
        return classes.toArray(new Class<?>[0]);
    }
    
    /**
     * This method adds unique elements from the given array to the specified list.
     * If an element from the array is already present in the list, it is not added again.
     *
     * @param list The list to which unique elements will be added.
     * @param t    The array of elements to be added to the list.
     * @param <T>  The type of elements in the list and array.
     */
    public static <T> void addUnique(List<T> list, @NotNull T[] t) {
        for (T t1 : t) {
            if (!list.contains(t1)) {
                list.add(t1);
            }
        }
    }
    
}
