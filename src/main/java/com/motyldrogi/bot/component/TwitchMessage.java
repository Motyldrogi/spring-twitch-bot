package com.motyldrogi.bot.component;

public class TwitchMessage {
    
    private String rawMessage;
    private String sentBy;
    private String message;
    private String channel;

    public TwitchMessage(String rawMessage) {
        this.rawMessage = rawMessage;
        parseMessage(rawMessage);
    }

    private void parseMessage(String rawMessage) {
        String[] splitMessage = rawMessage.split(":");
        
        // Parse metadata
        String[] splitMetadata = splitMessage[1].split(" ");
        this.sentBy = splitMetadata[0].split("!")[0];
        this.channel = splitMetadata[2].replace("#", "").trim();

        // Extract the message
        String metadata = ":" + splitMetadata[0] + " PRIVMSG " + "#" + this.channel + " :";
        this.message = this.rawMessage.replace(metadata, ""); 
    }

    public String getMessage() {
        return this.message;
    }

    public String getSentBy() { 
        return this.sentBy;
    }

    public String getChannel() {
        return this.channel;
    }
}
