package com.motyldrogi.bot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:app.properties")
@ConfigurationProperties
public class AppProperties {

  private String prefix;

  private String oauthToken;

  private String nickname;

  private String channel;

  public String getPrefix() {
    return prefix;
  }

  public String getOauthToken() {
    return oauthToken;
  }

  public void setOauthToken(String oauthToken) {
      this.oauthToken = oauthToken;
  }

  public String getNickname() {
      return nickname;
  }

  public void setNickname(String nickname) {
      this.nickname = nickname;
  }

  public String getChannel() {
      return channel;
  }

  public void setChannel(String channel) {
      this.channel = channel;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

}