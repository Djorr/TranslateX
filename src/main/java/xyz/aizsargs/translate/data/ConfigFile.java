package xyz.aizsargs.translate.data;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import xyz.aizsargs.translate.TranslateX;
import xyz.aizsargs.translate.util.ColorUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigFile extends YamlConfiguration {
    private static final TranslateX mainInstance = TranslateX.getInstance();

    private final File file;

    public File getFile() {
        return this.file;
    }

    public ConfigFile(String name) throws RuntimeException {
        this.file = new File(mainInstance.getDataFolder(), name);
        if (!this.file.exists())
            mainInstance.saveResource(name, false);
        try {
            load(this.file);
        } catch (IOException|org.bukkit.configuration.InvalidConfigurationException e) {
            mainInstance.log("");
            mainInstance.log("&9===&b=============================================&9===");
            mainInstance.log(center("&cError occurred while loading " + name + ".", 51));
            mainInstance.log("");
            Stream.<String>of(e.getMessage().split("\n")).forEach(mainInstance::log);
            mainInstance.log("&9===&b=============================================&9===");
            throw new RuntimeException();
        }
    }

    public String center(String value, int maxLength) {
        StringBuilder builder = new StringBuilder(maxLength - value.length());
        IntStream.range(0, maxLength - value.length()).forEach(i -> builder.append(" "));
        builder.insert(builder.length() / 2 + 1, value);
        return builder.toString();
    }

    public void save() {
        try {
            save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConfigurationSection getSection(String name) {
        return getConfigurationSection(name);
    }

    public int getInt(String path) {
        return getInt(path, 0);
    }

    public double getDouble(String path) {
        return getDouble(path, 0.0D);
    }

    public boolean getBoolean(String path) {
        return getBoolean(path, false);
    }

    public String getString(String path) {
        return ColorUtil.translate(getString(path, ""));
    }

    public List<String> getStringList(String path) {
        return (List<String>)super.getStringList(path).stream().map(ColorUtil::translate).collect(Collectors.toList());
    }
}
