package me.natrium.vercanna.commands.citations;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

public abstract class CitationSubCommand extends SubcommandData
{
  public CitationSubCommand(@NotNull String name, @NotNull String description) {
    super(name, description);
  }

  public abstract void executeCommand(SlashCommandInteractionEvent event, Connection connection);

}
