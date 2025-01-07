package org.fbs.mcb;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.fbs.mcb.annotation.BotConfiguration;
import org.fbs.mcb.annotation.Command;
import org.fbs.mcb.annotation.Feedback;
import org.fbs.mcb.data.entity.Bot;

@BotConfiguration(botToken = "7729126817:AAHfEXMObLlfLVcv5fxtDKanNksQe9Rr5fk")
public class TestBot extends Bot {

    public TestBot(){
        super(TestBot.class);
    }

    @Feedback("message")
    private static void message(Message message, Bot bot){
        bot.getBot().execute(new SendMessage(message.chat().id(), "Pizdec"));
    }

    @Command("/docs")
    private static void getDocs(Message message, Bot bot){
        bot.getBot().execute(new SendMessage(message.chat().id(), "https://core.telegram.org/bots/api"));
    }

}
