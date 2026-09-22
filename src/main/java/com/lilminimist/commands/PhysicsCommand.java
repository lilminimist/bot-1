package com.lilminimist.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public final class PhysicsCommand implements SlashCommand {
    @Override
    public net.dv8tion.jda.api.interactions.commands.build.CommandData commandData() {
        return Commands.slash("physics", "Calculate a common school-physics formula.")
                .addOption(OptionType.STRING, "formula", "Example: F=ma, V=IR, or v=fλ.", true)
                .addOption(OptionType.STRING, "values", "Comma-separated values, such as m=5,a=9.8.", true);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String formula = event.getOption("formula").getAsString();
        String rawValues = event.getOption("values").getAsString();
        try {
            Map<String, Double> values = parseValues(rawValues);
            Calculation result = calculate(normalize(formula), values);
            event.reply("**Formula:** `" + result.formula() + "`\n" +
                    "**Given:** `" + rawValues + "`\n" +
                    "**Substitution:** `" + result.substitution() + "`\n" +
                    "**Answer:** **" + format(result.answer()) + " " + result.unit() + "**").queue();
        } catch (IllegalArgumentException error) {
            event.reply(error.getMessage()).setEphemeral(true).queue();
        }
    }

    private Calculation calculate(String formula, Map<String, Double> values) {
        return switch (formula) {
            case "f=ma" -> result("F = ma", values, "m*a", require(values, "m") * require(values, "a"), "N");
            case "v=u+at" -> result("v = u + at", values, "u + a*t", require(values, "u") + require(values, "a") * require(values, "t"), "m/s");
            case "s=ut+1/2at^2" -> result("s = ut + 1/2 at²", values, "u*t + 0.5*a*t²",
                    require(values, "u") * require(values, "t") + 0.5 * require(values, "a") * Math.pow(require(values, "t"), 2), "m");
            case "v^2=u^2+2as" -> result("v² = u² + 2as", values, "sqrt(u² + 2*a*s)",
                    Math.sqrt(require(values, "u") * require(values, "u") + 2 * require(values, "a") * require(values, "s")), "m/s");
            case "p=mv" -> result("p = mv", values, "m*v", require(values, "m") * require(values, "v"), "kg·m/s");
            case "w=fs" -> result("W = Fs", values, "F*s", require(values, "f") * require(values, "s"), "J");
            case "p=w/t" -> result("P = W/t", values, "W/t", require(values, "w") / nonZero(values, "t"), "W");
            case "ke=1/2mv^2" -> result("KE = 1/2 mv²", values, "0.5*m*v²",
                    0.5 * require(values, "m") * Math.pow(require(values, "v"), 2), "J");
            case "pe=mgh" -> result("PE = mgh", values, "m*g*h",
                    require(values, "m") * require(values, "g") * require(values, "h"), "J");
            case "centripetal", "fc=mv^2/r" -> result("Fᶜ = mv²/r", values, "m*v²/r",
                    require(values, "m") * Math.pow(require(values, "v"), 2) / nonZero(values, "r"), "N");
            case "v=ir" -> result("V = IR", values, "I*R", require(values, "i") * require(values, "r"), "V");
            case "p=vi" -> result("P = VI", values, "V*I", require(values, "v") * require(values, "i"), "W");
            case "p=i^2r" -> result("P = I²R", values, "I²*R", Math.pow(require(values, "i"), 2) * require(values, "r"), "W");
            case "p=v^2/r" -> result("P = V²/R", values, "V²/R", Math.pow(require(values, "v"), 2) / nonZero(values, "r"), "W");
            case "q=it" -> result("Q = It", values, "I*t", require(values, "i") * require(values, "t"), "C");
            case "v=flambda", "v=fλ" -> result("v = fλ", values, "f*λ", require(values, "f") * require(values, "lambda"), "m/s");
            default -> throw new IllegalArgumentException("I do not support that formula yet. Try F=ma, V=IR, P=VI, KE=1/2mv^2, or v=fλ.");
        };
    }

    private static Calculation result(String formula, Map<String, Double> values, String substitution,
                                      double answer, String unit) {
        return new Calculation(formula, substitution + " with " + values, answer, unit);
    }

    private static Map<String, Double> parseValues(String raw) {
        Map<String, Double> values = new LinkedHashMap<>();
        for (String part : raw.split(",")) {
            String[] pieces = part.trim().split("=", 2);
            if (pieces.length != 2) {
                throw new IllegalArgumentException("Values must look like `m=5,a=9.8`.");
            }
            try {
                values.put(normalize(pieces[0]), Double.parseDouble(pieces[1].trim()));
            } catch (NumberFormatException error) {
                throw new IllegalArgumentException("Every physics value must be a number.");
            }
        }
        return values;
    }

    private static double require(Map<String, Double> values, String key) {
        Double value = values.get(key);
        if (value == null) {
            throw new IllegalArgumentException("This formula needs a value for `" + key + "`.");
        }
        return value;
    }

    private static double nonZero(Map<String, Double> values, String key) {
        double value = require(values, key);
        if (value == 0) {
            throw new IllegalArgumentException("`" + key + "` cannot be zero for this formula.");
        }
        return value;
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase(Locale.ROOT)
                .replace(" ", "")
                .replace("²", "^2")
                .replace("λ", "lambda");
    }

    private static String format(double value) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("That calculation does not produce a finite answer.");
        }
        return String.format("%.5g", value);
    }

    private record Calculation(String formula, String substitution, double answer, String unit) {
    }
}