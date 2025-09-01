package org.doraji.disguise.events;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.haoshoku.nick.api.NickAPI;

public class ChatListener implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        if (!event.isCancelled()) {
            Player player = event.getPlayer();
            if (NickAPI.isNicked(player)) {
                String nickname = NickAPI.getName(player);
                Component nickComponent = Component.text(nickname);
                event.renderer((source, displayName, message, viewer) -> ((TextComponent.Builder)((TextComponent.Builder)((TextComponent.Builder)((TextComponent.Builder)Component.text().append(Component.text("<"))).append(nickComponent)).append(Component.text("> "))).append(message)).build());
            }

        }
    }
}