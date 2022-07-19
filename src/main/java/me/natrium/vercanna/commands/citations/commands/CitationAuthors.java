package me.natrium.vercanna.commands.citations.commands;

import me.natrium.layouts.CitationAuthorLayout;
import me.natrium.vercanna.commands.citations.CitationSubCommand;
import me.natrium.vercanna.commands.citations.CitationUtils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.List;

public class CitationAuthors extends CitationSubCommand {
  public CitationAuthors(@NotNull String name, @NotNull String description) {
    super(name, description);
  }

  @Override
  public void executeCommand(SlashCommandInteractionEvent event, Connection connection) {
    StringBuffer buf = new StringBuffer();
    List<CitationAuthorLayout> authors = CitationUtils.getAuthors(connection);

    for (int i = authors.size() - 1; i >= 0; i--) {
      buf.append(authors.get(i).getName())
          .append(" `[")
          .append(authors.get(i).getManyCitations())
          .append("]`")
          .append("\n");
    }
    event.deferReply()
        .setContent(buf.toString())
        .queue();
  }
}