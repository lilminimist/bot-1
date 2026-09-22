package com.lilminimist.commands;

import com.lilminimist.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public final class ServerBannerCommand implements SlashCommand {
    @Override
    public net.dv8tion.jda.api.interactions.commands.build.CommandData commandData() {
        return Commands.slash("serverbanner", "Display the server banner when available.");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (event.getGuild() == null || event.getGuild().getBannerUrl() == null) {
            event.reply("This server does not have a banner available.").setEphemeral(true).queue();
            return;
        }
        event.replyEmbeds(EmbedUtils.cozy(event.getGuild().getName() + " banner", null)
                .setImage(event.getGuild().getBannerUrl())
                .build()).queue();
    }
}