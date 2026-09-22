package com.lilminimist.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class VibeCommand implements SlashCommand {
    private record Vibe(String mood, String response) {
    }

    private static final List<Vibe> VIBES = List.of(
            new Vibe("soft focus", "low lights, good music, zero rush."),
            new Vibe("creative static", "your brain has ideas. let them be a little messy."),
            new Vibe("main character energy", "go make the next small thing count."),
            new Vibe("cozy chaos", "the plan is flexible. the vibe is not."),
            new Vibe("quiet confidence", "you do not need to announce the comeback."),
            new Vibe("late-night arcade", "one more round, then maybe sleep.")
    );

    @Override
    public net.dv8tion.jda.api.interactions.commands.build.CommandData commandData() {
        return Commands.slash("vibe", "Get a random mood for the moment.");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Vibe vibe = VIBES.get(ThreadLocalRandom.current().nextInt(VIBES.size()));
        event.reply("**" + vibe.mood() + "**\n" + vibe.response()).queue();
    }
}