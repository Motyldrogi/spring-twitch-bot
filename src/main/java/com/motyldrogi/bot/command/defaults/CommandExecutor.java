package com.motyldrogi.bot.command.defaults;

import com.motyldrogi.bot.component.MessageComponent;
import com.motyldrogi.bot.component.TwitchMessage;

public interface CommandExecutor {

    public String execute(TwitchMessage tMessage, MessageComponent messageComponent);
}
