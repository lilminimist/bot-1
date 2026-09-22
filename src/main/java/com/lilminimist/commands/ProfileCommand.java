package com.lilminimist.commands;

import com.lilminimist.utils.EmbedUtils;
import com.lilminimist.utils.PreferenceStore;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public final class ProfileCommand implements SlashCommand {
    private final PreferenceStore preferences;

    public ProfileCommand(PreferenceStore preferences) {
        this.preferences = preferences;
    }

    @Override
    public net.dv8tion.jda.api.interactions.commands.build.CommandData commandData() {
        return Commands.slash("profile", "Show your private LilMinimist preferences.");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.replyEmbeds(EmbedUtils.cozy("your nook profile", "Only your own preference is shown here.")
                .addField("Vibe", display(preferences.styleFor(event.getUser().getIdLong())), true)
                .addField("Personal settings", "Use `/settings` or `/setup` to change it.", false)
                .build()).setEphemeral(true).queue();
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