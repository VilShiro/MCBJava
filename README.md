<div align = "center">
  <h1>MCB - Multi Client Bot</h1>
  <img src="Logo.png" alt="" height="205"/>
  <p></p>
  <a href="https://github.com/VilShiro/MCBJava/releases/latest">
    <img src="https://img.shields.io/github/release/VilShiro/MCBJava.svg?style=flat" alt="">
  </a>
  <a href="https://github.com/VilShiro/MCBJava/releases/latest">
    <img src="https://img.shields.io/github/downloads/VilShiro/MCBJava/total.svg?style=flat" alt="">
  </a>
</div>

> _Version 1.5 this is a transitional version, in version 2.0 the features of 1.0 (creation through function overrides) will be cut in favor of an emphasis on the annotation management system, and also the possibilities of multi-user multi-threaded work will be implemented_

> _The operation of the multi-user bot has not been tested and is at the development stage, bugs and critical errors are possible_

> _Publishing to Maven Central with version 2.0 release_

Please give my repository a ⭐, it will help me know that someone is using it

<!-- TOC -->
  * [Installation](#installation)
  * [Get started(Only for Bot class)](#get-startedonly-for-bot-class)
    * [Old Variant](#old-variant)
    * [Present variant](#present-variant)
      * [Bot superclass and @BotConfiguration annotation](#bot-superclass-and-botconfiguration-annotation)
      * [@Feedback annotation](#feedback-annotation)
      * [@Command annotation](#command-annotation)
  * [Bugs](#bugs)
  * [Priorities](#priorities)
  * [Prospect](#prospect)
  * [Thanks](#thanks)
<!-- TOC -->

## Installation

> _Version 1.5.x early access_

Maven
```xml
<dependency>
    <groupId>io.github.vilshiro</groupId>
    <artifactId>mcb</artifactId>
    <version>1.5.3</version>
</dependency>
```

[Maven Central Repository](https://central.sonatype.com/artifact/io.github.vilshiro/mcb/overview)

## Get started(Only for Bot class)

### Old Variant

Create a class that is a descendant of the class [`Bot`](src/main/java/org/fbs/mcb/data/entity/Bot.java) and override the necessary functions, the constructor accepts the bot token

Use the `getBot()` command to get a bot instance

MyBot.java

```java
public class MyBot extends Bot {
    public MyBot() {
        super("your token");
    }

    @Override
    protected void updateParse(Update update) {
        // your logic
    }

    @Override
    protected void messageParse(Message message) {
        // parsing the messages
    }

    @Override
    protected void callbackQueryParse(CallbackQuery query) {
        // callback data...
    }
}
```

### Present variant

#### Bot superclass and @BotConfiguration annotation

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

#### @Feedback annotation

Create any function, add the annotation [`@Feedback`](src/main/java/org/fbs/mcb/annotation/Feedback.java), add any set of parameters available for this function
In the annotation, specify a string indicating the type of updates that it can process

```java
@BotConfiguration(botToken = "your token")
public class MyConfig {

    @Feedback("update")
    public void onUpdate() {
        // parse your update here
    }

    @Feedback("message")
    private static void message(Bot bot) {
        // also parse messages here
    }

    @Feedback("start")
    private void runBot(Message message, Bot bot) {
        // code here
    }

}
```

Parameter sets for update processing functions [Constants.java](src/main/java/org/fbs/mcb/data/meta/Constants.java)

| type           | parameter set(classes)                                                                                                                                           |
|----------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| update         | com.pengrad.telegrambot.model.**Update**, org.fbs.mcb.form.**Bot**                                                                                               |
| start          | com.pengrad.telegrambot.model.**Update**, com.pengrad.telegrambot.model.**Message**, org.fbs.mcb.form.**Bot**                                                    |
| message        | com.pengrad.telegrambot.model.**Update**, com.pengrad.telegrambot.model.**Message**, org.fbs.mcb.form.**Bot**                                                    |
| entities       | com.pengrad.telegrambot.model.**Update**, com.pengrad.telegrambot.model.**Message**, com.pengrad.telegrambot.model.**MessageEntity**[], org.fbs.mcb.form.**Bot** |
| callback_query | com.pengrad.telegrambot.model.**Update**, com.pengrad.telegrambot.model.**CallbackQuery**, org.fbs.mcb.form.**Bot**                                              |
| inline_query   | com.pengrad.telegrambot.model.**Update**, com.pengrad.telegrambot.model.**InlineQuery**, org.fbs.mcb.form.**Bot**                                                |
| command        | com.pengrad.telegrambot.model.**Update**, com.pengrad.telegrambot.model.**Message**, com.pengrad.telegrambot.model.**MessageEntity**[], org.fbs.mcb.form.**Bot** |

#### @Command annotation

Create any function, add the annotation [`@Command`](src/main/java/org/fbs/mcb/annotation/Command.java), inside specify a line pointing to the command that is bound to this function

```java
@BotConfiguration(botToken = "your token")
public class MyConfig {

    @Command("/help")
    private void help(Bot bot) {
        // realize algorithm here
    }

    @Command("/exit")
    public static void save(Message message){
        // saving logic...
    }

}
```

Command functions have the same parameter sets as `@Feedback("entities")`, but without MessageEntities array

## Bugs

- Annotated methods fire twice when used as a bean in a Spring application (Possible workaround: use additional @Lazy annotation)

## Priorities

- If the bot token is specified via the constructor, but a configuration containing the token is also added, the bot will have the token specified via the constructor

## Prospect

- [X] Add a buffer of processed methods for faster response to updates
- [ ] Implementation of multi-user functionality
- [X] Possibility of implementing third-party Telegram Bot API
- [ ] Increase the autonomy of each element

## Thanks
- IDE [Intellij Idea](https://www.jetbrains.com/idea/)
- Base [pengrad telegram bot api](https://github.com/pengrad/java-telegram-bot-api)
- Tests [junit](https://github.com/junit-team/junit4)
- Shortcuts [lombok](https://github.com/projectlombok/lombok)