package com.motyldrogi.bot.command.defaults;

import com.motyldrogi.bot.component.TwitchMessage;

public interface CommandExecutor {

    void execute(TwitchMessage tMessage, CommandSender commandSender);
}
