package com.lilminimist.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.Objects;
import java.util.function.Consumer;

public final class SimpleSlashCommand implements SlashCommand {
    private final CommandData data;
    private final Consumer<SlashCommandInteractionEvent> action;

    public SimpleSlashCommand(CommandData data, Consumer<SlashCommandInteractionEvent> action) {
        this.data = Objects.requireNonNull(data);
        this.action = Objects.requireNonNull(action);
    }

    @Override
    public CommandData commandData() {
        return data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        action.accept(event);
    }
}