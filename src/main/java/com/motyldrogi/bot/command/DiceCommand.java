package com.motyldrogi.bot.command;

import java.util.Random;

import com.motyldrogi.bot.command.defaults.CommandExecutor;
import com.motyldrogi.bot.command.defaults.CommandInfo;
import com.motyldrogi.bot.command.defaults.CommandSender;
import com.motyldrogi.bot.component.TwitchMessage;

public class DiceCommand implements CommandExecutor {
    
    private Random rand;

    public DiceCommand() {
        this.rand = new Random(); 
    }

    @CommandInfo("dice")
    @Override
    public void execute(TwitchMessage tMessage, CommandSender commandSender) {
        
        commandSender.sendRawMessage("@" + tMessage.getSentBy() + " You rolled: " + (rand.nextInt(6) + 1));
    }
}
