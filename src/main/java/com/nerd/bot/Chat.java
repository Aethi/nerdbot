/*
 * Chat.java
 * Chat handler for Minecraft <-> Discord accessibility
 *
 */

package com.nerd.bot;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

public class Chat extends PlayerListener {
    private MinecraftCore plugin;

    public Chat( MinecraftCore plugin ){
        this.plugin = plugin;
    }

    @Override
    public void onPlayerChat( PlayerChatEvent event ){
        if (event.isCancelled()) {
            return;
        }

        plugin.getDiscord( ).sendMessage( plugin.getConfig( ).getString( "channel_id" ), event.getPlayer( ).getName( ) + ": " + event.getMessage( ) );
    }
}
