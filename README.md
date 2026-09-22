# lilminimist Discord Bot

`lilminimist` is a modular community, fun, utility, music, and server-identity bot for the **LilMinimist ✧™** community. It is written in Java 21 with Gradle and JDA 5.2.2, with a dark, cozy, black-and-burgundy personality.

## How the project works

- `LilminimistBot` reads the bot token from the `DISCORD_TOKEN` environment variable.
- `CommandRegistry` holds each slash command and routes incoming interactions to the matching command class.
- Each command is a separate class implementing `SlashCommand`.
- Slash commands are registered globally after the bot connects to Discord.
- `ButtonHandler` powers interactive features such as the `/nook` dashboard and `/poll`.
- `BotUptime` keeps the in-process start time used by `/serverstats`.
- `QuestionBank` loads categorized prompts and animal facts from `src/main/resources/questions/`.
- `PreferenceStore` persists each user's selected vibe in `data/preferences.properties`.

## Implemented command groups

The existing commands remain available:

| Command | What it does |
| --- | --- |
| `/ping` | Confirms the bot is online and shows gateway latency |
| `/help` | Shows the available commands grouped by category |
| `/vibe` | Picks a random built-in mood and short response |
| `/serverstats` | Shows server name, member count, channel count, and bot uptime |
| `/userinfo user:<member>` | Shows public username, display name, account creation date, and server join date when available |
| `/avatar user:<member>` | Displays the selected user's avatar |

The first upgrade phase also includes:

- **LilMinimist identity:** `/about`, `/nook`, `/dailyvibe`, `/midnight`, `/quote`, `/nookstats`
- **Server identity:** `/serverinfo`, `/servericon`, `/serverbanner`
- **Fun:** `/8ball`, `/coinflip`, `/roll`, `/dice`, `/choose`, `/wouldyourather`, `/truth`, `/dare`, `/neverhaveiever`, `/rate`, `/ship`, `/compatibility`, `/compliment`, `/roast`, `/fortune`, `/rps`, `/guess`, `/fact`, `/joke`, `/pun`, `/dadjoke`, `/pickup`, `/nickname`, `/reverse`, `/mock`, and light rating commands
- **Community:** `/poll`, `/qotd`, `/suggest`, `/confess`, `/anonymous`, `/idea`, `/topic`, `/question`, `/discussion`, `/icebreaker`, `/deepquestion`, `/funquestion`, `/randomquestion`, `/conversation`
- **Music prompts:** `/song`, `/playlist`, `/nowplaying`, `/musicvote`, `/musicmood`, `/musicquestion`
- **People:** `/membercount`, `/rolecount`, `/accountage`, `/joined`
- **Creative:** `/prompt`, `/writingprompt`, `/storyprompt`, `/poemprompt`, `/drawingprompt`, `/photographyprompt`, `/randomaesthetic`, `/colorpalette`
- **Knowledge:** `/sciencefact`, `/spacefact`, `/geography`, `/history`, `/animalfact`, `/riddle`, `/word`
- **Utility:** `/calculator`, `/quadratic`, `/physics`, `/convert`, `/randomnumber`, `/randomchoice`, `/color`, `/channelinfo`, `/uptime`
- **Personal preferences:** `/setup`, `/profile`, `/settings`

Use `/nook` for the interactive category dashboard. Polls use buttons and keep their vote state in memory while the bot is running.

### Calculator and physics examples

`/calculator` safely supports arithmetic, brackets, powers, percentages, scientific notation, and `sqrt`, `sin`, `cos`, `tan`, `log`, `ln`, `abs`, and `exp`.

```text
/calculator expression:sqrt(25) + 2^3
/calculator expression:sin(pi/2)
/calculator expression:15% * 200
/quadratic a:1 b:-3 c:2
/convert value:5 from:km to:m
```

`/physics` supports common mechanics, electricity, and waves formulas without executing user-supplied code:

```text
/physics formula:F=ma values:m=5,a=9.8
/physics formula:V=IR values:i=2,r=10
/physics formula:v=fλ values:f=440,lambda=0.78
```

### Personal preferences

`/setup` presents Soft, Soft Goth, Gen Z, Chaotic, Sarcastic, and Mix Everything styles. `/profile` shows only the invoking user's preference, and `/settings` changes it directly. Preferences are stored locally and do not collect unnecessary personal information.

### Developer-only commands

Developer commands require Discord user IDs in the optional `DEVELOPER_IDS` environment variable. Use a comma-separated list:

```text
DEVELOPER_IDS=123456789012345678,987654321098765432
```

Available commands are `/devinfo`, `/devstats`, `/reload`, `/reloadquestions`, `/test`, `/debug`, `/maintenance`, `/setversion`, and `/inspect`. Users outside the allowlist receive a normal developer-only response. Usernames and display names are never used for authorization, and IDs are never displayed publicly.

