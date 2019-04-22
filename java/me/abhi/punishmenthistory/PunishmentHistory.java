package me.abhi.punishmenthistory;

import lombok.Getter;
import me.abhi.punishmenthistory.commands.HistoryCommand;
import me.abhi.punishmenthistory.listener.PunishmentListener;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

@Getter
public class PunishmentHistory extends JavaPlugin {

    private static PunishmentHistory instance;
    private File hFile;
    private FileConfiguration historyFile;
    private String noPermission;
    private String noHistoryFound;
    private String invalidType;
    private String playerNotFound;
    private String dateFormat;
    private List<String> history;
    private List<String> banCommands;
    private List<String> kickCommands;
    private List<String> IPBanCommands;
    private List<String> MuteCommands;
    private List<String> IPMuteCommands;
    private List<String> unbanCommands;

    @Override
    public void onEnable() {
        instance = this;
        loadConfigs();
        registerListeners();
        registerCommands();
    }

    private void loadConfigs() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        try {
            hFile = new File(getDataFolder(), "history.yml");
            if (!hFile.exists()) {
                hFile.createNewFile();
            }
            historyFile = YamlConfiguration.loadConfiguration(hFile);
        } catch (Exception ex) {
            System.out.println("[PunishmentHistory] There was an error loading the history.yml. Please report the following error:");
            ex.printStackTrace();
        }
        noPermission = ChatColor.translateAlternateColorCodes('&', getConfig().getString("no-permission"));
        noHistoryFound = ChatColor.translateAlternateColorCodes('&', getConfig().getString("no-history-found"));
        invalidType = ChatColor.translateAlternateColorCodes('&', getConfig().getString("invalid-type"));
        playerNotFound = ChatColor.translateAlternateColorCodes('&', getConfig().getString("player-not-found"));
        history = getConfig().getStringList("history");
        dateFormat = getConfig().getString("date-format");
        banCommands = getConfig().getStringList("ban-commands");
        kickCommands = getConfig().getStringList("kick-commands");
        IPBanCommands = getConfig().getStringList("ipban-commands");
        MuteCommands = getConfig().getStringList("mute-commands");
        IPMuteCommands = getConfig().getStringList("ipmute-commands");
        unbanCommands = getConfig().getStringList("unban-commands");
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PunishmentListener(this), this);
    }

    private void registerCommands() {
        getCommand("history").setExecutor(new HistoryCommand(this));
    }

    public void saveHistoryFile() {
        try {
            getHistoryFile().save(getHFile());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
