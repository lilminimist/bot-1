package com.lilminimist.commands;

import net.dv8tion.jda.api.JDA;

public final class LilminimistBotVersion {
    private static volatile String version = "2.0.0";

    private LilminimistBotVersion() {
    }

    public static String version() {
        return version;
    }

    public static void setVersion(String newVersion) {
        if (newVersion != null && newVersion.matches("[0-9A-Za-z._-]{1,30}")) {
            version = newVersion;
        }
    }

    public static String jdaVersion() {
        String version = JDA.class.getPackage().getImplementationVersion();
        return version == null ? "5.2.2" : version;
    }
}