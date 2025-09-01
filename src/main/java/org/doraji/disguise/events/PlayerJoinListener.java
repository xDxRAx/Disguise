package org.doraji.disguise.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.doraji.disguise.Disguise;
import xyz.haoshoku.nick.api.NickAPI;

public class PlayerJoinListener implements Listener {
    private final Disguise plugin;

    public PlayerJoinListener(Disguise plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        String savedNickname = this.plugin.getConfig().getString("disguises." + uuid);
        if (savedNickname != null && !savedNickname.isEmpty()) {
            NickAPI.setNick(player, savedNickname);
            NickAPI.setProfileName(player, savedNickname);
            NickAPI.setSkin(player, savedNickname);
            NickAPI.refreshPlayer(player);
            Component originalMessage = event.joinMessage();
            if (originalMessage != null) {
                TextReplacementConfig replacement = (TextReplacementConfig)TextReplacementConfig.builder().matchLiteral(NickAPI.getOriginalName(player)).replacement(Component.text(savedNickname, NamedTextColor.YELLOW)).build();
                Component newJoinMessage = originalMessage.replaceText(replacement);
                event.joinMessage(newJoinMessage);
            }
        }

    }
}
