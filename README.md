# Spring Boot Twitch Bot
Twitch all-purpose bot, made using Spring Boot 2.5.1, WebFlux and Project Reactor.

**TravisCI**<br>
[![travis-icon]][travis]

Requirements for connection
---
Find the file `app.properties` in the resources folder and change following variables:

- **prefix**: The prefix for all command
- **oauthToken**: OAuth Token of the bot with at least the scopes `chat:read` and `chat:edit`, https://twitchtokengenerator.com/
- **nickname**: Twitch username of the bot account
- **channel**: The name of the channel in which the bot should join

Adding a Command
---

Every bot command should be a part of the `com.motyldrogi.bot.command` package and implement the `CommandExecutor` class, implementing the `execute()` method at bare-minimum. The `execute()` method expectes two arguments:

- **tMessage (TwitchMessage)**: The `TwitchMessage` object which contains the full information about the message
- **messageComponent (MessageComponent)**: The `MessageComponent` object which contains the texts for localization

The `execute()` method needs a `CommandInfo()` annotation to work and it should return a String, which will be sent back to the channel it was received from.

`CommandInfo()` annotation can have the following arguments:

- **value**: The value that triggers the command, i.e. after the prefix
- **minArguments**: Minimum arguments count for the command, defaults to 0
- **maxArguments**: Maximum arguments count for the command, defaults to 0
- **usage**: Text that gets displayed if the command was not used correctly

For example, the following command echos back the message received, tagging the person that sent it:

```java
package com.motyldrogi.bot.command;

import com.motyldrogi.bot.command.defaults.CommandExecutor;
import com.motyldrogi.bot.command.defaults.CommandInfo;
import com.motyldrogi.bot.component.MessageComponent;
import com.motyldrogi.bot.component.TwitchMessage;

public class EchoCommand implements CommandExecutor {

    @CommandInfo(value = "echo", minArguments = 1, maxArguments = 1, usage = "<message>")
    @Override
    public String execute(TwitchMessage tMessage, MessageComponent messageComponent) {

        return "@" + tMessage.getSentBy() + " " + tMessage.getData();
    }
}
```

If you created a command, you have to register it:
```java
this.commandRegistry.registerByExecutors(
    [...],
    new EchoCommand()
);
```

## Features
- [x] Easy to add commands
- [x] Localization
- [x] 3 types of basic commands

## Endpoints

| Method                                             | Success status codes   | Error status codes |
| -------------------------------------------------- | ---------------------  | ------------------ |             
| **GET  /actuator/** | 200                    | 404                |
| **GET  /actuator/health** | 200                    | 404                |

[travis-icon]: https://www.travis-ci.com/Motyldrogi/spring-twitch-bot.svg?token=BAY6DRwNfoKsyPs22bzN&branch=main
[travis]: https://www.travis-ci.com/github/Motyldrogi/spring-twitch-bot/
