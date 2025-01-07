package org.fbs.mcb.util;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.fbs.mcb.annotation.Command;
import org.fbs.mcb.annotation.Feedback;
import org.fbs.mcb.data.entity.Bot;

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
    public void processUpdate(Update update, Bot bot){
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

    public void commandParse(Update update, Bot bot) {
        processor.getMethodSet().callCommands(update.message(), update.message().entities(), update, bot);
    }

    public void inlineQueryParse(Update update, Bot bot) {
        processor.getMethodSet().callInlineQuery(update.inlineQuery(), update, bot);
    }

    public void callbackQueryParse(Update update, Bot bot) {
        processor.getMethodSet().callCallbackQuery(update.callbackQuery(), update, bot);
    }

    public void onStartCommand(Update update, Bot bot) {
        processor.getMethodSet().callStart(update, update.message(), bot);
    }

    public void entitiesParse(Update update, Bot bot) {
        processor.getMethodSet().callEntities(update.message(), update.message().entities(), update, bot);
    }

    public void parseMessage(Update update, Bot bot) {
        processor.getMethodSet().callMessage(update, update.message(), bot);
    }

    public void updateParse(Update update, Bot bot) {
        processor.getMethodSet().callUpdate(update, bot);
    }

}
