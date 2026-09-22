package com.lilminimist.commands;

import com.lilminimist.utils.EmbedUtils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class QuoteCommand implements SlashCommand {
    private static final List<String> QUOTES = List.of(
            "You can be a work in progress and still be worth celebrating.",
            "A quiet idea is still an idea. Give it somewhere to land.",
            "You do not need a perfect mood to make a meaningful thing.",
            "Small rituals become landmarks when you keep returning to them.",
            "The soft version of brave is still brave."
    );

    @Override
    public net.dv8tion.jda.api.interactions.commands.build.CommandData commandData() {
        return Commands.slash("quote", "Get a short original nook quote.");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.replyEmbeds(EmbedUtils.cozy("a little thought", "“" + QUOTES.get(
                ThreadLocalRandom.current().nextInt(QUOTES.size())) + "”").build()).queue();
    }
}