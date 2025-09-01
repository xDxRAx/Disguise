package org.doraji.disguise.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.haoshoku.nick.api.NickAPI;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DisguiseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                player.sendMessage("§c이 명령어는 OP만 사용할 수 있습니다.");
                return true;
            }
        }

        if (command.getName().equalsIgnoreCase("disguise")) {
            if (args.length == 0) {
                sender.sendMessage("§e사용법: /disguise <플레이어> [변경할 닉네임/스킨 주인] (닉네임 비워둘 시 초기화)");
                return true;
            }

            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                sender.sendMessage(ChatColor.RED + "해당 플레이어는 서버에 존재하지 않습니다.");
                return true;
            }

            if (args.length == 1) {
                NickAPI.resetNick(targetPlayer);
                NickAPI.resetProfileName(targetPlayer);
                NickAPI.resetSkin(targetPlayer);
                NickAPI.refreshPlayer(targetPlayer);
                sender.sendMessage(ChatColor.GREEN + args[0] + "님의 변장이 초기화되었습니다. [" + NickAPI.getOriginalName(targetPlayer) + "]");
                return true;
            }

            if (args.length == 2) {
                String skinOwner = args[1];
                if (Bukkit.getPlayer(skinOwner) != null) {
                    sender.sendMessage(ChatColor.RED + skinOwner + "님은 이미 서버에 접속해 있습니다. 다른 닉네임을 사용해주세요.");
                    return true;
                }
                CompletableFuture.supplyAsync(() -> getUUID(skinOwner))
                        .thenAccept(uuid -> {
                            Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("Disguise"), () -> {
                                if (uuid == null) {
                                    sender.sendMessage(ChatColor.RED + "스킨을 가져올 마인크래프트 계정 '" + skinOwner + "'이(가) 존재하지 않습니다.");
                                    return;
                                }
                                sender.sendMessage(ChatColor.GREEN + targetPlayer.getName() + "의 변장 상태가 " + skinOwner + "(으)로 변경되었습니다.");
                                NickAPI.setNick(targetPlayer, skinOwner);
                                NickAPI.setProfileName(targetPlayer, skinOwner);
                                NickAPI.setSkin(targetPlayer, skinOwner);
                                NickAPI.refreshPlayer(targetPlayer);
                            });
                        });
                return true;
            }
        }
        return false;
    }

    private UUID getUUID(String playerName) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            URLConnection conn = url.openConnection();
            conn.connect();
            JsonParser parser = new JsonParser();
            JsonObject json = (JsonObject) parser.parse(new InputStreamReader(conn.getInputStream()));
            String uuidString = json.get("id").getAsString();
            uuidString = uuidString.replaceFirst(
                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{12})",
                    "$1-$2-$3-$4-$5"
            );

            return UUID.fromString(uuidString);
        } catch (Exception e) {
            return null;
        }
    }
}
