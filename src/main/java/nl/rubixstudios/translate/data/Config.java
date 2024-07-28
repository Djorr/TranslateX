package nl.rubixstudios.translate.data;

import lombok.Getter;
import nl.rubixstudios.translate.TranslateX;
import nl.rubixstudios.translate.util.JavaUtil;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

@Getter

public class Config {
    public static List<String> API_KEYS;

    public Config() {
        ConfigFile configFile = TranslateX.getInstance().getConfigFile();
        API_KEYS = configFile.getStringList("API_KEYS");
    }
}
