package com.lilminimist.commands;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface ButtonHandler {
    boolean handleButton(ButtonInteractionEvent event);
}