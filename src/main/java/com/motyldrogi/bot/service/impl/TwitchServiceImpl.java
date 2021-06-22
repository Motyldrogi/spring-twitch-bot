package com.motyldrogi.bot.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.motyldrogi.bot.command.defaults.Command;
import com.motyldrogi.bot.component.IRCConnection;
import com.motyldrogi.bot.component.MessageComponent;
import com.motyldrogi.bot.component.TwitchMessage;
import com.motyldrogi.bot.configuration.AppProperties;
import com.motyldrogi.bot.service.TwitchService;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import reactor.core.scheduler.Schedulers;

public class TwitchServiceImpl implements TwitchService {

    private final MessageComponent messageComponent;

    private String oauth;
    private String nickname;
    private String channel;
    private String prefix;

    private IRCConnection connection;
    private boolean isAuthenticated = false;

    private Map<String, Command> commandRegistry;
    
    private Map<String, Counter> commandMetrics;
    private Counter totalBotCommands_counter;

    @Override
    public void startBot() {
		this.authorize();
		this.joinChannel();
    }

    public TwitchServiceImpl(MessageComponent messageComponent, AppProperties properties) {
        this.connection = new IRCConnection("irc.chat.twitch.tv", 6667);
        this.connection.connect();

        this.commandRegistry = new HashMap<String, Command>();
        this.commandMetrics = new HashMap<String, Counter>();

        this.messageComponent = messageComponent;

        // Get properties
        this.oauth = "oauth:" + properties.getOauthToken();
		this.nickname = properties.getNickname();
		this.channel = properties.getChannel();
        this.prefix = properties.getPrefix();

        // Enable metrics for schedulers
        Schedulers.enableMetrics();

        // Build the counter to gather metrics
        this.totalBotCommands_counter = Counter.builder("botcommand_total_counter")
            .description("Total invocations of all bot commands")
            .register(Metrics.globalRegistry);
    }

    @Override
    public void registerCommand(String command, Command botCommand) {
        this.commandRegistry.put(command, botCommand);
        
        // Build the counter to gather metrics
        Counter counter = Counter.builder("botcommand_" + command + "_counter")
            .description("Invocation of the " + command + " command")
            .register(Metrics.globalRegistry);
        this.commandMetrics.put(command, counter);
    }

    @Override
    public boolean isConnected() {
        return this.connection.isConnected();
    }

    @Override
    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    @Override
    public void authorize() {
        this.connection.send("PASS " + this.oauth);
        this.connection.send("NICK " + this.nickname);

        // Subscribe to the Flux stream and process the messages as they come in
        this.connection.getMessagesFlux().metrics().subscribeOn(Schedulers.parallel()).subscribe(message -> {
            processMessage(message);
        });

        this.waitForAuthentication(10);
    }

    @Override
    public void joinChannel() {
        this.connection.send("JOIN #" + this.channel);
        System.out.println("Joined to #" + this.channel);
    }

    @Override
    public void sendMessage(String message) {
        this.connection.send("PRIVMSG #" + this.channel + " :" + message);
    }

    @Override
    public void waitForAuthentication(int waitSeconds) {
        int count = 0;
        while (count < waitSeconds) {
            if (this.isAuthenticated) {
                return;
            } else {
                count++;
                try {
                    Thread.sleep(1000);
                } catch (IllegalArgumentException e) {
                } catch (InterruptedException e) { }
            }
        }
    }

    private void processMessage(String message) {
        if (message.contains("Welcome, GLHF!")) {
            // Authenticated to the server
            this.isAuthenticated = true;
            System.out.println("AUTHENTICATED TO SERVER");
        } else if (message.startsWith("PING")) {
            // Periodic PING command
            System.out.print("Responding to PING: . . . ");
            this.connection.send(message.replace("PING", "PONG"));
            System.out.println("done!");
        } else {
            // Not an internal command, see if it's a bot command
            TwitchMessage tMessage = new TwitchMessage(message);
            System.out.println(tMessage.getSentBy() + ": " + tMessage.getMessage());
            if (tMessage.getMessage().startsWith(this.prefix)) {
                processCommand(tMessage);
            }
        }
    }

    private void processCommand(TwitchMessage tMessage) {
        String command = tMessage.getMessage().split(" ")[0].replace(this.prefix, "");
        String data = tMessage.getMessage().replace(this.prefix + command, "").trim();

        if (this.commandRegistry.keySet().contains(command)) {
            Command botCommand = this.commandRegistry.get(command);

            List<String> args = Arrays.stream(tMessage.getMessage().split(" ")).skip(1)
                    .collect(Collectors.toList());
                    
            if ((args.size() < botCommand.getMinArguments()) || (args.size() > botCommand.getMaxArguments())) {
                String usage = prefix + botCommand.getName() + " " + botCommand.getUsage();
                String invalidMessage = messageComponent.get("invalid-pattern", usage);
                this.sendMessage(invalidMessage);
                return;
            }

            String retMessage = botCommand.getExecutor().execute(data, tMessage, messageComponent);

            // Increment the counters
            this.commandMetrics.get(command).increment();
            this.totalBotCommands_counter.increment();

            this.sendMessage(retMessage);
        }
    }
}