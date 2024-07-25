package nl.rubixstudios.translate.data;

import lombok.Getter;
import nl.rubixstudios.translate.TranslateX;
import nl.rubixstudios.translate.util.ColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Getter
public class ConfigFile extends YamlConfiguration {

    protected final String name;
    protected YamlConfiguration config;
    protected ConfigCommenter commenter = new ConfigCommenter();
    protected File file;

    public ConfigFile(String name, Plugin plugin) {
        this.name = name;
        file = new File(plugin.getDataFolder(), name);
        config = new YamlConfiguration();
        if (!file.exists())
            plugin.saveResource(name, false);
        config.options().copyDefaults(true);
    }

    public void save() {
        try {
            config.save(file);
            commenter.saveComments(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addComment(String path, String comment) {
        commenter.addComment(path, comment);
    }

    public void setHeader(String header) {
        commenter.setHeader(header);
    }

    public void load() {
        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set(String path, Object val) {
        config.set(path, val);
    }

    public boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, config.getBoolean(path));
    }

    public double getDouble(String path, double def) {
        config.addDefault(path, def);
        return config.getDouble(path, config.getDouble(path));
    }

    public int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInt(path, config.getInt(path));
    }

    public <T> List getList(String path, T def) {
        config.addDefault(path, def);
        return config.getList(path, config.getList(path));
    }

    public String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    public Object get(String path, Config setting) {
        Object def = setting.getValue();
        return def instanceof Integer ? getInt(path, (Integer) def) :
                def instanceof Double ? getDouble(path, (Double) def) :
                        def instanceof Boolean ? getBoolean(path, (Boolean) def) :
                                def instanceof String ? getString(path, (String) def) :
                                        def instanceof List ? getList(path, (List) def) : def;
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path).stream().map(string -> ChatColor.translateAlternateColorCodes('&', string)).collect(Collectors.toList());
    }
}
