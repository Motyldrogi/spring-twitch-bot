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
