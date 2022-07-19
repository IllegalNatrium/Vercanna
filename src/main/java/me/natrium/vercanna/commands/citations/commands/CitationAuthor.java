package me.natrium.vercanna.commands.citations.commands;

import me.natrium.layouts.CitationLayout;
import me.natrium.vercanna.commands.citations.CitationSubCommand;
import me.natrium.vercanna.commands.citations.CitationUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class CitationAuthor extends CitationSubCommand {
  public CitationAuthor(@NotNull String name, @NotNull String description) {
    super(name, description);
    this.addOption(
        OptionType.STRING,
        "author",
        "Type an author!",
        true
    );
  }

  @Override
  public void executeCommand(SlashCommandInteractionEvent event, Connection connection) {
    String author = event.getOption("author").getAsString();

    if (CitationUtils.getAuthors(connection)
        .stream()
        .noneMatch(o -> o.getName().equals(author))) {
      event.deferReply(true)
          .setContent("**404** Not found")
          .queue();
      return;
    }

    StringBuffer buff = new StringBuffer();
    List<CitationLayout> citations = CitationUtils.getCitationsByAuthor(author, connection);

    for  (int i = citations.size() - 1; i >= 0; i--) {
      // citation | createdAt
      buff.append(citations.get(i).getCitation())
          .append(" | ")
          .append(citations.get(i).getCreatedAt())
          .append("\n");
    }

    MessageEmbed embed = new EmbedBuilder()
        .setAuthor(
            event.getGuild().getName(),
            event.getGuild().getIconUrl(),
            event.getGuild().getIconUrl()
        )
        .addField("Citations",buff.toString(),true)
        .setColor(Color.CYAN)
        .build();

    event.deferReply()
        .addEmbeds(embed)
        .queue();
  }
}
