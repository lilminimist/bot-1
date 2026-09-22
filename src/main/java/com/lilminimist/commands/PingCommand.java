package com.lilminimist.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.awt.Color;

public final class PingCommand implements SlashCommand {
    private static final Color BURGUNDY = new Color(126, 35, 55);

    @Override
    public net.dv8tion.jda.api.interactions.commands.build.CommandData commandData() {
        return Commands.slash("ping", "Check whether lilminimist is online.");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        long latency = event.getJDA().getGatewayPing();
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(BURGUNDY)
                .setTitle("lilminimist is awake")
                .setDescription("Online and keeping the quiet hours cozy.")
                .addField("Gateway latency", latency + " ms", true);

        event.replyEmbeds(embed.build()).queue();
    }
}