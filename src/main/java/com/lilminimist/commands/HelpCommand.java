package com.lilminimist.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.awt.Color;

public final class HelpCommand implements SlashCommand {
    private static final Color BURGUNDY = new Color(126, 35, 55);
    private final CommandRegistry registry;

    public HelpCommand(CommandRegistry registry) {
        this.registry = registry;
    }

    @Override
    public net.dv8tion.jda.api.interactions.commands.build.CommandData commandData() {
        return Commands.slash("help", "See the commands available in this server.");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(BURGUNDY)
                .setTitle("lilminimist command nook")
                .setDescription("Small commands for a softer server. Use `/nook` for the button dashboard.")
                .addField("🖤 LilMinimist", "`/about` · `/nook` · `/dailyvibe` · `/midnight` · `/quote`", false)
                .addField("🎲 Fun", "`/8ball` · `/coinflip` · `/roll` · `/choose` · `/rps` · `/rate` · `/joke`", false)
                .addField("💬 Community", "`/poll` · `/qotd` · `/suggest` · `/topic` · `/discussion`", false)
                .addField("🎧 Music", "`/song` · `/playlist` · `/musicmood` · `/musicquestion`", false)
                .addField("👤 People", "`/userinfo` · `/avatar` · `/membercount` · `/accountage` · `/joined`", false)
                .addField("🎨 Creative", "`/prompt` · `/writingprompt` · `/photographyprompt` · `/colorpalette`", false)
                .addField("📚 Knowledge", "`/fact` · `/sciencefact` · `/spacefact` · `/animalfact` · `/riddle`", false)
                .addField("🛠 Utility", "`/calculator` · `/randomnumber` · `/randomchoice` · `/color` · `/uptime`", false)
                .setFooter("No moderation systems live here. Other bots can handle that job.");

        event.replyEmbeds(embed.build()).queue();
    }
}