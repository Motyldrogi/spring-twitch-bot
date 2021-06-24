package com.motyldrogi.bot.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.motyldrogi.bot.command.defaults.Command;
import com.motyldrogi.bot.command.defaults.CommandSender;
import com.motyldrogi.bot.command.defaults.impl.CommandSenderImpl;
import com.motyldrogi.bot.component.IRCConnection;
import com.motyldrogi.bot.component.MessageComponent;
import com.motyldrogi.bot.component.TwitchMessage;
import com.motyldrogi.bot.configuration.AppProperties;
import com.motyldrogi.bot.service.TwitchService;

import reactor.core.scheduler.Schedulers;

public class TwitchServiceImpl implements TwitchService {

    private final MessageComponent messageComponent;

    private String oauth;
    private String nickname;
    private String prefix;

    private IRCConnection connection;
    private boolean isAuthenticated = false;

    private Map<String, Command> commandRegistry;

    @Override
    public void startBot() {
        this.authorize();
    }

    public TwitchServiceImpl(MessageComponent messageComponent, AppProperties properties) {
        this.connection = new IRCConnection("irc.chat.twitch.tv", 6667, properties.getChannel());
        this.connection.connect();

        this.commandRegistry = new HashMap<String, Command>();

        this.messageComponent = messageComponent;

        // Get properties
        this.oauth = "oauth:" + properties.getOauthToken();
		this.nickname = properties.getNickname();
        this.prefix = properties.getPrefix();
    }

    @Override
    public void registerCommand(String command, Command botCommand) {
        this.commandRegistry.put(command, botCommand);
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
        this.connection.getMessagesFlux().subscribeOn(Schedulers.parallel()).subscribe(message -> {
            processMessage(message);
        });

        this.waitForAuthentication(10);
    }

    @Override
    public void waitForAuthentication(int waitSeconds) {
        int count = 0;
        while (count < waitSeconds) {
            if (this.isAuthenticated) {
                this.connection.joinChannel();
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
            TwitchMessage tMessage = new TwitchMessage(message, this.prefix);
            System.out.println(tMessage.getSentBy() + ": " + tMessage.getMessage());
            if (tMessage.getMessage().startsWith(this.prefix)) {
                processCommand(tMessage);
            }
        }
    }

    private void processCommand(TwitchMessage tMessage) {
        if (this.commandRegistry.keySet().contains(tMessage.getCommand())) {
            Command botCommand = this.commandRegistry.get(tMessage.getCommand());
            CommandSender commandSender = new CommandSenderImpl(this.connection, messageComponent);

            List<String> args = Arrays.stream(tMessage.getMessage().split(" ")).skip(1)
                    .collect(Collectors.toList());
                    
            if ((args.size() < botCommand.getMinArguments()) || (args.size() > botCommand.getMaxArguments())) {
                String usage = prefix + botCommand.getName() + " " + botCommand.getUsage();
                String invalidMessage = messageComponent.get("invalid-pattern", usage);
                this.connection.sendMessage(invalidMessage);
                return;
            }

            botCommand.getExecutor().execute(tMessage, commandSender);
        }
    }
}