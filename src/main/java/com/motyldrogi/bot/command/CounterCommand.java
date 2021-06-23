package com.motyldrogi.bot.command;

import com.motyldrogi.bot.command.defaults.CommandExecutor;
import com.motyldrogi.bot.command.defaults.CommandInfo;
import com.motyldrogi.bot.component.MessageComponent;
import com.motyldrogi.bot.component.TwitchMessage;

public class CounterCommand implements CommandExecutor {
    
    private int counter;

    public CounterCommand() {
        this.counter = 0;
    }

    @CommandInfo("counter")
    @Override
    public String execute(TwitchMessage tMessage, MessageComponent messageComponent) {
        
        this.counter++;
        return "The counter is currently at " + this.counter;
    }
}
