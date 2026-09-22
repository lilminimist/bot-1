package com.lilminimist.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class PeopleCommands {
    private PeopleCommands() {
    }

    public static List<SlashCommand> createAll() {
        List<SlashCommand> commands = new ArrayList<>();
        commands.add(new SimpleSlashCommand(Commands.slash("membercount", "Show the server member count."), event -> {
            if (event.getGuild() == null) {
                event.reply("This command only works inside a server.").setEphemeral(true).queue();
                return;
            }
            event.reply("this server has **" + event.getGuild().getMemberCount() + "** members.").queue();
        }));
        commands.add(new SimpleSlashCommand(Commands.slash("rolecount", "Show the server role count."), event -> {
            if (event.getGuild() == null) {
                event.reply("This command only works inside a server.").setEphemeral(true).queue();
                return;
            }
            event.reply("this server has **" + event.getGuild().getRoles().size() + "** roles.").queue();
        }));
        commands.add(new SimpleSlashCommand(
                Commands.slash("accountage", "Show a user's account age.")
                        .addOption(OptionType.USER, "user", "The user to inspect.", true),
                event -> {
                    User user = event.getOption("user").getAsUser();
                    long days = Duration.between(user.getTimeCreated(), Instant.now()).toDays();
                    event.reply("**" + user.getEffectiveName() + "** has been on Discord for about **" + days + " days**.").queue();
                }));
        commands.add(new SimpleSlashCommand(
                Commands.slash("joined", "Show when a member joined this server.")
                        .addOption(OptionType.USER, "user", "The member to inspect.", true),
                PeopleCommands::replyJoinedDate));
        return commands;
    }

    private static void replyJoinedDate(SlashCommandInteractionEvent event) {
        if (event.getGuild() == null) {
            event.reply("This command only works inside a server.").setEphemeral(true).queue();
            return;
        }
        User user = event.getOption("user").getAsUser();
        event.deferReply().queue();
        event.getGuild().retrieveMemberById(user.getIdLong()).queue(
                member -> editJoined(event, member),
                error -> event.getHook().editOriginal("Discord did not make this member's join date available.").queue()
        );
    }

    private static void editJoined(SlashCommandInteractionEvent event, Member member) {
        event.getHook().editOriginal("**" + member.getEffectiveName() + "** joined this server on **" +
                member.getTimeJoined().toLocalDate() + "**.").queue();
    }
}