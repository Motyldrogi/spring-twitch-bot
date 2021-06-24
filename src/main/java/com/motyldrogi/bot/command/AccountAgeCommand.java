package com.motyldrogi.bot.command;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import com.mashape.unirest.http.Unirest;
import com.motyldrogi.bot.command.defaults.CommandExecutor;
import com.motyldrogi.bot.command.defaults.CommandInfo;
import com.motyldrogi.bot.command.defaults.CommandSender;
import com.motyldrogi.bot.component.TwitchMessage;
import com.motyldrogi.bot.util.RestServiceType;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

public class AccountAgeCommand implements CommandExecutor {
    
  @CommandInfo("accage")
  @Override
  public void execute(TwitchMessage tMessage, CommandSender commandSender) {
    try {
      HttpResponse<JsonNode> httpResponse = Unirest.get(RestServiceType.TWITCH_API_URL + "users?login=" + tMessage.getSentBy())
          .header("Accept", "application/vnd.twitchtv.v5+json")
          .header("Client-ID", "5g03rgv9bwij5byhsf3gmnnw2i3xp2")
          .asJsonAsync()
          .get();
          
      JSONObject jsonObject = httpResponse.getBody().getObject();
      String createdAt = jsonObject.getJSONArray("users").getJSONObject(0).getString("created_at");

      DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      Date date = format.parse(createdAt.substring(0, createdAt.lastIndexOf(".")));
			DateFormat newFormat = new SimpleDateFormat("d. MMM yyyy");

      commandSender.sendRawMessage("@" + tMessage.getSentBy() + ", your account was created on " + newFormat.format(date) + ".");

    } catch (InterruptedException | ExecutionException | ParseException e) {
      commandSender.sendRawMessage("Couldn't get data from twitch.");;
    }
  }
}
