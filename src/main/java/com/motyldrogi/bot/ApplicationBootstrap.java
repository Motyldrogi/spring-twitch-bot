package com.motyldrogi.bot;

import com.motyldrogi.bot.command.*;
import com.motyldrogi.bot.command.defaults.impl.CommandRegistry;
import com.motyldrogi.bot.repository.CounterRepository;
import com.motyldrogi.bot.service.TwitchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class ApplicationBootstrap implements CommandLineRunner {

  private final TwitchService twitchService;
  private final CommandRegistry commandRegistry;

  public ApplicationBootstrap(TwitchService twitchService, CommandRegistry commandRegistry) {
    this.twitchService = twitchService;
    this.commandRegistry = commandRegistry;
  }

	public static void main(String[] args) {
		SpringApplication.run(ApplicationBootstrap.class, args);
  }

  @Autowired
  private CounterRepository counterRepository;

  @Override
  public void run(String... args) throws Exception {
		twitchService.startBot();

		// Register commands
    this.commandRegistry.registerByExecutors(
      new EchoCommand(),
      new CounterCommand(counterRepository),
      new DiceCommand(),
      new AccountAgeCommand()
    );
  }
}
