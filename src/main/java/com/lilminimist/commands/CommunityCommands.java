package com.lilminimist.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import com.lilminimist.utils.QuestionBank;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public final class CommunityCommands {
    private CommunityCommands() {
    }

    public static List<SlashCommand> createAll(QuestionBank questionBank) {
        List<SlashCommand> commands = new ArrayList<>();
        commands.add(command(Commands.slash("qotd", "Post a safe Question of the Day."), event ->
                event.reply("question of the day: **" +
                        questionBank.random("qotd", "random", event.getUser().getIdLong()) + "**").queue()));
        commands.add(command(Commands.slash("suggest", "Submit a suggestion for the server.")
                        .addOption(OptionType.STRING, "suggestion", "Your suggestion.", true),
                event -> event.reply("Your suggestion is ready, but no suggestions channel is configured yet. Ask a server manager to configure one before submissions are published.")
                        .setEphemeral(true).queue()));
        commands.add(command(Commands.slash("confess", "Prepare an anonymous confession.")
                        .addOption(OptionType.STRING, "message", "Your confession.", true),
                event -> event.reply("Anonymous confessions are not published until a dedicated confession channel is configured. Nothing was posted.")
                        .setEphemeral(true).queue()));
        commands.add(command(Commands.slash("anonymous", "Prepare an anonymous community message.")
                        .addOption(OptionType.STRING, "message", "Your message.", true),
                event -> event.reply("Anonymous posting is disabled until a server manager configures a destination channel. Nothing was posted.")
                        .setEphemeral(true).queue()));
        commands.add(command(Commands.slash("idea", "Share a low-pressure idea.")
                        .addOption(OptionType.STRING, "idea", "Your idea.", true),
                event -> event.reply("idea noted for the nook: **" + event.getOption("idea").getAsString() + "**").queue()));
        commands.add(command(Commands.slash("topic", "Start a conversation topic.")
                        .addOption(OptionType.STRING, "topic", "Optional topic seed.", false),
                event -> {
                    String topic = event.getOption("topic") == null
                            ? random(List.of("What is a hobby you wish more people understood?", "Which fictional character would make a great roommate?", "What is your favorite kind of creative break?"))
                            : event.getOption("topic").getAsString();
                    event.reply("conversation topic: **" + topic + "**").queue();
                }));
        commands.add(command(Commands.slash("question", "Ask the nook for a community question."), event ->
                event.reply(random(List.of("What are you listening to lately?", "What is a book you still think about?", "What would you learn with unlimited time?"))).queue()));
        commands.add(command(Commands.slash("discussion", "Get a thoughtful discussion prompt."), event ->
                event.reply(random(List.of("Is a perfect first draft helpful or impossible?", "What makes a community feel welcoming?", "Do you prefer collecting experiences or objects?"))).queue()));
        return commands;
    }

    private static SlashCommand command(net.dv8tion.jda.api.interactions.commands.build.CommandData data,
                                        Consumer<SlashCommandInteractionEvent> action) {
        return new SimpleSlashCommand(data, action);
    }

    private static <T> T random(List<T> values) {
        return values.get(ThreadLocalRandom.current().nextInt(values.size()));
    }
}