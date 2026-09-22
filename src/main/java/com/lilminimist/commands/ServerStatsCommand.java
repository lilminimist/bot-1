package com.lilminimist.commands;

import com.lilminimist.utils.BotUptime;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.awt.Color;

public final class ServerStatsCommand implements SlashCommand {
    private static final Color BURGUNDY = new Color(126, 35, 55);
    private final BotUptime uptime;

    public ServerStatsCommand(BotUptime uptime) {
        this.uptime = uptime;
    }

    @Override
    public net.dv8tion.jda.api.interactions.commands.build.CommandData commandData() {
        return Commands.slash("serverstats", "See a few quick server stats.");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null) {
            event.reply("This command only works inside a server.").setEphemeral(true).queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(BURGUNDY)
                .setTitle(guild.getName())
                .addField("Members", String.valueOf(guild.getMemberCount()), true)
                .addField("Channels", String.valueOf(guild.getChannels().size()), true)
                .addField("Bot uptime", uptime.formatted(), true);

        event.replyEmbeds(embed.build()).queue();
    }
}