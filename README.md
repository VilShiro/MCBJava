# MCB - Multi Client Bot

---

## Structure
<!-- TOC -->
* [Get started](#get-started)
* [Features](#features)
* [Thanks❤️](#thanks)
<!-- TOC -->

## Get started

Create a class inheriting from the Bot or MultiClientBot

```java
import com.pengrad.telegrambot.model.*;
import org.fbs.mcb.core.form.Bot;

public class MyBot extends Bot {

    private static final String BOT_TOKEN = "your bot token";
    private static final String START_COMMAND = "/start";

    protected MyBot() {
        super(BOT_TOKEN, START_COMMAND);
    }

    @Override
    protected void onStartCommand(Message message) {}

    @Override
    protected void updateParse(Update update) {}

    @Override
    protected void callbackQueryParse(CallbackQuery query) {}

    @Override
    protected void entitiesParse(MessageEntity[] messageEntities, Message message) {}

    @Override
    protected void messageParse(Message message) {}

    @Override
    protected void inlineQueryParse(InlineQuery query) {}
}
```

## Features

The Bot class methods do not create separate threads for processing, if your implementation requires multi-threaded processing, you will have to implement it yourself.

## Thanks
- IDE [Intellij Idea](https://www.jetbrains.com/idea/)
- Base [pengrad telegram bot api](https://github.com/pengrad/java-telegram-bot-api)
