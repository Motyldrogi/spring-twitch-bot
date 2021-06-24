package com.motyldrogi.bot.command.defaults;
import java.util.List;

import com.motyldrogi.bot.component.IRCConnection;

public interface CommandSender {

    void sendRawMessage(String message);

    void sendRawMessage(List<String> messages);

    void sendMessage(String key, String... arguments);

    void sendMessage(List<String> keys, String... arguments);
    
    String getMessage(String key, String... arguments);

    IRCConnection getConnection();

}
