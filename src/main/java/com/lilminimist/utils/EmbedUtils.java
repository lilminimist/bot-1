package com.lilminimist.utils;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.Color;

public final class EmbedUtils {
    public static final Color BURGUNDY = new Color(126, 35, 55);
    public static final Color SOFT_BLACK = new Color(24, 20, 24);

    private EmbedUtils() {
    }

    public static EmbedBuilder cozy(String title, String description) {
        return new EmbedBuilder()
                .setColor(BURGUNDY)
                .setTitle(title)
                .setDescription(description);
    }
}