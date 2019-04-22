package me.abhi.punishmenthistory.commands;

import me.abhi.punishmenthistory.PunishmentHistory;
import me.abhi.punishmenthistory.util.PunishmentType;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class HistoryCommand implements CommandExecutor {

    private PunishmentHistory punishmentHistory;

    public HistoryCommand(PunishmentHistory punishmentHistory) {
        this.punishmentHistory = punishmentHistory;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("punishmenthistory.command.history")) {
            sender.sendMessage(punishmentHistory.getNoPermission());
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " <player> <BAN:KICK:IPBAN:MUTE:IPMUTE>");
            return true;
        }
        OfflinePlayer offlinePlayer = punishmentHistory.getServer().getOfflinePlayer(args[0]);
        if (offlinePlayer == null) {
            sender.sendMessage(punishmentHistory.getPlayerNotFound());
            return true;
        }
        if (!args[1].equalsIgnoreCase("ban") && !args[1].equalsIgnoreCase("kick") && !args[1].equalsIgnoreCase("ipban") && !args[1].equalsIgnoreCase("mute") && !args[1].equalsIgnoreCase("ipmute")) {
            sender.sendMessage(punishmentHistory.getInvalidType());
            return true;
        }
        PunishmentType punishmentType = PunishmentType.valueOf(args[1].toUpperCase());
        if (punishmentHistory.getHistoryFile().get("history." + offlinePlayer.getUniqueId() + "." + punishmentType) == null) {
            sender.sendMessage(punishmentHistory.getNoHistoryFound());
            return true;
        }
        for (String punishments : punishmentHistory.getHistoryFile().getConfigurationSection("history." + offlinePlayer.getUniqueId() + "." + punishmentType.toString()).getKeys(false)) {
            String date = punishmentHistory.getHistoryFile().getString("history." + offlinePlayer.getUniqueId() + "." + punishmentType.toString() + "." + punishments + ".date");
            String reason = punishmentHistory.getHistoryFile().getString("history." + offlinePlayer.getUniqueId() + "." + punishmentType.toString() + "." + punishments + ".reason");
            String executor = punishmentHistory.getServer().getOfflinePlayer(UUID.fromString(punishmentHistory.getHistoryFile().getString("history." + offlinePlayer.getUniqueId() + "." + punishmentType.toString() + "." + punishments + ".executor"))).getName();
            for (String history : punishmentHistory.getHistory()) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', history).replace("%date%", date).replace("%reason%", reason).replace("%executor%", executor).replace("%type%", punishmentType.toString()));
            }
        }
        return true;
    }
}
