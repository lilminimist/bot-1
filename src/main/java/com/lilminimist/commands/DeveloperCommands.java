package com.lilminimist.commands;

import com.lilminimist.utils.BotUptime;
import com.lilminimist.utils.QuestionBank;
import com.lilminimist.utils.PreferenceStore;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public final class DeveloperCommands {
    private final DeveloperAccess access;
    private final CommandRegistry registry;
    private final QuestionBank questionBank;
    private final PreferenceStore preferences;
    private final BotUptime uptime;
    private final AtomicBoolean maintenance = new AtomicBoolean(false);
    private final JDA jda;

    public DeveloperCommands(DeveloperAccess access, CommandRegistry registry, QuestionBank questionBank,
                             PreferenceStore preferences, BotUptime uptime, JDA jda) {
        this.access = access;
        this.registry = registry;
        this.questionBank = questionBank;
        this.preferences = preferences;
        this.uptime = uptime;
        this.jda = jda;
    }

    public List<SlashCommand> createAll() {
        List<SlashCommand> commands = new ArrayList<>();
        commands.add(guarded(Commands.slash("devinfo", "Developer-only bot information."), event ->
                event.reply("developer panel: version **" + LilminimistBotVersion.version() +
                        "**, configured developer IDs: **" + access.configuredCount() + "**.").setEphemeral(true).queue()));
        commands.add(guarded(Commands.slash("devstats", "Developer-only runtime statistics."), event ->
                event.reply("commands: **" + registry.size() + "**\nquestions: **" + questionBank.count() +
                        "**\npreferences: **" + preferences.size() + "**\nuptime: **" + uptime.formatted() + "**").setEphemeral(true).queue()));
        commands.add(guarded(Commands.slash("reload", "Reload local question and preference data."), event -> {
            questionBank.reload();
            event.reply("Reloaded local question data.").setEphemeral(true).queue();
        }));
        commands.add(guarded(Commands.slash("reloadquestions", "Reload the question files."), event -> {
            questionBank.reload();
            event.reply("Reloaded **" + questionBank.count() + "** question/fact entries.").setEphemeral(true).queue();
        }));
        commands.add(guarded(Commands.slash("test", "Run a small developer health check."), event ->
                event.reply("health check passed: JDA is connected and command registry is loaded.").setEphemeral(true).queue()));
        commands.add(guarded(Commands.slash("debug", "Show safe debug information."), event ->
                event.reply("status: `" + jda.getStatus().name() + "`\ncommands: `" + registry.size() +
                        "`\nquestions: `" + questionBank.count() + "`\nmaintenance: `" + maintenance.get() + "`")
                        .setEphemeral(true).queue()));
        commands.add(guarded(Commands.slash("maintenance", "Toggle developer maintenance status."), event -> {
            boolean enabled;
            while (true) {
                boolean current = maintenance.get();
                if (maintenance.compareAndSet(current, !current)) {
                    enabled = !current;
                    break;
                }
            }
            event.reply("maintenance status is now **" + enabled + "**.").setEphemeral(true).queue();
        }));
        commands.add(guarded(Commands.slash("setversion", "Set the displayed bot version.")
                        .addOption(OptionType.STRING, "version", "Version label, such as 2.0.1.", true),
                event -> {
                    String version = event.getOption("version").getAsString();
                    if (!version.matches("[0-9A-Za-z._-]{1,30}")) {
                        event.reply("Use a short version label with letters, numbers, dots, underscores, or hyphens.").setEphemeral(true).queue();
                        return;
                    }
                    LilminimistBotVersion.setVersion(version);
                    event.reply("Displayed version set to **" + version + "**.").setEphemeral(true).queue();
                }));
        commands.add(guarded(Commands.slash("inspect", "Inspect a safe runtime label.")
                        .addOption(OptionType.STRING, "target", "A command or label to inspect.", true),
                event -> event.reply("inspected label: `" + event.getOption("target").getAsString() + "`").setEphemeral(true).queue()));
        return commands;
    }

    private SlashCommand guarded(net.dv8tion.jda.api.interactions.commands.build.CommandData data,
                                 Consumer<SlashCommandInteractionEvent> action) {
        return new SimpleSlashCommand(data, event -> {
            if (!access.isDeveloper(event.getUser().getIdLong())) {
                event.reply("Developer-only command.").setEphemeral(true).queue();
                return;
            }
            action.accept(event);
        });
    }
}