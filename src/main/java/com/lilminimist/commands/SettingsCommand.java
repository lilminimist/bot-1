package com.lilminimist.commands;

import com.lilminimist.utils.PreferenceStore;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public final class SettingsCommand implements SlashCommand {
    private final PreferenceStore preferences;

    public SettingsCommand(PreferenceStore preferences) {
        this.preferences = preferences;
    }

    @Override
    public net.dv8tion.jda.api.interactions.commands.build.CommandData commandData() {
        OptionData style = new OptionData(OptionType.STRING, "style", "The response style to use.", false);
        List.of("soft", "soft_goth", "genz", "chaotic", "sarcastic", "mix")
                .forEach(value -> style.addChoice(display(value), value));
        return Commands.slash("settings", "Change your private LilMinimist preferences.")
                .addOptions(style);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (event.getOption("style") == null) {
            event.reply("Your current vibe is **" + display(preferences.styleFor(event.getUser().getIdLong())) +
                    "**. Choose a style with `/settings style:<name>` or use `/setup`.").setEphemeral(true).queue();
            return;
        }
        String style = event.getOption("style").getAsString();
        preferences.setStyle(event.getUser().getIdLong(), style);
        event.reply("Saved your vibe as **" + display(style) + "**.").setEphemeral(true).queue();
    }

    private static String display(String style) {
        return switch (style) {
            case "soft_goth" -> "Soft Goth";
            case "genz" -> "Gen Z";
            case "mix" -> "Mix Everything";
            default -> style.substring(0, 1).toUpperCase() + style.substring(1);
        };
    }
}