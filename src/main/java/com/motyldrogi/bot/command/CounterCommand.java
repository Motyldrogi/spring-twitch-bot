package com.motyldrogi.bot.command;

import java.util.Optional;

import com.motyldrogi.bot.command.defaults.CommandExecutor;
import com.motyldrogi.bot.command.defaults.CommandInfo;
import com.motyldrogi.bot.command.defaults.CommandSender;
import com.motyldrogi.bot.component.TwitchMessage;
import com.motyldrogi.bot.entity.impl.CounterEntityImpl;
import com.motyldrogi.bot.repository.CounterRepository;

import org.springframework.beans.factory.annotation.Autowired;

public class CounterCommand implements CommandExecutor {
    
    private final CounterRepository counterRepository;

    @Autowired
    public CounterCommand(CounterRepository counterRepository) {
      this.counterRepository = counterRepository;
    }

    public CounterCommand() {
        this(null);
    }

    @CommandInfo("counter")
    @Override
    public void execute(TwitchMessage tMessage, CommandSender commandSender) {
        CounterEntityImpl counterEntity = null;

        Optional<CounterEntityImpl> optionalCounterEntity = counterRepository.findByName("counter-command");

        if(!optionalCounterEntity.isPresent()) {
            counterEntity = new CounterEntityImpl.Builder()
                .withIdentifier(null)
                .withName("counter-command")
                .setValue(0)
                .build();
            
            counterEntity = counterRepository.save(counterEntity);
        }

        if(counterEntity == null) {
            counterEntity = optionalCounterEntity.get();
        }

        Integer counter = counterEntity.getValue();
        counter++;

        // Update value +1
        counterEntity.setValue(counter);
        counterRepository.save(counterEntity);
        
        commandSender.sendRawMessage("The counter is currently at " + counter);
    }
}
