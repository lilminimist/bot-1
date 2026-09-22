package com.lilminimist.commands;

import com.lilminimist.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class MidnightCommand implements SlashCommand {
    private static final List<String> THOUGHTS = List.of(
            "What would you make if nobody had to see the first version?",
            "Put on one song and let it choose the next ten minutes.",
            "Write three sentences that begin with: “At 2:17, the city changed.”",
            "Name one tiny thing from today that deserved more credit.",
            "Take a photo of a shadow, a texture, or a window and call it enough."
    );

    @Override
    public net.dv8tion.jda.api.interactions.commands.build.CommandData commandData() {
        return Commands.slash("midnight", "Open a gentle late-night nook moment.");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.replyEmbeds(EmbedUtils.cozy("midnight mode", THOUGHTS.get(
                ThreadLocalRandom.current().nextInt(THOUGHTS.size()))).build()).queue();
    }
}