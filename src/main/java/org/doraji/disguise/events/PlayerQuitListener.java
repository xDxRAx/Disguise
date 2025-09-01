package org.doraji.disguise.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.doraji.disguise.Disguise;
import xyz.haoshoku.nick.api.NickAPI;

public class PlayerQuitListener implements Listener {
    private final Disguise plugin;

    public PlayerQuitListener(Disguise plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (NickAPI.isNicked(player)) {
            String currentNickname = NickAPI.getName(player);
            String uuid = player.getUniqueId().toString();
            this.plugin.getConfig().set("disguises." + uuid, currentNickname);
        } else {
            String uuid = player.getUniqueId().toString();
            this.plugin.getConfig().set("disguises." + uuid, (Object)null);
        }

        this.plugin.saveConfig();
    }
}
