# nerdbot - Minecraft/Discord interactivity bot for Minecraft Beta 1.7.3
## Features
* Log messages from Minecraft to a Discord channel
* Pass messages from Discord to Minecraft
* Check server status in Discord

## Compiling
* Gradle handles [JDA](https://github.com/discord-jda/JDA) dependency linking
* You'll need to link Bukkit 1.7.3 in your project's Artifacts
* Build with Gradle's shadowJar task

## Usage
* You'll need to [create a bot](https://discord.com/developers/docs/getting-started) for discord
* Put the built plugin's .jar file into your servers plugin folder
* Edit the config.yml with your bots token & wanted logging channel ID

This project was made with IntelliJ IDEA & Gradle.