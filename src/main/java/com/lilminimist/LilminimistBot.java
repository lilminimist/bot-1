package com.lilminimist;


import com.lilminimist.commands.AvatarCommand;
import com.lilminimist.commands.AboutCommand;
import com.lilminimist.commands.CommunityCommands;
import com.lilminimist.commands.CommandRegistry;
import com.lilminimist.commands.CreativeCommands;
import com.lilminimist.commands.DailyVibeCommand;
import com.lilminimist.commands.DeveloperAccess;
import com.lilminimist.commands.DeveloperCommands;
import com.lilminimist.commands.FunCommands;
import com.lilminimist.commands.HelpCommand;
import com.lilminimist.commands.KnowledgeCommands;
import com.lilminimist.commands.MidnightCommand;
import com.lilminimist.commands.MusicCommands;
import com.lilminimist.commands.NookCommand;
import com.lilminimist.commands.NookStatsCommand;
import com.lilminimist.commands.PeopleCommands;
import com.lilminimist.commands.PollCommand;
import com.lilminimist.commands.PingCommand;
import com.lilminimist.commands.QuoteCommand;
import com.lilminimist.commands.QuestionCommands;
import com.lilminimist.commands.ServerBannerCommand;
import com.lilminimist.commands.ServerIconCommand;
import com.lilminimist.commands.ServerInfoCommand;
import com.lilminimist.commands.ServerStatsCommand;
import com.lilminimist.commands.SettingsCommand;
import com.lilminimist.commands.SetupCommand;
import com.lilminimist.commands.ProfileCommand;
import com.lilminimist.commands.UtilityCommands;
import com.lilminimist.commands.UserInfoCommand;
import com.lilminimist.commands.VibeCommand;
import com.lilminimist.utils.BotUptime;
import com.lilminimist.utils.QuestionBank;
import com.lilminimist.utils.PreferenceStore;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public final class LilminimistBot {
    private LilminimistBot() {
    }

    public static void main(String[] args) throws InterruptedException {
        String token = System.getenv("DISCORD_TOKEN");
        if (token == null || token.isBlank()) {
            throw new IllegalStateException(
                    "DISCORD_TOKEN is not set. Add it as a Replit Secret before starting the bot."
            );
        }

        BotUptime uptime = new BotUptime();
        QuestionBank questionBank = new QuestionBank();
        PreferenceStore preferences = new PreferenceStore();
        CommandRegistry commandRegistry = new CommandRegistry(uptime);
        commandRegistry.register(new PingCommand());
        commandRegistry.register(new HelpCommand(commandRegistry));
        commandRegistry.register(new VibeCommand());
        commandRegistry.register(new ServerStatsCommand(uptime));
        commandRegistry.register(new UserInfoCommand());
        commandRegistry.register(new AvatarCommand());
        commandRegistry.register(new AboutCommand(uptime, commandRegistry, questionBank));

        NookCommand nook = new NookCommand();
        commandRegistry.register(nook);
        commandRegistry.registerButtonHandler(nook);
        commandRegistry.register(new DailyVibeCommand());
        commandRegistry.register(new MidnightCommand());
        commandRegistry.register(new QuoteCommand());
        commandRegistry.register(new NookStatsCommand(commandRegistry, uptime));
        commandRegistry.register(new ServerIconCommand());
        commandRegistry.register(new ServerBannerCommand());
        commandRegistry.register(new ServerInfoCommand());
        SetupCommand setup = new SetupCommand(preferences);
        commandRegistry.register(setup);
        commandRegistry.registerButtonHandler(setup);
        commandRegistry.register(new ProfileCommand(preferences));
        commandRegistry.register(new SettingsCommand(preferences));

        FunCommands.createAll().forEach(commandRegistry::register);
        CommunityCommands.createAll(questionBank).forEach(commandRegistry::register);
        QuestionCommands questionCommands = new QuestionCommands(questionBank);
        questionCommands.createAll().forEach(commandRegistry::register);
        MusicCommands.createAll().forEach(commandRegistry::register);
        PeopleCommands.createAll().forEach(commandRegistry::register);
        CreativeCommands.createAll().forEach(commandRegistry::register);
        KnowledgeCommands.createAll().forEach(commandRegistry::register);
        UtilityCommands utilityCommands = new UtilityCommands(uptime);
        utilityCommands.createAll().forEach(commandRegistry::register);

        PollCommand poll = new PollCommand();
        commandRegistry.register(poll);
        commandRegistry.registerButtonHandler(poll);

        JDA jda = JDABuilder.createDefault(token)
                .setActivity(Activity.listening("the quiet side of the server"))
                .addEventListeners(commandRegistry, new OwnerPingListener())
                .build();

        jda.awaitReady();
        DeveloperAccess developerAccess = new DeveloperAccess();
        DeveloperCommands developerCommands = new DeveloperCommands(
                developerAccess, commandRegistry, questionBank, preferences, uptime, jda);
        developerCommands.createAll().forEach(commandRegistry::register);
        System.out.println("TOTAL COMMANDS: " + commandRegistry.size());        // commandRegistry.registerCommands(jda);
        System.out.println("lilminimist is online.");
    }
}
