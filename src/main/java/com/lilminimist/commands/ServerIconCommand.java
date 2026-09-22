package com.lilminimist.commands;

import com.lilminimist.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public final class ServerIconCommand implements SlashCommand {
    @Override
    public net.dv8tion.jda.api.interactions.commands.build.CommandData commandData() {
        return Commands.slash("servericon", "Display the server icon.");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (event.getGuild() == null || event.getGuild().getIconUrl() == null) {
            event.reply("This server does not have an icon available.").setEphemeral(true).queue();
            return;
        }
        event.replyEmbeds(EmbedUtils.cozy(event.getGuild().getName() + " icon", null)
                .setImage(event.getGuild().getIconUrl())
                .build()).queue();
    }
}