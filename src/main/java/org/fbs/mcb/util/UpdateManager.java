package org.fbs.mcb.util;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.fbs.mcb.annotation.Command;
import org.fbs.mcb.annotation.Feedback;
import org.fbs.mcb.data.entity.AbstractBot;

import java.util.Objects;

/**
 * The UpdateManager class is responsible for processing updates received from a Telegram bot.
 * It categorizes the update type and performs specific actions accordingly.
 * It utilizes reflection to invoke methods annotated with {@link Feedback} and {@link Command} annotations.
 */
public class UpdateManager {

    /**
     * The ConfigurationProcessor instance used for processing updates.
     */
    private final ConfigurationProcessor processor;

    /**
     * Constructs a new UpdateManager instance.
     *
     * @param processor The ConfigurationProcessor instance used for processing updates.
     */
    public UpdateManager(ConfigurationProcessor processor) {
        this.processor = processor;
    }


    /**
     * Processes the received update from a Telegram bot.
     * It categorizes the update type and performs specific actions accordingly.
     *
     * @param update The received update from Telegram.
     * @param bot The Bot entity associated with the update.
     */
    public void processUpdate(Update update, AbstractBot<?> bot){
        updateParse(update, bot);
        if (update.message() != null && !Objects.equals(update.message().text(), "")){
            Message message = update.message();
            parseMessage(update, bot);
            try {
                if (message.entities().length > 0) {
                    entitiesParse(update, bot);
                    if (message.text().startsWith("/")) {
                        if (message.text().contains(processor.getStartCommand())) {
                            onStartCommand(update, bot);
                        }
                        commandParse(update, bot);
                    }
                }
            }catch (NullPointerException ignored){}
        }
        else if (update.callbackQuery() != null){
            callbackQueryParse(update, bot);
        }
        else if (update.inlineQuery() != null) {
            inlineQueryParse(update, bot);
        }
    }

    /**
     * Parses and processes the command received in a Telegram update.
     *
     * @param update The received update from Telegram.
     * @param bot The Bot entity associated with the update.
     */
    public void commandParse(Update update, AbstractBot<?> bot) {
        processor.getMethodSet().callCommands(update.message().text(), update.message(), update.message().entities(), update, bot);
    }

    /**
     * Parses and processes the inline query received in a Telegram update.
     *
     * @param update The received update from Telegram.
     * @param bot The Bot entity associated with the update.
     */
    public void inlineQueryParse(Update update, AbstractBot<?> bot) {
        processor.getMethodSet().callInlineQuery(update.inlineQuery(), update, bot);
    }

    /**
     * Parses and processes the callback query received in a Telegram update.
     *
     * @param update The received update from Telegram.
     * @param bot The Bot entity associated with the update.
     */
    public void callbackQueryParse(Update update, AbstractBot<?> bot) {
        processor.getMethodSet().callCallbackQuery(update.callbackQuery(), update, bot);
    }

    /**
     * Parses and processes the start command received in a Telegram update.
     *
     * @param update The received update from Telegram.
     * @param bot The Bot entity associated with the update.
     */
    public void onStartCommand(Update update, AbstractBot<?> bot) {
        processor.getMethodSet().callStart(update, update.message(), bot);
    }

    /**
     * Parses and processes the entities received in a Telegram update.
     *
     * @param update The received update from Telegram.
     * @param bot The Bot entity associated with the update.
     */
    public void entitiesParse(Update update, AbstractBot<?> bot) {
        processor.getMethodSet().callEntities(update.message(), update.message().entities(), update, bot);
    }

    /**
     * Parses and processes the message received in a Telegram update.
     *
     * @param update The received update from Telegram.
     * @param bot The Bot entity associated with the update.
     */
    public void parseMessage(Update update, AbstractBot<?> bot) {
        processor.getMethodSet().callMessage(update, update.message(), bot);
    }

    /**
     * Parses and processes the general update received from a Telegram bot.
     *
     * @param update The received update from Telegram.
     * @param bot The Bot entity associated with the update.
     */
    public void updateParse(Update update, AbstractBot<?> bot) {
        processor.getMethodSet().callUpdate(update, bot);
    }

}
