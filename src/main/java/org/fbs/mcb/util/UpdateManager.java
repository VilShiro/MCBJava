package org.fbs.mcb.util;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.fbs.mcb.data.entity.AbstractBot;
import org.fbs.mcb.util.base.AbstractUpdateManager;

import java.util.Objects;

/**
 * The UpdateManager class is responsible for processing updates received from a Telegram bot.
 * It extends the AbstractUpdateManager class and provides methods for parsing and processing different types of updates.
 */
public class UpdateManager extends AbstractUpdateManager {

    /**
     * Sets the ConfigurationProcessor for the UpdateManager.
     * This method overrides the setProcessor method from the AbstractUpdateManager class.
     *
     * @param processor The ConfigurationProcessor to be set for the UpdateManager.
     *                  This processor will be used to handle and process updates received from the Telegram bot.
     *
     * @see AbstractUpdateManager#setProcessor(ConfigurationProcessor)
     */
    @Override
    public void setProcessor(ConfigurationProcessor processor) {
        super.setProcessor(processor);
    }

    /**
     * Processes the received update from a Telegram bot.
     *
     * @param args An array of objects containing the update and the associated bot.
     *             The first element should be an instance of {@link Update}, and the second element should be an instance of {@link AbstractBot<?>}.
     *             If the provided arguments do not match these types, the function will attempt to cast them accordingly.
     *
     * @throws NullPointerException If the update or bot is null.
     */
    @Override
    public void processUpdate(Object ... args){
        Update update = null;
        AbstractBot<?> bot = null;
        
        if (args[0] instanceof Update){
            update = (Update) args[0];
        }
        else {
            throw new RuntimeException("This update handler must take a non-null Update value as its first argument");
        }

        if (args[1] instanceof AbstractBot<?>){
            bot = (AbstractBot<?>) args[1];
        }
        else {
            throw new RuntimeException("This update handler must take a non-null AbstractBot<?> value as its second argument");
        }
        
        updateParse(update, bot);
        if (update.message() != null && !Objects.equals(update.message().text(), "")) {
            Message message = update.message();
            parseMessage(update, bot);
            try {
                if (message.entities().length > 0) {
                    entitiesParse(update, bot);
                    if (message.text().startsWith("/")) {
                        if (message.text().contains(getProcessor().getStartCommand())) {
                            onStartCommand(update, bot);
                        }
                        commandParse(update, bot);
                    }
                }
            } catch (NullPointerException ignored) {
            }
        }
    }

    /**
     * Parses and processes the command received in a Telegram update.
     *
     * @param update The received update from Telegram.
     * @param bot The Bot entity associated with the update.
     */
    public void commandParse(Update update, AbstractBot<?> bot) {
        getProcessor().getMethodSet().callCommands(update.message().text(), update.message(), update.message().entities(), update, bot);
    }

    /**
     * Parses and processes the inline query received in a Telegram update.
     *
     * @param update The received update from Telegram.
     * @param bot The Bot entity associated with the update.
     */
    public void inlineQueryParse(Update update, AbstractBot<?> bot) {
        getProcessor().getMethodSet().callInlineQuery(update.inlineQuery(), update, bot);
    }

    /**
     * Parses and processes the callback query received in a Telegram update.
     *
     * @param update The received update from Telegram.
     * @param bot The Bot entity associated with the update.
     */
    public void callbackQueryParse(Update update, AbstractBot<?> bot) {
        getProcessor().getMethodSet().callCallbackQuery(update.callbackQuery(), update, bot);
    }

    /**
     * Parses and processes the start command received in a Telegram update.
     *
     * @param update The received update from Telegram.
     * @param bot The Bot entity associated with the update.
     */
    public void onStartCommand(Update update, AbstractBot<?> bot) {
        getProcessor().getMethodSet().callStart(update, update.message(), bot);
    }

    /**
     * Parses and processes the entities received in a Telegram update.
     *
     * @param update The received update from Telegram.
     * @param bot The Bot entity associated with the update.
     */
    public void entitiesParse(Update update, AbstractBot<?> bot) {
        getProcessor().getMethodSet().callEntities(update.message(), update.message().entities(), update, bot);
    }

    /**
     * Parses and processes the message received in a Telegram update.
     *
     * @param update The received update from Telegram.
     * @param bot The Bot entity associated with the update.
     */
    public void parseMessage(Update update, AbstractBot<?> bot) {
        getProcessor().getMethodSet().callMessage(update, update.message(), bot);
    }

    /**
     * Parses and processes the general update received from a Telegram bot.
     *
     * @param update The received update from Telegram.
     * @param bot The Bot entity associated with the update.
     */
    public void updateParse(Update update, AbstractBot<?> bot) {
        getProcessor().getMethodSet().callUpdate(update, bot);
    }

}
