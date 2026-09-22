package com.lilminimist.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public final class FunCommands {
    private static final List<String> EIGHT_BALL = List.of(
            "yes, but do not overthink it.",
            "the vibes say no for now.",
            "ask again after a snack.",
            "very likely.",
            "the answer is hiding in plain sight.",
            "not impossible. not recommended."
    );
    private static final List<String> COMPLIMENTS = List.of(
            "You make the room feel less awkward just by being in it.",
            "Your taste is suspiciously good.",
            "You have excellent side-character-to-main-character growth.",
            "You are doing better than the dramatic soundtrack suggests.",
            "Your existence adds texture to the timeline."
    );
    private static final List<String> LIGHT_ROASTS = List.of(
            "You have the energy of a tab left open for three months.",
            "Your plan is brave. Your plan is also absolutely improvising.",
            "You are not late; you are on a mysterious alternate schedule.",
            "That idea has potential and several loose cables.",
            "You bring premium chaos at a free-trial price."
    );
    private static final List<String> TRUTHS = List.of(
            "What song do you secretly know every word to?",
            "What tiny thing instantly improves your mood?",
            "Which fictional world would you move into for a week?",
            "What hobby would you try if nobody judged the first attempt?"
    );
    private static final List<String> DARES = List.of(
            "Describe your day using only three dramatic movie titles.",
            "Send a message with exactly five words and make it sound mysterious.",
            "Invent a new holiday and explain its main tradition.",
            "Rename yourself in your head like a character in a cozy game."
    );
    private static final List<String> JOKES = List.of(
            "I tried to write a book about gravity. It was impossible to put down.",
            "My playlist and I have a complicated relationship. It keeps bringing up old tracks.",
            "I told my keyboard a secret. Now it has too many spaces.",
            "The moon is a terrible listener. It always gives the same phase."
    );
    private static final List<String> FACTS = List.of(
            "Octopuses have three hearts.",
            "Bananas are berries, but strawberries are not botanical berries.",
            "A group of flamingos is called a flamboyance.",
            "The fingerprints of a koala are surprisingly similar to human fingerprints.",
            "Sea otters hold hands while sleeping so they do not drift apart."
    );

    private FunCommands() {
    }

    public static List<SlashCommand> createAll() {
        List<SlashCommand> commands = new ArrayList<>();
        commands.add(text("8ball", "Ask the little digital void a question.", event ->
                event.reply(random(EIGHT_BALL)).queue()));
        commands.add(text("coinflip", "Flip a coin.", event ->
                event.reply(random(List.of("heads", "tails"))).queue()));
        commands.add(command(Commands.slash("roll", "Roll a die.")
                        .addOption(OptionType.INTEGER, "sides", "Number of sides, from 2 to 1000.", false),
                event -> {
                    long sides = optionLong(event, "sides", 6);
                    sides = Math.max(2, Math.min(1000, sides));
                    event.reply("rolled **" + (ThreadLocalRandom.current().nextLong(sides) + 1) + "** on a d" + sides + ".").queue();
                }));
        commands.add(command(Commands.slash("dice", "Roll two six-sided dice."), event ->
                event.reply("🎲 **" + die() + " + " + die() + "**").queue()));
        commands.add(command(Commands.slash("number", "Pick a random number from 1 to 100."), event ->
                event.reply("the number is **" + number(1, 100) + "**.").queue()));
        commands.add(command(Commands.slash("random", "Get a random low-stakes decision."), event ->
                event.reply(random(List.of("do it", "wait a little", "ask a friend", "make tea first", "choose the weird option"))).queue()));
        commands.add(command(Commands.slash("flip", "Flip a yes-or-no decision."), event ->
                event.reply(random(List.of("yes", "no"))).queue()));
        commands.add(command(Commands.slash("choose", "Choose from a comma-separated list.")
                        .addOption(OptionType.STRING, "choices", "Example: tea, coffee, water", true),
                event -> {
                    String[] choices = event.getOption("choices").getAsString().split(",");
                    List<String> cleaned = java.util.Arrays.stream(choices)
                            .map(String::trim).filter(value -> !value.isBlank()).toList();
                    event.reply(cleaned.isEmpty() ? "Give me at least one choice." : "the nook chooses **" + random(cleaned) + "**.").queue();
                }));
        commands.add(text("thisorthat", "Pick between two cozy options.", event ->
                event.reply(random(List.of("books or films?", "sunrise or midnight?", "headphones or speakers?", "sweet or salty?"))).queue()));
        commands.add(command(Commands.slash("rate", "Give a playful rating.")
                        .addOption(OptionType.STRING, "subject", "What should be rated?", true),
                event -> {
                    String subject = event.getOption("subject").getAsString();
                    event.reply("**" + subject + "** gets **" + number(1, 100) + "/100**. The committee has spoken.").queue();
                }));
        commands.add(command(Commands.slash("ship", "Give two people a playful compatibility score.")
                        .addOption(OptionType.USER, "first", "First person.", true)
                        .addOption(OptionType.USER, "second", "Second person.", true),
                event -> {
                    User first = event.getOption("first").getAsUser();
                    User second = event.getOption("second").getAsUser();
                    event.reply(first.getEffectiveName() + " + " + second.getEffectiveName() + " = **" + number(1, 100) + "%** cosmic compatibility.").queue();
                }));
        commands.add(command(Commands.slash("compatibility", "Check two users' playful compatibility.")
                        .addOption(OptionType.USER, "first", "First person.", true)
                        .addOption(OptionType.USER, "second", "Second person.", true),
                event -> {
                    User first = event.getOption("first").getAsUser();
                    User second = event.getOption("second").getAsUser();
                    event.reply("compatibility scan: **" + first.getEffectiveName() + " × " + second.getEffectiveName() + " = " + number(1, 100) + "%**.").queue();
                }));
        commands.add(text("compliment", "Get a small compliment.", event -> event.reply(random(COMPLIMENTS)).queue()));
        commands.add(text("roast", "Get a gentle, playful roast.", event -> event.reply(random(LIGHT_ROASTS)).queue()));
        commands.add(text("mood", "Get a random mood label.", event ->
                event.reply("current mood: **" + random(List.of("soft static", "cozy chaos", "quietly iconic", "rainy-window brain", "playlist protagonist")) + "**").queue()));
        commands.add(text("fortune", "Get a tiny fortune.", event ->
                event.reply("fortune: **" + random(List.of(
                        "a good idea will find you while doing something else.",
                        "the next small step is enough.",
                        "someone is glad you showed up.",
                        "your future self is rooting for the practical choice."
                )) + "**").queue()));
        commands.add(command(Commands.slash("rps", "Play rock, paper, scissors.")
                        .addOption(OptionType.STRING, "choice", "rock, paper, or scissors", true),
                event -> {
                    String userChoice = event.getOption("choice").getAsString().trim().toLowerCase();
                    if (!List.of("rock", "paper", "scissors").contains(userChoice)) {
                        event.reply("Choose `rock`, `paper`, or `scissors`.").setEphemeral(true).queue();
                        return;
                    }
                    String botChoice = random(List.of("rock", "paper", "scissors"));
                    String result = userChoice.equals(botChoice) ? "tie" :
                            (userChoice.equals("rock") && botChoice.equals("scissors"))
                                    || (userChoice.equals("paper") && botChoice.equals("rock"))
                                    || (userChoice.equals("scissors") && botChoice.equals("paper"))
                                    ? "you win" : "the nook wins";
                    event.reply("you chose **" + userChoice + "**; the nook chose **" + botChoice + "**. **" + result + "**.").queue();
                }));
        commands.add(text("magic", "Get a little magic answer.", event ->
                event.reply("the magic says: **" + random(List.of("follow the interesting path", "not yet", "yes, with better snacks", "trust the weird idea")) + "**").queue()));
        commands.add(command(Commands.slash("guess", "Guess a number from 1 to 10.")
                        .addOption(OptionType.INTEGER, "guess", "Your guess.", true),
                event -> {
                    long guess = event.getOption("guess").getAsLong();
                    long answer = number(1, 10);
                    event.reply("the nook picked **" + answer + "**. " + (guess == answer ? "you got it." : "close enough for a first try.")).queue();
                }));
        commands.add(text("fact", "Get a safe built-in fact.", event -> event.reply(random(FACTS)).queue()));
        commands.add(text("joke", "Get a clean built-in joke.", event -> event.reply(random(JOKES)).queue()));
        commands.add(text("pun", "Get a tiny pun.", event -> event.reply(random(List.of("I am reading a book about anti-gravity. It is impossible to put down.", "I wanted to tell a time-travel joke, but you did not like it.", "The future, the present, and the past walked into a bar. It was tense."))).queue()));
        commands.add(text("dadjoke", "Get a clean dad joke.", event -> event.reply(random(List.of("What do you call a fake noodle? An impasta.", "Why did the scarecrow win an award? It was outstanding in its field.", "I only know 25 letters of the alphabet. I do not know y."))).queue()));
        commands.add(text("pickup", "Get a harmless, goofy pickup line.", event -> event.reply(random(List.of("Are you a playlist? Because you are exactly my type.", "You must be a camera, because every time I see you I smile.", "Are you made of stardust? This is a very scientific compliment."))).queue()));
        commands.add(text("nickname", "Get a cozy nickname.", event -> event.reply("your nook nickname is **" + random(List.of("midnight moth", "velvet pixel", "soft menace", "library ghost", "burgundy comet", "tiny thunder")) + "**.").queue()));
        commands.add(command(Commands.slash("reverse", "Reverse a short piece of text.")
                        .addOption(OptionType.STRING, "text", "Text to reverse.", true),
                event -> event.reply(new StringBuilder(event.getOption("text").getAsString()).reverse().toString()).queue()));
        commands.add(command(Commands.slash("mock", "Get a gentle, playful mock response.")
                        .addOption(OptionType.STRING, "target", "What should be lightly mocked?", true),
                event -> event.reply("**" + event.getOption("target").getAsString() + "** has been lovingly placed in the tiny clown car.").queue()));
        commands.add(text("howchaotic", "Measure the current chaos.", event -> percentage(event, "chaos")));
        commands.add(text("howlucky", "Measure the current luck.", event -> percentage(event, "luck")));
        commands.add(text("howbased", "Measure the current basedness.", event -> percentage(event, "basedness")));
        commands.add(text("howtired", "Measure the current tiredness.", event -> percentage(event, "tiredness")));
        commands.add(text("howbored", "Measure the current boredom.", event -> percentage(event, "boredom")));
        commands.add(text("howmaincharacter", "Measure the current main-character energy.", event -> percentage(event, "main-character energy")));
        return commands;
    }

    private static SlashCommand text(String name, String description, Consumer<SlashCommandInteractionEvent> action) {
        return command(Commands.slash(name, description), action);
    }

    private static SlashCommand command(CommandData data, Consumer<SlashCommandInteractionEvent> action) {
        return new SimpleSlashCommand(data, action);
    }

    private static void percentage(SlashCommandInteractionEvent event, String label) {
        event.reply(label + ": **" + number(1, 100) + "%**.").queue();
    }

    private static long optionLong(SlashCommandInteractionEvent event, String name, long fallback) {
        return event.getOption(name) == null ? fallback : event.getOption(name).getAsLong();
    }

    private static int die() {
        return (int) number(1, 6);
    }

    private static int number(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private static <T> T random(List<T> values) {
        return values.get(ThreadLocalRandom.current().nextInt(values.size()));
    }
}