package com.lilminimist;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class OwnerPingListener extends ListenerAdapter {

    private static final String OWNER_ID = "1231980673787498540";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        if (!event.getAuthor().getId().equals(OWNER_ID)) {
            return;
        }

        if (!event.getMessage().getMentions().isMentioned(event.getJDA().getSelfUser())) {
            return;
        }

        event.getChannel()
                .sendMessage("at your service maalkin ji")
                .queue();
    }
}
