package com.lilminimist.commands;

import com.lilminimist.utils.BotUptime;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public final class UtilityCommands {
    private final BotUptime uptime;

    public UtilityCommands(BotUptime uptime) {
        this.uptime = uptime;
    }

    public List<SlashCommand> createAll() {
        List<SlashCommand> commands = new ArrayList<>();
        commands.add(new SimpleSlashCommand(
                Commands.slash("calculator", "Safely calculate a scientific expression.")
                        .addOption(OptionType.STRING, "expression", "Example: sqrt(25) + 2^3 or sin(pi/2)", true),
                event -> {
                    try {
                        double result = new ArithmeticParser(event.getOption("expression").getAsString()).parse();
                        event.reply("result: **" + format(result) + "**").queue();
                    } catch (IllegalArgumentException error) {
                        event.reply("I could not parse that safely. Try numbers, brackets, `+ - * / ^ %`, scientific notation, or functions such as `sqrt`, `sin`, `cos`, `tan`, `log`, `ln`, `abs`, and `exp`.").setEphemeral(true).queue();
                    }
                }));
        commands.add(new SimpleSlashCommand(
                Commands.slash("quadratic", "Solve a quadratic equation ax² + bx + c = 0.")
                        .addOption(OptionType.NUMBER, "a", "The a coefficient.", true)
                        .addOption(OptionType.NUMBER, "b", "The b coefficient.", true)
                        .addOption(OptionType.NUMBER, "c", "The c coefficient.", true),
                event -> {
                    double a = event.getOption("a").getAsDouble();
                    double b = event.getOption("b").getAsDouble();
                    double c = event.getOption("c").getAsDouble();
                    if (a == 0) {
                        event.reply("For a quadratic, `a` cannot be zero.").setEphemeral(true).queue();
                        return;
                    }
                    double discriminant = b * b - 4 * a * c;
                    if (discriminant < 0) {
                        event.reply("The equation has no real roots. Discriminant: **" + format(discriminant) + "**").queue();
                    } else if (discriminant == 0) {
                        event.reply("The repeated root is **" + format(-b / (2 * a)) + "**.").queue();
                    } else {
                        double root = Math.sqrt(discriminant);
                        event.reply("roots: **" + format((-b + root) / (2 * a)) + "** and **" +
                                format((-b - root) / (2 * a)) + "**").queue();
                    }
                }));
        commands.add(new SimpleSlashCommand(
                Commands.slash("randomnumber", "Pick a number in a range.")
                        .addOption(OptionType.INTEGER, "minimum", "Minimum value.", true)
                        .addOption(OptionType.INTEGER, "maximum", "Maximum value.", true),
                event -> {
                    long minimum = event.getOption("minimum").getAsLong();
                    long maximum = event.getOption("maximum").getAsLong();
                    if (minimum > maximum || maximum - minimum > 1_000_000) {
                        event.reply("Please provide a valid range no wider than one million.").setEphemeral(true).queue();
                        return;
                    }
                    event.reply("your number: **" + ThreadLocalRandom.current().nextLong(minimum, maximum + 1) + "**").queue();
                }));
        commands.add(new SimpleSlashCommand(
                Commands.slash("randomchoice", "Choose from a comma-separated list.")
                        .addOption(OptionType.STRING, "choices", "Example: red, blue, green", true),
                event -> {
                    List<String> choices = java.util.Arrays.stream(event.getOption("choices").getAsString().split(","))
                            .map(String::trim).filter(value -> !value.isBlank()).toList();
                    if (choices.isEmpty()) {
                        event.reply("Give me at least one choice.").setEphemeral(true).queue();
                        return;
                    }
                    event.reply("the choice is **" + choices.get(ThreadLocalRandom.current().nextInt(choices.size())) + "**.").queue();
                }));
        commands.add(new SimpleSlashCommand(Commands.slash("color", "Generate a random color."), event -> {
            int red = ThreadLocalRandom.current().nextInt(256);
            int green = ThreadLocalRandom.current().nextInt(256);
            int blue = ThreadLocalRandom.current().nextInt(256);
            event.reply("random color: **#%02X%02X%02X**".formatted(red, green, blue)).queue();
        }));
        commands.add(new SimpleSlashCommand(Commands.slash("channelinfo", "Show information about the current channel."), event ->
                event.reply("channel: **" + event.getChannel().getName() + "**\nkind: `" + event.getChannel().getType().name().toLowerCase() + "`").queue()));
        commands.add(new SimpleSlashCommand(Commands.slash("uptime", "Show how long the bot has been online."), event ->
                event.reply("lilminimist has been awake for **" + uptime.formatted() + "**.").queue()));
        commands.add(new SimpleSlashCommand(
                Commands.slash("convert", "Convert common metric units.")
                        .addOption(OptionType.NUMBER, "value", "The value to convert.", true)
                        .addOption(OptionType.STRING, "from", "Source unit: m, km, cm, g, kg, c, or f.", true)
                        .addOption(OptionType.STRING, "to", "Target unit: m, km, cm, g, kg, c, or f.", true),
                event -> {
                    double value = event.getOption("value").getAsDouble();
                    String from = event.getOption("from").getAsString().toLowerCase(Locale.ROOT);
                    String to = event.getOption("to").getAsString().toLowerCase(Locale.ROOT);
                    try {
                        double converted = UnitConverter.convert(value, from, to);
                        event.reply("**" + format(value) + " " + from + " = " + format(converted) + " " + to + "**").queue();
                    } catch (IllegalArgumentException error) {
                        event.reply(error.getMessage()).setEphemeral(true).queue();
                    }
                }));
        return commands;
    }

    private static String format(double value) {
        if (value == Math.rint(value)) {
            return String.valueOf((long) value);
        }
        return String.format("%.4f", value);
    }

    private static final class ArithmeticParser {
        private final String input;
        private int position;

        private ArithmeticParser(String raw) {
            if (raw.length() > 100) {
                throw new IllegalArgumentException("Expression too long.");
            }
            this.input = raw.replaceAll("\\s+", "");
        }

        private double parse() {
            if (input.isBlank() || !input.matches("[0-9+*/().%^,a-zA-Z-]+")) {
                throw new IllegalArgumentException("Invalid characters.");
            }
            double result = expression();
            if (position != input.length() || !Double.isFinite(result)) {
                throw new IllegalArgumentException("Invalid expression.");
            }
            return result;
        }

        private double expression() {
            double value = term();
            while (position < input.length()) {
                char operator = input.charAt(position);
                if (operator != '+' && operator != '-') {
                    break;
                }
                position++;
                double right = term();
                value = operator == '+' ? value + right : value - right;
            }
            return value;
        }

        private double term() {
            double value = power();
            while (position < input.length()) {
                char operator = input.charAt(position);
                if (operator != '*' && operator != '/') {
                    break;
                }
                position++;
                double right = power();
                if (operator == '/' && right == 0) {
                    throw new IllegalArgumentException("Division by zero.");
                }
                value = operator == '*' ? value * right : value / right;
            }
            return value;
        }

        private double power() {
            double value = unary();
            if (position < input.length() && input.charAt(position) == '^') {
                position++;
                value = Math.pow(value, power());
            }
            return value;
        }

        private double unary() {
            if (position < input.length() && input.charAt(position) == '-') {
                position++;
                return -unary();
            }
            if (position < input.length() && input.charAt(position) == '+') {
                position++;
                return unary();
            }
            return primary();
        }

        private double primary() {
            if (position < input.length() && input.charAt(position) == '(') {
                position++;
                double value = expression();
                if (position >= input.length() || input.charAt(position++) != ')') {
                    throw new IllegalArgumentException("Missing parenthesis.");
                }
                return percentage(value);
            }
            if (position < input.length() && Character.isLetter(input.charAt(position))) {
                int start = position;
                while (position < input.length() && Character.isLetter(input.charAt(position))) {
                    position++;
                }
                String function = input.substring(start, position);
                if (function.equals("pi")) {
                    return percentage(Math.PI);
                }
                if (function.equals("e")) {
                    return percentage(Math.E);
                }
                if (position >= input.length() || input.charAt(position++) != '(') {
                    throw new IllegalArgumentException("Functions need parentheses.");
                }
                double argument = expression();
                if (position >= input.length() || input.charAt(position++) != ')') {
                    throw new IllegalArgumentException("Missing function parenthesis.");
                }
                double result = switch (function) {
                    case "sqrt" -> Math.sqrt(argument);
                    case "sin" -> Math.sin(argument);
                    case "cos" -> Math.cos(argument);
                    case "tan" -> Math.tan(argument);
                    case "log" -> Math.log10(argument);
                    case "ln" -> Math.log(argument);
                    case "abs" -> Math.abs(argument);
                    case "exp" -> Math.exp(argument);
                    default -> throw new IllegalArgumentException("Unsupported function.");
                };
                return percentage(result);
            }
            int start = position;
            while (position < input.length() && (Character.isDigit(input.charAt(position)) || input.charAt(position) == '.')) {
                position++;
            }
            if (position < input.length() && (input.charAt(position) == 'e')) {
                position++;
                if (position < input.length() && (input.charAt(position) == '+' || input.charAt(position) == '-')) {
                    position++;
                }
                while (position < input.length() && Character.isDigit(input.charAt(position))) {
                    position++;
                }
            }
            if (start == position) {
                throw new IllegalArgumentException("Expected a number.");
            }
            return percentage(Double.parseDouble(input.substring(start, position)));
        }

        private double percentage(double value) {
            if (position < input.length() && input.charAt(position) == '%') {
                position++;
                return value / 100;
            }
            return value;
        }
    }

    private static final class UnitConverter {
        private static double convert(double value, String from, String to) {
            if (from.equals(to)) {
                return value;
            }
            if (isTemperature(from) && isTemperature(to)) {
                double celsius = from.equals("c") ? value : (value - 32) * 5 / 9;
                return to.equals("c") ? celsius : celsius * 9 / 5 + 32;
            }
            if (isMass(from) && isMass(to)) {
                double grams = from.equals("kg") ? value * 1000 : value;
                return to.equals("kg") ? grams / 1000 : grams;
            }
            if (isLength(from) && isLength(to)) {
                double meters = switch (from) {
                    case "km" -> value * 1000;
                    case "cm" -> value / 100;
                    default -> value;
                };
                return switch (to) {
                    case "km" -> meters / 1000;
                    case "cm" -> meters * 100;
                    default -> meters;
                };
            }
            throw new IllegalArgumentException("Supported conversions: length (`m`, `km`, `cm`), mass (`g`, `kg`), and temperature (`c`, `f`).");
        }

        private static boolean isTemperature(String unit) {
            return unit.equals("c") || unit.equals("f");
        }

        private static boolean isMass(String unit) {
            return unit.equals("g") || unit.equals("kg");
        }

        private static boolean isLength(String unit) {
            return unit.equals("m") || unit.equals("km") || unit.equals("cm");
        }
    }
}