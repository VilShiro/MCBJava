<div align = "center">
  <h1>MCB - Multi Client Bot</h1>
  <img src="Logo.jpg" alt=""/>
  <a href="https://github.com/VilShiro/MCBJava/releases/latest">
    <img src="https://img.shields.io/github/release/VilShiro/MCBJava.svg?style=flat" alt="">
  </a>
  <a href="https://github.com/VilShiro/MCBJava/releases/latest">
    <img src="https://img.shields.io/github/downloads/VilShiro/MCBJava/total.svg?style=flat" alt="">
  </a>
</div>

> _Version 1.5 this is a transitional version, in version 2.0 the features of 1.0 (creation through function overrides) will be cut in favor of an emphasis on the annotation management system, and also the possibilities of multi-user multi-threaded work will be implemented._

> _The operation of the multi-user bot has not been tested and is at the development stage, bugs and critical errors are possible._

> _Publishing to Maven Central with version 2.0 release_

<!-- TOC -->
* [Get started(Only for Bot class)](#get-startedonly-for-bot-class)
  * [Bot superclass and @BotConfiguration annotation](#bot-superclass-and-botconfiguration-annotation)
  * [@Feedback annotation](#feedback-annotation)
  * [@Command annotation](#command-annotation)
  * [Priorities](#priorities)
* [Prospect](#prospect)
* [Thanks](#thanks)
<!-- TOC -->

## Get started(Only for Bot class)

### Bot superclass and @BotConfiguration annotation

First, you must create a class that is a descendant of the Bot class and implement one of the constructors (recommended with a configuration class)

MyBot.java
```java
public class MyBot extends Bot{
    public MyBot(){
        super(MyConfig.class);
    }
}
```

Next, in the specified MyConfig class, add the BotConfiguration annotation and specify the token

MyConfig.java

```java
@BotConfiguration(botToken = "your token")
public class MyConfig {
    
}
```

Additional BotConfiguration parameters:

| parameter        | type    | functionality                                                                                                                                                                                                                |
|------------------|---------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| threadSeparation | boolean | each command will run in its own thread                                                                                                                                                                                      |
| startCommand     | String  | start command, usually `/start`                                                                                                                                                                                              |
| doubleDispatch   | boolean | when disabled - updates come only to annotated functions, when enabled - to annotated functions and overridden ones                                                                                                          |
| staticBuild      | boolean | does not create an instance of the configuration class within itself, the value is ignored if the `@BotConfiguration` annotation is on a class that is a descendant of the Bot class, only static functions will be executed |

### @Feedback annotation

Create a function that returns a value of type `void` with any access and static modifier, add to the parameters any set of parameters available for this function

```java
@BotConfiguration(botToken = "your token")
public class MyConfig {

    @Feedback(type = "update")
    public void onUpdate() {
        // parse your update here
    }

    @Feedback(type = "message")
    private static void message(Bot bot) {
        // also parse messages here
    }

    @Feedback(type = "start")
    private void runBot(Message message, Bot bot) {
        // code here
    }

}
```

Parameter sets for update processing functions

| type           | parameter set(classes)                                                                                                 |
|----------------|------------------------------------------------------------------------------------------------------------------------|
| update         | com.pengrad.telegrambot.model.**Update**, org.fbs.mcb.form.**Bot**                                                     |
| start          | com.pengrad.telegrambot.model.**Message**, org.fbs.mcb.form.**Bot**                                                    |
| message        | com.pengrad.telegrambot.model.**Message**, org.fbs.mcb.form.**Bot**                                                    |
| entities       | com.pengrad.telegrambot.model.**MessageEntity**[], com.pengrad.telegrambot.model.**Message**, org.fbs.mcb.form.**Bot** |
| callback_query | com.pengrad.telegrambot.model.**CallbackQuery**, org.fbs.mcb.form.**Bot**                                              |
| inline_query   | com.pengrad.telegrambot.model.**InlineQuery**, org.fbs.mcb.form.**Bot**                                                |

### @Command annotation

Create a function that returns a value of type `void` of any access and static modifier, add the `@Command` annotation and with the `command` parameter specify the command that will be called when sent

```java
@BotConfiguration(botToken = "your token")
public class MyConfig {

    @Command(command = "/help")
    private void help(Bot bot) {
        // realize algorithm here
    }

    @Command(command = "/exit")
    public static void save(Message message){
        // saving logic...
    }

}
```

Command functions have the same parameter sets as `@Feedback(type = "entities")`, but without MessageEntities array

### Priorities

- If the bot token is specified via the constructor, but a configuration containing the token is also added, the bot will have the token specified via the constructor

## Prospect

[-] Add a buffer of processed methods for faster response to updates
[-] Ignoring individual senders
[-] Implementation of multi-user functionality
[-] Possibility of implementing third-party Telegram Bot API

## Thanks
- IDE [Intellij Idea](https://www.jetbrains.com/idea/)
- Base [pengrad telegram bot api](https://github.com/pengrad/java-telegram-bot-api)
- Tests [junit](https://github.com/junit-team/junit4)
- Shortcuts [lombok](https://github.com/projectlombok/lombok)