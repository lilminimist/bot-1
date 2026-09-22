package com.lilminimist.commands;

import com.lilminimist.utils.EmbedUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;

public final class ServerInfoCommand implements SlashCommand {
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy").withZone(ZoneOffset.UTC);

    @Override
    public net.dv8tion.jda.api.interactions.commands.build.CommandData commandData() {
        return Commands.slash("serverinfo", "Show a clean server overview.");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null) {
            event.reply("This command only works inside a server.").setEphemeral(true).queue();
            return;
        }

        event.replyEmbeds(EmbedUtils.cozy(guild.getName(), "A quick look at this corner of the nook.")
                .setThumbnail(guild.getIconUrl())
                .addField("Members", String.valueOf(guild.getMemberCount()), true)
                .addField("Channels", String.valueOf(guild.getChannels().size()), true)
                .addField("Roles", String.valueOf(guild.getRoles().size()), true)
                .addField("Boosts", String.valueOf(guild.getBoostCount()), true)
                .addField("Created", DATE_FORMAT.format(guild.getTimeCreated()), true)
                .addField("Owner", guild.getOwner() == null ? "Not available" : guild.getOwner().getEffectiveName(), true)
                .build()).queue();
    }
}