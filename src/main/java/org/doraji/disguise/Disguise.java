package org.doraji.disguise;

import org.bukkit.plugin.java.JavaPlugin;
import org.doraji.disguise.commands.DisguiseCommand;
import org.doraji.disguise.events.ChatListener;
import org.doraji.disguise.events.PlayerJoinListener;
import org.doraji.disguise.events.PlayerQuitListener;

public final class Disguise extends JavaPlugin {
    public void onEnable() {
        this.getLogger().info("플러그인 활성화");
        this.getServer().getPluginManager().registerEvents(new ChatListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        this.getServer().getPluginCommand("disguise").setExecutor(new DisguiseCommand());
        this.saveDefaultConfig();
    }

    public void onDisable() {
        this.getLogger().info("플러그인 비활성화");
    }
}