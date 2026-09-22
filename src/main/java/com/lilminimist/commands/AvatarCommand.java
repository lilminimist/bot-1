package com.lilminimist.commands;

import com.lilminimist.utils.EmbedUtils;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public final class AvatarCommand implements SlashCommand {
    @Override
    public net.dv8tion.jda.api.interactions.commands.build.CommandData commandData() {
        return Commands.slash("avatar", "Display a user's avatar.")
                .addOption(OptionType.USER, "user", "The user whose avatar you want to see.", true);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        User user = event.getOption("user").getAsUser();
        String fullSizeUrl = fullSizeUrl(user.getEffectiveAvatarUrl());
        event.replyEmbeds(EmbedUtils.cozy(user.getEffectiveName() + "'s avatar", "Full-resolution avatar preview.")
                .setTitle(user.getEffectiveName() + "'s avatar")
                .setImage(fullSizeUrl)
                .setFooter("Requested by " + event.getUser().getEffectiveName())
                .build())
                .addActionRow(
                        Button.link(fullSizeUrl, "🖼️ Open Full Size"),
                        Button.link(fullSizeUrl, "⬇️ Download"))
                .queue();
    }

    private static String fullSizeUrl(String avatarUrl) {
        if (avatarUrl == null || avatarUrl.isBlank()) {
            return "https://cdn.discordapp.com/embed/avatars/0.png?size=4096";
        }
        if (avatarUrl.matches(".*[?&]size=\\d+.*")) {
            return avatarUrl.replaceFirst("([?&]size=)\\d+", "$14096");
        }
        return avatarUrl + (avatarUrl.contains("?") ? "&size=4096" : "?size=4096");
    }
}