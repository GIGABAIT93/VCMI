package com.vcmi.modules.playertime;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;

public class PlayerEventListener {

    private final PlayerTimeTracker timeTracker;

    public PlayerEventListener(PlayerTimeTracker timeTracker) {
        this.timeTracker = timeTracker;
    }

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        timeTracker.playerJoined(event.getPlayer().getUniqueId(), event.getPlayer().getUsername());
    }

    @Subscribe
    public void onPlayerLeave(DisconnectEvent event) {
        timeTracker.playerLeft(event.getPlayer().getUniqueId());
    }
}
