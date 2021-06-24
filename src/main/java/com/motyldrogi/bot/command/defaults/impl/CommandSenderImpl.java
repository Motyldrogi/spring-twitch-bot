package com.motyldrogi.bot.command.defaults.impl;

import java.util.List;

import com.motyldrogi.bot.command.defaults.CommandSender;
import com.motyldrogi.bot.component.IRCConnection;
import com.motyldrogi.bot.component.MessageComponent;

public class CommandSenderImpl implements CommandSender {

    private final IRCConnection connection;
    private final MessageComponent messageComponent;

    public CommandSenderImpl(IRCConnection connection, MessageComponent messageComponent) {
        this.connection = connection;
        this.messageComponent = messageComponent;
    }

    @Override
    public void sendRawMessage(String message) {
      this.connection.sendMessage(message);
    }
  
    @Override
    public void sendRawMessage(List<String> messages) {
      messages.forEach(message -> this.sendRawMessage(message));
    }

    @Override
    public void sendMessage(String key, String... arguments) {
      this.connection.sendMessage(this.messageComponent.get(key, arguments));
    }
  
    @Override
    public void sendMessage(List<String> keys, String... arguments) {
      keys.forEach(key -> this.sendMessage(key, arguments));
    }

    @Override
    public String getMessage(String key, String... arguments) {
      return this.messageComponent.get(key, arguments);
    }

    @Override
    public IRCConnection getConnection() {
      return this.connection;
    }
    
}
