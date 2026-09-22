package com.lilminimist.commands;

import com.lilminimist.utils.BotUptime;
import com.lilminimist.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public final class NookStatsCommand implements SlashCommand {
    private final CommandRegistry registry;
    private final BotUptime uptime;

    public NookStatsCommand(CommandRegistry registry, BotUptime uptime) {
        this.registry = registry;
        this.uptime = uptime;
    }

    @Override
    public net.dv8tion.jda.api.interactions.commands.build.CommandData commandData() {
        return Commands.slash("nookstats", "See how much of the nook is online.");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.replyEmbeds(EmbedUtils.cozy("nook status", "The little system behind the cozy replies.")
                .addField("Registered commands", String.valueOf(registry.size()), true)
                .addField("Uptime", uptime.formatted(), true)
                .addField("Mode", "community / fun / utility", true)
                .build()).queue();
    }
}