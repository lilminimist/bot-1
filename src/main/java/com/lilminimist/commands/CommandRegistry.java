package com.lilminimist.commands;

import com.lilminimist.utils.BotUptime;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class CommandRegistry extends ListenerAdapter {
    private final Map<String, SlashCommand> commands = new LinkedHashMap<>();
    private final List<ButtonHandler> buttonHandlers = new ArrayList<>();
    private final BotUptime uptime;

    public CommandRegistry(BotUptime uptime) {
        this.uptime = uptime;
    }

    public void register(SlashCommand command) {
        if (commands.containsKey(command.commandData().getName())) {
            throw new IllegalArgumentException("Duplicate slash command: " + command.commandData().getName());
        }
        commands.put(command.commandData().getName(), command);
    }

    public void registerButtonHandler(ButtonHandler handler) {
        buttonHandlers.add(handler);
    }

    public void registerCommands(JDA jda) {
        jda.updateCommands()
                .addCommands(commandData())
                .queue();
    }

    public List<net.dv8tion.jda.api.interactions.commands.build.CommandData> commandData() {
        return new ArrayList<>(
                commands.values().stream().map(SlashCommand::commandData).toList()
        );
    }

    public BotUptime uptime() {
        return uptime;
    }

    public int size() {
        return commands.size();
    }

    public SlashCommand find(String name) {
        return commands.get(name);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        SlashCommand command = find(event.getName());
        if (command == null) {
            event.reply("That command is not available right now.").setEphemeral(true).queue();
            return;
        }

        command.execute(event);
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        for (ButtonHandler handler : buttonHandlers) {
            if (handler.handleButton(event)) {
                return;
            }
        }
        event.reply("That button is no longer active.").setEphemeral(true).queue();
    }
}