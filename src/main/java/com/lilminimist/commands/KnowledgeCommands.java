package com.lilminimist.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public final class KnowledgeCommands {
    private KnowledgeCommands() {
    }

    public static List<SlashCommand> createAll() {
        List<SlashCommand> commands = new ArrayList<>();
        commands.add(simple("sciencefact", "Get a safe built-in science fact.", event -> event.reply(random(List.of(
                "Lightning can heat the air around it to temperatures hotter than the surface of the sun.",
                "Honey can remain edible for an extremely long time when sealed properly.",
                "A day on Venus is longer than a Venusian year."
        ))).queue()));
        commands.add(simple("spacefact", "Get a safe space fact.", event -> event.reply(random(List.of(
                "A year on Mercury is only 88 Earth days.",
                "Neutron stars are incredibly dense remnants of massive stars.",
                "Saturn would float in water if a bathtub large enough existed."
        ))).queue()));
        commands.add(simple("geography", "Get a geography fact.", event -> event.reply(random(List.of(
                "Africa is the only continent that spans all four hemispheres.",
                "The Pacific Ocean is larger than all of Earth's land area combined.",
                "There are more trees on Earth than stars in the Milky Way is a commonly cited estimate."
        ))).queue()));
        commands.add(simple("history", "Get a small history prompt.", event -> event.reply(random(List.of(
                "Which historical object would you most want to see in person?",
                "What everyday technology from the past seems most surprising now?",
                "Choose a historical period and imagine its ideal playlist."
        ))).queue()));
        commands.add(simple("riddle", "Get a gentle riddle.", event -> event.reply(random(List.of(
                "I have pages but no voice, and I can take you anywhere. What am I? A book.",
                "I get wetter as I dry. What am I? A towel.",
                "I have a face and two hands but no arms. What am I? A clock."
        ))).queue()));
        commands.add(simple("word", "Get an interesting word.", event -> event.reply(random(List.of(
                "**sonder** — the realization that everyone has a life as vivid as your own.",
                "**petrichor** — the earthy scent after rain.",
                "**limerence** — an intense, often sudden infatuation.",
                "**mellifluous** — pleasingly smooth to hear."
        ))).queue()));
        return commands;
    }

    private static SlashCommand simple(String name, String description, Consumer<SlashCommandInteractionEvent> action) {
        return new SimpleSlashCommand(Commands.slash(name, description), action);
    }

    private static <T> T random(List<T> values) {
        return values.get(ThreadLocalRandom.current().nextInt(values.size()));
    }
}