Store `DEVELOPER_IDS` as a Replit Secret or environment variable, never in source code.

## Upgrade roadmap

The following areas are intentionally deferred so the first upgrade stays stable:

- Lightweight persistent birthday profiles and XP/leveling
- Daily and weekly challenge tracking
- Configurable welcome, goodbye, boost, suggestion, confession, and daily channels
- Discord scheduled events and giveaway lifecycle commands
- Optional weather, translation, definitions, and other external API integrations
- Music service integrations or playback
- More games such as trivia sessions, word scrambles, and multi-step guessing

There are no moderation commands or moderation systems in this bot. No bans, kicks, mutes, warnings, purge, automod, anti-raid, or moderation logging are included.

## Safe limitations

- `/suggest`, `/confess`, and `/anonymous` show a setup message and do not post anywhere until a destination channel/configuration system is added. This prevents accidental anonymous publishing.
- Music commands provide prompts and built-in responses only. They do not download or play copyrighted music.
- Poll vote state is in memory and resets when the bot restarts. A database is deliberately not used in this phase.
- Question and fact content is local, categorized, and loaded from resource files. The bank avoids immediately repeating the same prompt for each user and has safe fallbacks for empty or missing categories.
- `/avatar` uses Discord's highest practical CDN size and link buttons. Discord clients decide whether a link opens or downloads; the bot cannot force a client-side download.
- Commands use built-in content and do not require extra API keys.

This phase intentionally leaves symbolic algebra beyond quadratic equations and simultaneous equations for a future specialist math implementation. `/calculator` never uses `eval`, Java compilation, or arbitrary code execution.

## Add `DISCORD_TOKEN` in Replit

1. Open the **Secrets** tool in Replit.
2. Add a new secret named `DISCORD_TOKEN`.
3. Paste the token copied from the Discord Developer Portal into the secure value field.
4. Do not put the token in this README, source code, `.env` files, or logs.

The application intentionally stops with a clear error if `DISCORD_TOKEN` is missing.

## Run the bot

From this directory:

```bash
./gradlew run
```

If the Gradle wrapper has not been generated yet, use the installed Gradle command:

```bash
gradle run
```

The bot must be invited to at least one server before its commands can be used. Global slash-command registration can take a little time to appear in Discord after the first connection.

## Discord permissions and intents

The bot only needs:

- **Bot** scope
- **applications.commands** scope
- **View Channels**
- **Send Messages**
- **Embed Links**

The baseline bot does not request any privileged gateway intent, so it can connect without extra portal configuration. No Message Content Intent is needed because this bot uses slash commands only. `/userinfo` attempts to retrieve a server member so it can show the join date; if Discord does not make that date available, the command clearly shows `Not available`. You may enable **Server Members Intent** under the bot's **Privileged Gateway Intents** in the Discord Developer Portal if you want the most reliable member lookup behavior.

## Invite the bot

In the Discord Developer Portal:

1. Open your application.
2. Go to **OAuth2 → URL Generator**.
3. Select `bot` and `applications.commands`.
4. Select the permissions listed above.
5. Open the generated URL and choose your server.

Only invite the bot to servers where you have permission to manage bots.

## Add another slash command

1. Create a new class in `src/main/java/com/lilminimist/commands/`.
2. Implement `SlashCommand`.
3. Return a `Commands.slash("name", "description")` value from `commandData()`.
4. Add the behavior in `execute(SlashCommandInteractionEvent event)`.
5. Register the class in `LilminimistBot.main()` with `commandRegistry.register(new YourCommand())`.
6. Add the command to `HelpCommand` so members can discover it.

Keep new commands focused and avoid putting unrelated behavior into `LilminimistBot`.

## Project structure

```text
src/main/java/com/lilminimist/
├── LilminimistBot.java
├── commands/
│   ├── AboutCommand.java
│   ├── AvatarCommand.java
│   ├── ButtonHandler.java
│   ├── CommunityCommands.java
│   ├── CommandRegistry.java
│   ├── CreativeCommands.java
│   ├── FunCommands.java
│   ├── HelpCommand.java
│   ├── KnowledgeCommands.java
│   ├── MusicCommands.java
│   ├── NookCommand.java
│   ├── PeopleCommands.java
│   ├── PollCommand.java
│   ├── PingCommand.java
│   ├── ServerInfoCommand.java
│   ├── SimpleSlashCommand.java
│   ├── UtilityCommands.java
│   ├── ServerStatsCommand.java
│   ├── SlashCommand.java
│   ├── UserInfoCommand.java
│   ├── VibeCommand.java
│   └── ...
└── utils/
    ├── BotUptime.java
    └── EmbedUtils.java
```