package com.lilminimist.commands;

import com.lilminimist.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

public final class NookCommand implements SlashCommand, ButtonHandler {
    @Override
    public net.dv8tion.jda.api.interactions.commands.build.CommandData commandData() {
        return Commands.slash("nook", "Open the LilMinimist command dashboard.");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.replyEmbeds(dashboard().build())
                .addActionRow(
                        Button.secondary("nook:community", "🖤 Community"),
                        Button.secondary("nook:fun", "🎲 Fun"),
                        Button.secondary("nook:music", "🎧 Music"))
                .addActionRow(
                        Button.secondary("nook:people", "👤 People"),
                        Button.secondary("nook:utility", "🛠 Utility"),
                        Button.secondary("nook:identity", "🌙 LilMinimist"))
                .queue();
    }

    @Override
    public boolean handleButton(ButtonInteractionEvent event) {
        if (!event.getComponentId().startsWith("nook:")) {
            return false;
        }

        String category = event.getComponentId().substring("nook:".length());
        event.replyEmbeds(categoryEmbed(category).build()).setEphemeral(true).queue();
        return true;
    }

    private EmbedBuilder dashboard() {
        return EmbedUtils.cozy("the lilminimist nook", "Pick a corner and see what is waiting there.")
                .addField("Community", "Prompts, polls, and little shared moments.", false)
                .addField("Fun", "Low-stakes games, randomizers, and soft chaos.", false)
                .addField("Music", "Song prompts and mood-based listening ideas.", false)
                .addField("People", "Public profile and server details.", false)
                .addField("Utility", "Small tools that do one thing cleanly.", false)
                .addField("LilMinimist", "Vibes, midnight thoughts, and nook identity.", false)
                .setFooter("Use a category button to open its command list.");
    }

    private EmbedBuilder categoryEmbed(String category) {
        return switch (category) {
            case "community" -> EmbedUtils.cozy("community", "`/poll` — make a quick vote\n`/qotd` — question of the day\n`/icebreaker` — start a conversation");
            case "fun" -> EmbedUtils.cozy("fun", "`/8ball` — ask the void\n`/coinflip` — heads or tails\n`/roll` — roll a die\n`/choose` — let chance decide\n`/rps` — rock, paper, scissors\n`/rate` — rate a thing");
            case "music" -> EmbedUtils.cozy("music", "`/song` — get a listening prompt\n`/musicmood` — a mood to soundtrack\n`/musicquestion` — talk about music");
            case "people" -> EmbedUtils.cozy("people", "`/userinfo` — public user info\n`/avatar` — view an avatar\n`/membercount` — server members\n`/accountage` — account age");
            case "utility" -> EmbedUtils.cozy("utility", "`/randomnumber` — number in a range\n`/randomchoice` — choose from text\n`/color` — a random color\n`/channelinfo` — inspect a channel");
            case "identity" -> EmbedUtils.cozy("lilminimist", "`/about` — bot identity\n`/dailyvibe` — today’s nook mood\n`/midnight` — late-night mode\n`/quote` — a small original thought\n`/serverinfo` — server overview");
            default -> EmbedUtils.cozy("nook", "That corner is still being arranged.");
        };
    }
}