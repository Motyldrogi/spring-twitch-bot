package com.motyldrogi.bot.command;

import com.motyldrogi.bot.command.defaults.CommandExecutor;
import com.motyldrogi.bot.command.defaults.CommandInfo;
import com.motyldrogi.bot.command.defaults.CommandSender;
import com.motyldrogi.bot.component.TwitchMessage;

public class EchoCommand implements CommandExecutor {

    @CommandInfo(value = "echo", minArguments = 1, maxArguments = 1, usage = "<message>")
    @Override
    public void execute(TwitchMessage tMessage, CommandSender commandSender) {

        commandSender.sendRawMessage("@" + tMessage.getSentBy() + " " + tMessage.getData());
    }
}
