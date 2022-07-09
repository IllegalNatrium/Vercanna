package me.natrium.vercanna.controllers;

import ch.qos.logback.classic.Logger;
import me.natrium.redis.RedisController;
import me.natrium.spotify.SpotifyController;
import me.natrium.vercanna.commands.spotify.login.LoginCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class CommandController extends ListenerAdapter {

  private List<ICommand> commands = new ArrayList<>();
  private final Logger logger;
  private final Connection connection;
  private final RedisController redis;
  private final SpotifyController spotify;

  public CommandController(Logger logger, RedisController redis, SpotifyController spotify, Connection connection) {
    this.logger = logger;
    this.redis = redis;
    this.spotify = spotify;
    this.connection = connection;
    this.registerAllCommands(
        // -- Spotify
        new LoginCommand("login","Login to spotify!")
    );
  }

  @Override
  public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    String name = event.getName();
    for (int i = this.commands.size() - 1; i >= 0; i--) {
      if(name.equals(this.commands.get(i).getName())) {
        this.commands.get(i).executeCommand(event,this.redis,this.spotify,this.connection);
      }
    }
    super.onSlashCommandInteraction(event);
  }


  private String registerAllCommands(ICommand... commands) {
    for (int i = commands.length - 1; i >= 0; i--) {
      if (commands[i].isDisabled()) {
        this.logger.warn(commands[i].getName() + " was skipped because disabled.");
        continue;
      }
      this.commands.add(commands[i]);
      this.logger.info("Added " + commands[i].getName() + " to the registers!");
    }
    return "OK";
  }
}