/*
 *  Core.java
 *  Minecraft plugin core
 *  Minecraft ver. 1.7.3
 *
 */

package com.nerd.bot;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MinecraftCore extends JavaPlugin implements Listener {
    private Logger logger;
    private Configuration config;
    private DiscordCore bot;

    @Override
    public void onEnable( ) {
        logger = this.getServer( ).getLogger( );

        /*
         * Register our events
         */
        MinecraftCore plugin = this;
        final Chat gameListener = new Chat( plugin );
        getServer( ).getPluginManager( ).registerEvent( Event.Type.PLAYER_CHAT, gameListener, Event.Priority.Highest, this );
        getServer( ).getPluginManager( ).registerEvent( Event.Type.PLAYER_JOIN, gameListener, Event.Priority.Highest, this );
        getServer( ).getPluginManager( ).registerEvent( Event.Type.PLAYER_QUIT, gameListener, Event.Priority.Highest, this );

        /*
         * Load and read our config
         */
        File file = new File(plugin.getDataFolder(), "config.yml");

        boolean newConfig = false;
        if ( file.getParentFile( ).mkdirs( ) ) {
            log( Level.SEVERE, "Config not found, creating one..." );
            newConfig = true;
        }

        config = new Configuration( file );
        config.load( );

        if ( newConfig ) {
            /*
             * Set default properties
             */
            config.setProperty( "token", "paste_token_here" );
            config.setProperty( "channel_id", "paste_channelid_here" );
            config.setProperty( "logging_only", false );
            config.setProperty( "log_connection", true );
            config.setProperty( "use_nicknames", false );
            config.setProperty( "embed_photo", "https://couldntbe.me/media/bsod.png" );

            config.save( );

            Bukkit.getServer( ).getPluginManager( ).disablePlugin( plugin );
            return;
        }

        /*
         * Spin up Discord bot
         */
        try {
            bot = new DiscordCore( this );
            bot.Start( config.getString( "token" ) );
        } catch ( Exception e ) {
            log( Level.WARNING, e + ": " + e.getMessage( ) );
            Bukkit.getServer( ).getPluginManager( ).disablePlugin( plugin );
        }
    }

    @Override
    public void onDisable( ) {
        if ( bot != null ) {
            bot.Stop( );
        }

        log( Level.INFO, "Plugin has been disabled." );
    }

    public Configuration getConfig( ) {
        return config;
    }

    public DiscordCore getDiscord( ) {
        return bot;
    }

    public void log( Level level, String msg ) {
        logger.log( level, "[ ! ] " + msg );
    }

    public void sendMessage( String msg ) {
        Bukkit.getServer( ).broadcastMessage( msg );
    }
}
