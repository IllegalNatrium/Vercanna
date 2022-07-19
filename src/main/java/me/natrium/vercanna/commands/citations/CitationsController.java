package me.natrium.vercanna.commands.citations;

import me.natrium.vercanna.commands.citations.commands.CitationAuthor;
import me.natrium.vercanna.commands.citations.commands.CitationAuthors;
import me.natrium.vercanna.controllers.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class CitationsController extends ICommand {

  List<CitationSubCommand> commands = new ArrayList<>();

  public CitationsController(@NotNull String name, @NotNull String description) {
    super(name, description);
    this.registerCommands(
        new CitationAuthors("authors", "Citation authors!"),
        new CitationAuthor("author","Citation author!")
    );
    this.addSubcommands(commands);
  }

  @Override
  public void executeCommand(SlashCommandInteractionEvent event, Connection connection) {
    for (int i = commands.size() - 1; i >= 0; i--) {
      if(event.getSubcommandName().equals(commands.get(i).getName())) {
        commands.get(i).executeCommand(
            event,
            connection
        );
      }
    }
  }

  private boolean registerCommands(CitationSubCommand... commands) {
    for (int i = commands.length - 1; i >= 0; i--) {
      this.commands.add(commands[i]);
    }
    return true;
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
