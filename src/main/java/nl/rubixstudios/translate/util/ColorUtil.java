package nl.rubixstudios.translate.util;

import org.bukkit.ChatColor;

public class ColorUtil {

    public static String translate(String line) {
        return ChatColor.translateAlternateColorCodes('&', line);
    }

    public static String strip(String line) {
        return ChatColor.stripColor(line);
    }
}
