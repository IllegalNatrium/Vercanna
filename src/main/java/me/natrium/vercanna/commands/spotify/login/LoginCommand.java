package me.natrium.vercanna.commands.spotify.login;

import me.natrium.redis.RedisController;
import me.natrium.spotify.SpotifyController;
import me.natrium.vercanna.controllers.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

public class LoginCommand extends ICommand {

  public LoginCommand(@NotNull String name, @NotNull String description) {
    super(name, description);
  }

  @Override
  public void executeCommand(SlashCommandInteractionEvent event, RedisController redis, SpotifyController spotify, Connection connection) {
    if(event.getMember().getIdLong() == 453984820242939905L) {
      event.deferReply(true)
          .setContent("Failed. not owner")
          .queue();
      return;
    }

    String url = spotify.authorizationCodeSync();
    event.deferReply(true)
        .setContent(url)
        .queue();
  }

  @Override
  public boolean isDisabled() {
    return false;
  }

  @Override
  public boolean isStaffOnly() {
    return false;
  }
}
