package com.lilminimist.commands;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public final class DeveloperAccess {
    private final Set<Long> developerIds;

    public DeveloperAccess() {
        String configured = System.getenv("DEVELOPER_IDS");
        if (configured == null || configured.isBlank()) {
            developerIds = Set.of();
            return;
        }
        developerIds = Arrays.stream(configured.split(","))
                .map(String::trim)
                .filter(value -> value.matches("\\d+"))
                .map(Long::parseLong)
                .collect(Collectors.toUnmodifiableSet());
    }

    public boolean isDeveloper(long userId) {
        return developerIds.contains(userId);
    }

    public int configuredCount() {
        return developerIds.size();
    }
}