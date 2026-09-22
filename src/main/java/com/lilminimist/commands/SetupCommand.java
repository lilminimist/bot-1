package com.lilminimist.commands;

import com.lilminimist.utils.EmbedUtils;
import com.lilminimist.utils.PreferenceStore;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public final class SetupCommand implements SlashCommand, ButtonHandler {
    private final PreferenceStore preferences;

    public SetupCommand(PreferenceStore preferences) {
        this.preferences = preferences;
    }

    @Override
    public net.dv8tion.jda.api.interactions.commands.build.CommandData commandData() {
        return Commands.slash("setup", "Choose your LilMinimist vibe.");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.replyEmbeds(EmbedUtils.cozy("choose your LilMinimist vibe", "Your choice only affects your own bot responses.")
                        .addField("Available styles", "Soft · Soft Goth · Gen Z · Chaotic · Sarcastic · Mix Everything", false)
                        .build())
                .addActionRow(
                        Button.secondary("setup:soft", "🕯️ Soft"),
                        Button.secondary("setup:soft_goth", "🖤 Soft Goth"),
                        Button.secondary("setup:genz", "✨ Gen Z"))
                .addActionRow(
                        Button.secondary("setup:chaotic", "🌀 Chaotic"),
                        Button.secondary("setup:sarcastic", "😭 Sarcastic"),
                        Button.secondary("setup:mix", "🌑 Mix Everything"))
                .queue();
    }

    @Override
    public boolean handleButton(ButtonInteractionEvent event) {
        if (!event.getComponentId().startsWith("setup:")) {
            return false;
        }
        String style = event.getComponentId().substring("setup:".length());
        preferences.setStyle(event.getUser().getIdLong(), style);
        event.reply("Saved your LilMinimist vibe as **" + display(style) + "**.").setEphemeral(true).queue();
        return true;
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