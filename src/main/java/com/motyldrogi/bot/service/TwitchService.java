package com.motyldrogi.bot.service;

import com.motyldrogi.bot.command.defaults.Command;

import org.springframework.stereotype.Service;

@Service
public interface TwitchService {

    void startBot();

    void registerCommand(String command, Command botCommand);

    boolean isConnected();

    boolean isAuthenticated();

    void authorize();

    void joinChannel();

    void sendMessage(String message);

    void waitForAuthentication(int waitSeconds);
}
