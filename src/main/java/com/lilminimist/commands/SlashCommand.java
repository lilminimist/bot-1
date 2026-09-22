package com.lilminimist.commands;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface SlashCommand {
    CommandData commandData();

    void execute(SlashCommandInteractionEvent event);
}