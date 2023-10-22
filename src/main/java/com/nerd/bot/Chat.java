/*
 * Chat.java
 * Chat handler for Minecraft <-> Discord accessibility
 *
 */

package com.nerd.bot;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

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

        String message = event.getMessage( );

        /*
         * Remove @everyone/@here
         */
        if ( message.contains( "@everyone" ) ||
             message.contains( "@here" ) ) {
            message = message.replaceAll("@everyone", "at everyone");
            message = message.replaceAll("@here", "at here");

            message = message.trim();
        }

        String playerName = ( Objects.equals( plugin.getConfig( ).getString( "use_nicknames" ), "true") ) ? event.getPlayer( ).getDisplayName( ) : event.getPlayer( ).getName( );
        plugin.getDiscord( ).sendMessage( plugin.getConfig( ).getString( "channel_id" ), playerName + ": " + message );
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        if ( Objects.equals( plugin.getConfig( ).getString( "log_connection" ), "false" ) ) {
            return;
        }

        String playerName = ( Objects.equals( plugin.getConfig( ).getString( "use_nicknames" ), "true") ) ? event.getPlayer( ).getDisplayName( ) : event.getPlayer( ).getName( );
        plugin.getDiscord( ).sendMessage( plugin.getConfig( ).getString( "channel_id" ), playerName + " has joined the server." );
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        if ( Objects.equals( plugin.getConfig( ).getString( "log_connection" ), "false" ) ) {
            return;
        }

        String playerName = ( Objects.equals( plugin.getConfig( ).getString( "use_nicknames" ), "true") ) ? event.getPlayer( ).getDisplayName( ) : event.getPlayer( ).getName( );
        plugin.getDiscord( ).sendMessage( plugin.getConfig( ).getString( "channel_id" ), playerName + " has left the server." );
    }
}
