package com.lilminimist.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public final class MusicCommands {
    private MusicCommands() {
    }

    public static List<SlashCommand> createAll() {
        List<SlashCommand> commands = new ArrayList<>();
        commands.add(simple("song", "Get a built-in listening prompt.", event ->
                event.reply(random(List.of("Play one song you loved at 15.", "Find a song with a color in its title.", "Put on the song you would use for a night drive.", "Listen to something with no lyrics and notice what changes."))).queue()));
        commands.add(simple("playlist", "Get a tiny playlist concept.", event ->
                event.reply("playlist idea: **" + random(List.of("burgundy after midnight", "rain on the bus window", "soft songs for loud thoughts", "arcade lights and old headphones")) + "**").queue()));
        commands.add(simple("nowplaying", "Get a playful now-playing status.", event ->
                event.reply("now playing: **" + random(List.of("the song you forgot to skip", "a suspiciously emotional instrumental", "track 7 of the imaginary album", "the background music of your next idea")) + "**").queue()));
        commands.add(simple("musicvote", "Get a music discussion vote.", event ->
                event.reply("music vote: **albums front-to-back or playlist shuffle?**").queue()));
        commands.add(simple("musicmood", "Get a mood to soundtrack.", event ->
                event.reply("soundtrack mood: **" + random(List.of("velvet rain", "neon confidence", "quiet sunrise", "late train home", "creative mess")) + "**").queue()));
        commands.add(simple("musicquestion", "Get a question about music.", event ->
                event.reply(random(List.of("Which song feels like a place?", "What album would you save from a time capsule?", "Do you listen to lyrics first or production first?", "What is your favorite unexpected genre combination?"))).queue()));
        return commands;
    }

    private static SlashCommand simple(String name, String description, Consumer<SlashCommandInteractionEvent> action) {
        return new SimpleSlashCommand(Commands.slash(name, description), action);
    }

    private static <T> T random(List<T> values) {
        return values.get(ThreadLocalRandom.current().nextInt(values.size()));
    }
}