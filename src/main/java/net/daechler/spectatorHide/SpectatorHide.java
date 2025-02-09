package net.daechler.spectatorHide;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;

public class SpectatorHide extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Register the listener (this class) with Bukkit
        Bukkit.getPluginManager().registerEvents(this, this);
        // Send a message to the console indicating the plugin has been activated
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + getName() + " has been activated!");
    }

    @Override
    public void onDisable() {
        // Send a message to the console indicating the plugin has been disabled
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + getName() + " has been disabled!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Hide any players currently in spectator mode from the newly joined player
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                event.getPlayer().hidePlayer(this, player);
            }
        }
    }

    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        // If a player switches to spectator mode, hide them from all other players
        if (event.getNewGameMode() == GameMode.SPECTATOR) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.hidePlayer(this, event.getPlayer());
            }
        }
        // If a player switches out of spectator mode, show them to all other players
        else if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.showPlayer(this, event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onTabComplete(org.bukkit.event.server.TabCompleteEvent event) {
        // Remove any players in spectator mode from the autocomplete list
        Iterator<String> iterator = event.getCompletions().iterator();
        while (iterator.hasNext()) {
            String completion = iterator.next();
            Player player = Bukkit.getPlayer(completion);
            if (player != null && player.getGameMode() == GameMode.SPECTATOR) {
                iterator.remove();
            }
        }
    }
}
