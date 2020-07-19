package com.mateus.burble.listener;

import com.mateus.burble.command.CommandManager;
import com.mateus.burble.config.CoreConfig;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import javax.annotation.Nonnull;

public class MainListener implements EventListener {
    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if (event instanceof MessageReceivedEvent) {
            MessageReceivedEvent messageEvent = (MessageReceivedEvent) event;
            if (CoreConfig.getInstance().isUserOwner(messageEvent.getAuthor())
                && messageEvent.getMessage().getContentRaw().equals("shutdown")) {

                event.getJDA().shutdown();
                System.exit(0);
            } else {
                CommandManager.getInstance().handleEvent(messageEvent);
            }
        }
    }
}
