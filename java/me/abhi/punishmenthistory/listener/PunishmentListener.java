package me.abhi.punishmenthistory.listener;

import me.abhi.punishmenthistory.PunishmentHistory;
import me.abhi.punishmenthistory.util.PunishmentType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PunishmentListener implements Listener {

    private PunishmentHistory punishmentHistory;

    public PunishmentListener(PunishmentHistory punishmentHistory) {
        this.punishmentHistory = punishmentHistory;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        UUID uuid = null;
        String reason = null;
        boolean punishment = false;
        PunishmentType punishmentType = null;
        int punishmentNumber = 1;
        if (!player.hasPermission("punishmenthistory.punish")) {
            return;
        }
        for (String banCommands : punishmentHistory.getBanCommands()) {
            if (event.getMessage().startsWith("/" + banCommands + " ")) {
                String message = event.getMessage().replace("/" + banCommands + " ", "");
                String[] args = message.split(" ");
                String[] reasonArgs = new String[args.length - 1];
                uuid = this.punishmentHistory.getServer().getOfflinePlayer(args[0]).getUniqueId();
                for (int i = 1; i < args.length; i++) {
                    reasonArgs[i - 1] = args[i];
                }
                reason = StringUtils.join(reasonArgs, " ");
                punishment = true;
                punishmentType = PunishmentType.BAN;
            }
        }
        for (String kickCommands : punishmentHistory.getKickCommands()) {
            if (event.getMessage().startsWith("/" + kickCommands + " ")) {
                if (this.punishmentHistory.getServer().getPluginCommand(kickCommands) == null) {
                    return;
                }
                String message = event.getMessage().replace("/" + kickCommands + " ", "");
                String[] args = message.split(" ");
                String[] reasonArgs = new String[args.length - 1];
                uuid = this.punishmentHistory.getServer().getOfflinePlayer(args[0]).getUniqueId();
                for (int i = 1; i < args.length; i++) {
                    reasonArgs[i - 1] = args[i];
                }
                reason = StringUtils.join(reasonArgs, " ");
                punishment = true;
                punishmentType = PunishmentType.KICK;
            }
        }
        for (String IPBanCommands : punishmentHistory.getIPBanCommands()) {
            if (event.getMessage().startsWith("/" + IPBanCommands + " ")) {
                String message = event.getMessage().replace("/" + IPBanCommands + " ", "");
                String[] args = message.split(" ");
                String[] reasonArgs = new String[args.length - 1];
                uuid = this.punishmentHistory.getServer().getOfflinePlayer(args[0]).getUniqueId();
                for (int i = 1; i < args.length; i++) {
                    reasonArgs[i - 1] = args[i];
                }
                reason = StringUtils.join(reasonArgs, " ");
                punishment = true;
                punishmentType = PunishmentType.IPBAN;
            }
        }
        for (String muteCommands : punishmentHistory.getMuteCommands()) {
            if (event.getMessage().startsWith("/" + muteCommands + " ")) {
                String message = event.getMessage().replace("/" + muteCommands + " ", "");
                String[] args = message.split(" ");
                String[] reasonArgs = new String[args.length - 1];
                uuid = this.punishmentHistory.getServer().getOfflinePlayer(args[0]).getUniqueId();
                for (int i = 1; i < args.length; i++) {
                    reasonArgs[i - 1] = args[i];
                }
                reason = StringUtils.join(reasonArgs, " ");
                punishment = true;
                punishmentType = PunishmentType.MUTE;
            }
        }
        for (String IPMuteCommands : punishmentHistory.getIPMuteCommands()) {
            if (event.getMessage().startsWith("/" + IPMuteCommands + " ")) {
                String message = event.getMessage().replace("/" + IPMuteCommands + " ", "");
                String[] args = message.split(" ");
                String[] reasonArgs = new String[args.length - 1];
                uuid = this.punishmentHistory.getServer().getOfflinePlayer(args[0]).getUniqueId();
                for (int i = 1; i < args.length; i++) {
                    reasonArgs[i - 1] = args[i];
                }
                reason = StringUtils.join(reasonArgs, " ");
                punishment = true;
                punishmentType = PunishmentType.IPMUTE;
            }
        }
        if (punishment) {
            if (this.punishmentHistory.getHistoryFile().get("history." + uuid + "." + punishmentType.toString()) != null) {
                for (String punishments : this.punishmentHistory.getHistoryFile().getConfigurationSection("history." + uuid + "." + punishmentType.toString()).getKeys(false)) {
                    punishmentNumber += 1;
                }
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(punishmentHistory.getDateFormat());
            punishmentHistory.getHistoryFile().set("history." + uuid + "." + punishmentType.toString() + "." + punishmentNumber + ".reason", reason);
            punishmentHistory.getHistoryFile().set("history." + uuid + "." + punishmentType.toString() + "." + punishmentNumber + ".date", simpleDateFormat.format(new Date()).toString());
            punishmentHistory.getHistoryFile().set("history." + uuid + "." + punishmentType.toString() + "." + punishmentNumber + ".executor", player.getUniqueId().toString());
            punishmentHistory.saveHistoryFile();
        }
    }
}
