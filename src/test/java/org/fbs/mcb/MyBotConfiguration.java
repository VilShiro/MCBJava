package org.fbs.mcb;


import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.fbs.mcb.annotation.BotConfiguration;
import org.fbs.mcb.annotation.Feedback;
import org.fbs.mcb.data.entity.Bot;

@BotConfiguration(botToken = "5792337403:AAFxy4ksALgHyCw-2pNzcG1jFPaHHmbABMI")
public class MyBotConfiguration {

    @Feedback("update")
    private static void update(Update update){
        System.out.println("update: " + update);
    }

    @Feedback("message")
    private static void message(Message message, Bot bot){
        System.out.println(8);
        System.out.println(message.text() == null);
        bot.getBot().execute(new SendMessage(message.chat().id(), message.text()));
    }

}
