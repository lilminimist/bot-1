package com.lilminimist.commands;

import com.lilminimist.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PollCommand implements SlashCommand, ButtonHandler {
    private final Map<String, Poll> polls = new ConcurrentHashMap<>();

    @Override
    public net.dv8tion.jda.api.interactions.commands.build.CommandData commandData() {
        return Commands.slash("poll", "Create a quick community poll.")
                .addOption(OptionType.STRING, "question", "What should people vote on?", true)
                .addOption(OptionType.STRING, "option1", "First option.", true)
                .addOption(OptionType.STRING, "option2", "Second option.", true)
                .addOption(OptionType.STRING, "option3", "Optional third option.", false)
                .addOption(OptionType.STRING, "option4", "Optional fourth option.", false);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        List<String> options = new ArrayList<>();
        for (int index = 1; index <= 4; index++) {
            if (event.getOption("option" + index) != null) {
                String option = event.getOption("option" + index).getAsString().trim();
                if (!option.isBlank()) {
                    options.add(option);
                }
            }
        }
        if (options.size() < 2) {
            event.reply("A poll needs at least two options.").setEphemeral(true).queue();
            return;
        }

        String id = UUID.randomUUID().toString().replace("-", "");
        Poll poll = new Poll(
                event.getOption("question").getAsString().trim(),
                options,
                new ConcurrentHashMap<>()
        );
        polls.put(id, poll);

        List<Button> buttons = new ArrayList<>();
        for (int index = 0; index < options.size(); index++) {
            String label = options.get(index).length() > 70
                    ? options.get(index).substring(0, 67) + "..."
                    : options.get(index);
            buttons.add(Button.primary("poll:" + id + ":" + index, (index + 1) + " · " + label));
        }
        event.replyEmbeds(render(poll).build()).addActionRow(buttons).queue();
    }

    @Override
    public boolean handleButton(ButtonInteractionEvent event) {
        if (!event.getComponentId().startsWith("poll:")) {
            return false;
        }
        String[] parts = event.getComponentId().split(":");
        if (parts.length != 3) {
            event.reply("This poll button is no longer valid.").setEphemeral(true).queue();
            return true;
        }

        Poll poll = polls.get(parts[1]);
        int optionIndex;
        try {
            optionIndex = Integer.parseInt(parts[2]);
        } catch (NumberFormatException error) {
            event.reply("This poll button is no longer valid.").setEphemeral(true).queue();
            return true;
        }
        if (poll == null || optionIndex < 0 || optionIndex >= poll.options().size()) {
            event.reply("This poll is no longer active.").setEphemeral(true).queue();
            return true;
        }

        poll.votes().put(event.getUser().getIdLong(), optionIndex);
        event.editMessageEmbeds(render(poll).build()).queue();
        return true;
    }

    private EmbedBuilder render(Poll poll) {
        StringBuilder description = new StringBuilder();
        for (int index = 0; index < poll.options().size(); index++) {
            int selectedIndex = index;
            long votes = poll.votes().values().stream().filter(value -> value == selectedIndex).count();
            description.append(index + 1)
                    .append(". ")
                    .append(poll.options().get(index))
                    .append(" — **")
                    .append(votes)
                    .append("**\n");
        }
        return EmbedUtils.cozy("poll: " + poll.question(), description.toString())
                .setFooter("Choose a button to vote. Your latest vote counts.");
    }

    private record Poll(String question, List<String> options, Map<Long, Integer> votes) {
    }
}