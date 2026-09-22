package com.lilminimist.commands;

import com.lilminimist.utils.BotUptime;
import com.lilminimist.utils.EmbedUtils;
import com.lilminimist.utils.QuestionBank;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public final class AboutCommand implements SlashCommand {
    private final BotUptime uptime;
    private final CommandRegistry registry;
    private final QuestionBank questionBank;

    public AboutCommand(BotUptime uptime, CommandRegistry registry, QuestionBank questionBank) {
        this.uptime = uptime;
        this.registry = registry;
        this.questionBank = questionBank;
    }

    @Override
    public net.dv8tion.jda.api.interactions.commands.build.CommandData commandData() {
        return Commands.slash("about", "Learn about lilminimist.");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String serverName = event.getGuild() == null ? "Direct messages" : event.getGuild().getName();
        String jdaVersion = LilminimistBotVersion.jdaVersion();
        event.replyEmbeds(EmbedUtils.cozy("about lilminimist", "A tiny community nook for quiet chaos and good ideas.")
                .addField("Bot", "lilminimist", true)
                .addField("Version", LilminimistBotVersion.version(), true)
                .addField("Server", serverName, true)
                .addField("Uptime", uptime.formatted(), true)
                .addField("Java", System.getProperty("java.version"), true)
                .addField("JDA", jdaVersion, true)
                .addField("Commands", String.valueOf(registry.size()), true)
                .addField("Loaded prompts", String.valueOf(questionBank.count()), true)
                .setFooter("Built for the LilMinimist ✧™ community.")
                .build()).queue();
    }
}