package com.lilminimist.utils;

import java.time.Duration;
import java.time.Instant;

public final class BotUptime {
    private final Instant startedAt = Instant.now();

    public String formatted() {
        Duration uptime = Duration.between(startedAt, Instant.now());
        long days = uptime.toDays();
        long hours = uptime.toHoursPart();
        long minutes = uptime.toMinutesPart();
        long seconds = uptime.toSecondsPart();

        if (days > 0) {
            return "%dd %02dh %02dm".formatted(days, hours, minutes);
        }
        if (hours > 0) {
            return "%dh %02dm %02ds".formatted(hours, minutes, seconds);
        }
        return "%dm %02ds".formatted(minutes, seconds);
    }
}