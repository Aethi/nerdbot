/*
 *  Bot.java
 *  Discord bot core
 *
 */

package com.nerd.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.EmbedBuilder;

import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.Objects;
import java.util.Timer;
import java.util.EnumSet;
import java.util.TimerTask;
import java.util.logging.Level;
import java.awt.Color;

import org.bukkit.entity.Player;

public class DiscordCore extends ListenerAdapter {
    public static void main( String[] arguments ) throws Exception { }

    public MinecraftCore plugin;
    public JDA api;

    public DiscordCore( MinecraftCore plugin ) {
        this.plugin = plugin;
    }

    public void Start( String token ) throws LoginException {
        api = JDABuilder.createDefault( token ).enableIntents( GatewayIntent.MESSAGE_CONTENT ).build( );
        api.addEventListener( this );

        api.updateCommands( ).addCommands(
                Commands.slash( "status", "Get Minecraft server status" )
        ).queue( );

        new Timer( ).schedule( new TimerTask( ) {
            public void run( ) {
                int count = plugin.getServer( ).getOnlinePlayers( ).length;
                api.getPresence( ).setActivity( Activity.playing( "Minecraft 1.7.3 with " + count + ( count == 1 ? " friend" : " friends" ) ) );
            } },0,30_000 );

        api.getPresence( ).setActivity( Activity.playing( "Minecraft 1.7.3" ) );
    }

    public void Stop( ) {
        api.shutdownNow( );
    }

    public void sendMessage( String channel, String message ) {
        if ( api.getStatus( ) != JDA.Status.CONNECTED )
            return;

        TextChannel chan = api.getTextChannelById( channel );
        if ( chan != null )
            chan.sendMessage( message ).queue( );
    }

    public JDA getApi( ) {
        return api;
    }

    @Override
    public void onReady( @NotNull ReadyEvent event ) {
        plugin.log( Level.INFO, "Logged in as " + api.getSelfUser( ).getName( ) + " (ID: " + api.getSelfUser( ).getId( ) + ")" );
        plugin.log( Level.INFO, "Java Discord API version: " + JDAInfo.VERSION );
        plugin.log( Level.INFO, "Invite link: " + api.getInviteUrl() + "&permissions=294678424784" );
    }

    @Override
    public void onMessageReceived( MessageReceivedEvent event ) {
        User author = event.getAuthor( );
        Message message = event.getMessage( );

        // Don't infinitely loop
        if ( author == api.getSelfUser( ) ) {
            return;
        }

        // Likely image, just skip
        if ( message.getContentStripped( ).isEmpty( ) ) {
            return;
        }

        if ( event.getChannel( ).getId( ).equals( plugin.getConfig( ).getString( "channel_id" ) ) &&
             Objects.equals(plugin.getConfig().getString( "logging_only"), "false" ) ) {
            plugin.sendMessage( "<" + author.getName( ) + "> §b" + message.getContentStripped( ) );
        }
    }

    @Override
    public void onSlashCommandInteraction( SlashCommandInteractionEvent event ) {
        // make sure we handle the right command
        switch ( event.getName( ) ) {
            case "status":
                // Create the EmbedBuilder instance
                EmbedBuilder embed = new EmbedBuilder( );

                embed.setTitle( "Server Status", null );
                embed.setColor( new Color( 0x0CF4C6 ) );

                embed.addField( "Player Count", plugin.getServer( ).getOnlinePlayers( ).length + "/" + plugin.getServer( ).getMaxPlayers( ), false );

                StringBuilder playerList = new StringBuilder( );
                for ( final Player player : plugin.getServer( ).getOnlinePlayers( ) ) {
                    playerList.append( player.getDisplayName( ) );
                    playerList.append( " " );
                }

                embed.addField( "Players", playerList.toString( ), false );

                embed.setFooter("Boobs farts n sex", "https://couldntbe.me/media/bsod.png");
                embed.setThumbnail("https://couldntbe.me/media/bsod.png");

                plugin.log(Level.INFO, event.getInteraction( ).getUser( ).getName( ) + " used /status" );
                event.replyEmbeds( embed.build( ) ).complete( );
            default:
        }
    }
}
