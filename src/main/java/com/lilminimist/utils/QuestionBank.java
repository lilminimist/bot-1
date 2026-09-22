package com.lilminimist.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public final class QuestionBank {
    private static final Map<String, String> RESOURCES = Map.of(
            "truth", "questions/truths.txt",
            "dare", "questions/dares.txt",
            "neverhaveiever", "questions/never_have_i_ever.txt",
            "animalfact", "questions/animalfacts.txt",
            "wouldyourather", "questions/would_your_rather.txt",
            "qotd", "questions/qotd.txt",
            "icebreaker", "questions/icebreakers.txt",
            "deepquestion", "questions/deepquestions.txt",
            "funquestion", "questions/funquestions.txt",
            "conversation", "questions/conversation.txt"
    );

    private final Map<String, Map<String, List<String>>> questions = new LinkedHashMap<>();
    private final Map<String, String> previousByUser = new HashMap<>();

    public QuestionBank() {
        reload();
    }

    public synchronized void reload() {
        questions.clear();
        for (Map.Entry<String, String> resource : RESOURCES.entrySet()) {
            questions.put(resource.getKey(), load(resource.getValue()));
        }
        previousByUser.clear();
    }

    public synchronized String random(String bank, String category, long userId) {
        Map<String, List<String>> categories = questions.getOrDefault(bank, Map.of());
        List<String> available = new ArrayList<>();
        if (category == null || category.isBlank() || category.equalsIgnoreCase("random")) {
            categories.values().forEach(available::addAll);
        } else {
            available.addAll(categories.getOrDefault(normalize(category), List.of()));
        }
        if (available.isEmpty()) {
            available.addAll(categories.getOrDefault("random", List.of()));
        }
        if (available.isEmpty()) {
            return "The nook has no prompt here yet. Try another category.";
        }

        String previousKey = bank + ":" + userId;
        String previous = previousByUser.get(previousKey);
        List<String> choices = available.stream().filter(value -> !value.equals(previous)).toList();
        String selected = choices.isEmpty()
                ? available.get(ThreadLocalRandom.current().nextInt(available.size()))
                : choices.get(ThreadLocalRandom.current().nextInt(choices.size()));
        previousByUser.put(previousKey, selected);
        return selected;
    }

    public synchronized String animalFact(String animal) {
        Map<String, List<String>> facts = questions.getOrDefault("animalfact", Map.of());
        if (animal == null || animal.isBlank()) {
            List<String> all = new ArrayList<>();
            facts.values().forEach(all::addAll);
            return all.isEmpty() ? "The animal fact shelf is empty right now." :
                    all.get(ThreadLocalRandom.current().nextInt(all.size()));
        }
        List<String> matching = facts.get(normalize(animal));
        if (matching == null || matching.isEmpty()) {
            return "I do not have an animal fact for **" + animal + "** yet. Try cat, octopus, fox, crow, otter, or penguin.";
        }
        return matching.get(ThreadLocalRandom.current().nextInt(matching.size()));
    }

    public synchronized int count() {
        return questions.values().stream()
                .flatMap(categories -> categories.values().stream())
                .mapToInt(List::size)
                .sum();
    }

    public synchronized Map<String, List<String>> categories(String bank) {
        Map<String, List<String>> source = questions.getOrDefault(bank, Map.of());
        Map<String, List<String>> copy = new LinkedHashMap<>();
        source.forEach((key, value) -> copy.put(key, List.copyOf(value)));
        return Collections.unmodifiableMap(copy);
    }

    private Map<String, List<String>> load(String resource) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        try (var stream = QuestionBank.class.getClassLoader().getResourceAsStream(resource)) {
            if (stream == null) {
                return result;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.isBlank() || line.startsWith("#")) {
                        continue;
                    }
                    String[] parts = line.split("\\|", 2);
                    if (parts.length == 1) {
                        result.computeIfAbsent("random", ignored -> new ArrayList<>()).add(parts[0].trim());
                    } else {
                        result.computeIfAbsent(normalize(parts[0]), ignored -> new ArrayList<>()).add(parts[1].trim());
                    }
                }
            }
        } catch (IOException ignored) {
            // An empty bank is handled by the command with a user-facing fallback.
        }
        return result;
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase(Locale.ROOT).replace(' ', '_');
    }
}