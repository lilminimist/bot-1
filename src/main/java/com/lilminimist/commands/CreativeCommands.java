package com.lilminimist.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public final class CreativeCommands {
    private CreativeCommands() {
    }

    public static List<SlashCommand> createAll() {
        List<SlashCommand> commands = new ArrayList<>();
        commands.add(simple("prompt", "Get a creative prompt.", event -> event.reply(random(List.of(
                "Describe a room that remembers everyone who visits.",
                "Make something using only three shapes.",
                "Write about a message that arrives ten years late."
        ))).queue()));
        commands.add(simple("writingprompt", "Get a writing prompt.", event -> event.reply(random(List.of(
                "A character finds a key labeled with their own name.",
                "Two strangers keep receiving the same anonymous book.",
                "The city turns one color every time someone tells the truth."
        ))).queue()));
        commands.add(simple("storyprompt", "Get a story prompt.", event -> event.reply(random(List.of(
                "A tiny neighborhood shop sells memories by accident.",
                "A storm reveals a staircase that was not there yesterday.",
                "The last person awake in the city hears the buildings whisper."
        ))).queue()));
        commands.add(simple("poemprompt", "Get a poetry prompt.", event -> event.reply(random(List.of(
                "Write about a sound that feels like a color.",
                "Start with: “I kept the light on for the version of me that left.”",
                "Describe missing someone without using the words miss or gone."
        ))).queue()));
        commands.add(simple("drawingprompt", "Get a drawing prompt.", event -> event.reply(random(List.of(
                "A moonlit vending machine for impossible snacks.",
                "A tiny ghost doing a very serious office job.",
                "A city bus driven by a moth with a map."
        ))).queue()));
        commands.add(simple("photographyprompt", "Get a photography prompt.", event -> event.reply(random(List.of(
                "Find three textures that look like landscapes.",
                "Photograph a reflection without showing the reflective surface.",
                "Capture a shadow that tells a story."
        ))).queue()));
        commands.add(simple("randomaesthetic", "Get a random aesthetic direction.", event -> event.reply("aesthetic: **" + random(List.of(
                "rainy library", "burgundy arcade", "moonlit greenhouse", "old camera shop", "sticker-covered notebook"
        )) + "**").queue()));
        commands.add(simple("colorpalette", "Get a small color palette.", event -> event.reply("palette: **" + random(List.of(
                "#241F24 · #7E2337 · #C28B94", "#17191F · #5B456B · #D5B7D0",
                "#211E2B · #8A5A44 · #E2C7A7", "#191F24 · #496B6A · #B2D1C7"
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