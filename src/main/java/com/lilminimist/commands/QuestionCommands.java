package com.lilminimist.commands;

import com.lilminimist.utils.EmbedUtils;
import com.lilminimist.utils.QuestionBank;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class QuestionCommands {
    private static final List<String> TRUTH_CATEGORIES = List.of(
            "random", "funny", "chaotic", "awkward", "exposing", "school", "gaming", "music",
            "creative", "friendship", "couple", "deep", "social", "text", "emoji", "confidence",
            "improv", "soft", "goth", "genz", "sarcastic", "extreme"
    );
    private static final List<String> NEVER_CATEGORIES = List.of(
            "random", "funny", "chaotic", "school", "gaming", "music", "friendship", "awkward",
            "creative", "food", "travel", "internet", "genz", "wholesome", "deep"
    );
    private final QuestionBank questionBank;

    public QuestionCommands(QuestionBank questionBank) {
        this.questionBank = questionBank;
    }

    public List<SlashCommand> createAll() {
        List<SlashCommand> commands = new ArrayList<>();
        commands.add(question("truth", "Get a categorized truth prompt.", TRUTH_CATEGORIES));
        commands.add(question("dare", "Get a categorized, safe dare prompt.", TRUTH_CATEGORIES));
        commands.add(question("neverhaveiever", "Get a categorized Never Have I Ever prompt.", NEVER_CATEGORIES));
        commands.add(question("wouldyourather", "Get a categorized Would You Rather prompt.", List.of(
                "random", "funny", "chaotic", "school", "gaming", "music", "friendship", "creative", "wholesome", "deep"
        )));
        commands.add(new SimpleSlashCommand(
                Commands.slash("animalfact", "Get a short animal fact.")
                        .addOption(OptionType.STRING, "animal", "Optional animal, such as cat or octopus.", false),
                event -> {
                    String animal = event.getOption("animal") == null ? null : event.getOption("animal").getAsString();
                    event.replyEmbeds(EmbedUtils.cozy("animal fact", questionBank.animalFact(animal)).build()).queue();
                }));
        commands.add(question("icebreaker", "Get a conversation starter.", List.of("random", "funny", "music", "gaming", "creative", "wholesome")));
        commands.add(question("deepquestion", "Get a thoughtful question.", List.of("random", "friendship", "creative", "deep", "soft")));
        commands.add(question("funquestion", "Get a fun conversation question.", List.of("random", "funny", "gaming", "music", "food", "chaotic")));
        commands.add(question("randomquestion", "Get a random conversation question.", List.of("random")));
        commands.add(question("conversation", "Get a conversation prompt.", List.of("random", "music", "books", "creativity", "friendship", "gaming")));
        return commands;
    }

    private SlashCommand question(String name, String description, List<String> categories) {
        OptionData category = new OptionData(OptionType.STRING, "category", "Choose a prompt category.", false);
        categories.forEach(value -> category.addChoice(display(value), value));
        return new SimpleSlashCommand(
                Commands.slash(name, description).addOptions(category),
                event -> {
                    String selected = event.getOption("category") == null
                            ? "random"
                            : event.getOption("category").getAsString();
                    String bank = name.equals("neverhaveiever") ? "neverhaveiever" : name;
                    String prompt = questionBank.random(bank, selected, event.getUser().getIdLong());
                    event.reply(prompt).queue();
                });
    }

    private static String display(String value) {
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }
}