package com.motyldrogi.bot.command;

import com.motyldrogi.bot.command.defaults.CommandExecutor;
import com.motyldrogi.bot.command.defaults.CommandInfo;
import com.motyldrogi.bot.command.defaults.CommandSender;
import com.motyldrogi.bot.component.TwitchMessage;

public class CounterCommand implements CommandExecutor {
    
    private int counter;

    public CounterCommand() {
        this.counter = 0;
    }

    @CommandInfo("counter")
    @Override
    public void execute(TwitchMessage tMessage, CommandSender commandSender) {
        
        this.counter++;
        commandSender.sendRawMessage("The counter is currently at " + this.counter);
    }
}
