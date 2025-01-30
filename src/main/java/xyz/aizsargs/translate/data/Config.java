package xyz.aizsargs.translate.data;

import lombok.Getter;
import xyz.aizsargs.translate.TranslateX;
import org.bukkit.ChatColor;

import java.util.List;

@Getter

public class Config {
    public static List<String> API_KEYS;

    public Config() {
        ConfigFile configFile = TranslateX.getInstance().getConfigFile();
        API_KEYS = configFile.getStringList("API_KEYS");
    }
}
