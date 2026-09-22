package com.lilminimist.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.awt.Color;
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;

public final class UserInfoCommand implements SlashCommand {
    private static final Color BURGUNDY = new Color(126, 35, 55);
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy").withZone(ZoneOffset.UTC);

    @Override
    public net.dv8tion.jda.api.interactions.commands.build.CommandData commandData() {
        return Commands.slash("userinfo", "Show public information about a server member.")
                .addOption(OptionType.USER, "user", "The user to look up.", true);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        User user = event.getOption("user").getAsUser();
        event.deferReply().queue();

        if (event.getGuild() == null) {
            event.getHook().editOriginal("This command only works inside a server.").queue();
            return;
        }

        event.getGuild().retrieveMemberById(user.getIdLong()).queue(
                member -> event.getHook().editOriginalEmbeds(buildEmbed(user, member).build()).queue(),
                error -> event.getHook().editOriginalEmbeds(buildEmbed(user, null).build()).queue()
        );
    }

    private EmbedBuilder buildEmbed(User user, Member member) {
        String displayName = member == null ? user.getName() : member.getEffectiveName();
        String joinedAt = member == null
                ? "Not available"
                : DATE_FORMAT.format(member.getTimeJoined());

        return new EmbedBuilder()
                .setColor(BURGUNDY)
                .setTitle(displayName)
                .setThumbnail(user.getEffectiveAvatarUrl())
                .addField("Username", user.getAsTag(), true)
                .addField("Display name", displayName, true)
                .addField("Account created", DATE_FORMAT.format(user.getTimeCreated()), true)
                .addField("Joined this server", joinedAt, true)
                .setFooter("Public Discord information only.");
    }
}