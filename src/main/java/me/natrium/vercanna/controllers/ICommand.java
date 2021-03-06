package me.natrium.vercanna.controllers;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

public abstract class ICommand extends CommandDataImpl {
  public ICommand(@NotNull String name, @NotNull String description) {
    super(name, description);
  }
  public abstract void executeCommand(SlashCommandInteractionEvent event, Connection connection);
  public abstract boolean isDisabled();
  public abstract boolean isStaffOnly();
}
