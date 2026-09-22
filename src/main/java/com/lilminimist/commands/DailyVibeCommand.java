package com.lilminimist.commands;

import com.lilminimist.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class DailyVibeCommand implements SlashCommand {
    private static final List<String> VIBES = List.of(
            "soft focus — do one small thing with your whole attention.",
            "creative static — let the messy first draft exist.",
            "quiet confidence — you do not need to announce the comeback.",
            "cozy chaos — flexible plans still count as plans.",
            "late-night arcade — play one more round, then drink some water."
    );

    @Override
    public net.dv8tion.jda.api.interactions.commands.build.CommandData commandData() {
        return Commands.slash("dailyvibe", "Get a small daily mood from the nook.");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String vibe = VIBES.get(ThreadLocalRandom.current().nextInt(VIBES.size()));
        event.replyEmbeds(EmbedUtils.cozy("today's nook mood", vibe)
                .setFooter("A new little vibe whenever you need one.")
                .build()).queue();
    }
}