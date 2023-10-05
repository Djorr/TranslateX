package nl.rubixstudios.translate.data;

import lombok.Getter;
import nl.rubixstudios.translate.TranslateX;
import nl.rubixstudios.translate.util.ColorUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Getter
public class ConfigFile extends YamlConfiguration {

    private static final TranslateX mainInstance = TranslateX.getInstance();

    private final File file;

    public ConfigFile(String name) throws RuntimeException {
        this.file = new File(mainInstance.getDataFolder(), name);

        if (!this.file.exists()) {
            mainInstance.saveResource(name, false);
        }

        try {
            this.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            mainInstance.log("");
            mainInstance.log("&9===&b=============================================&9===");
            mainInstance.log(center("&cError occurred while loading " + name + ".", 51));
            mainInstance.log("");

            Stream.of(e.getMessage().split("\n")).forEach(mainInstance::log);

            mainInstance.log("&9===&b=============================================&9===");
            throw new RuntimeException();
        }
    }

    public String center(String value, int maxLength) {
        StringBuilder builder = new StringBuilder(maxLength - value.length());
        IntStream.range(0, maxLength - value.length()).forEach(i -> builder.append(" "));

        builder.insert((builder.length() / 2) + 1, value);
        return builder.toString();
    }

    public void save() {
        try {
            this.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConfigurationSection getSection(String name) {
        return super.getConfigurationSection(name);
    }

    @Override
    public int getInt(String path) {
        return super.getInt(path, 0);
    }

    @Override
    public double getDouble(String path) {
        return super.getDouble(path, 0.0);
    }

    @Override
    public boolean getBoolean(String path) {
        return super.getBoolean(path, false);
    }

    @Override
    public String getString(String path) {
        return ColorUtil.translate(super.getString(path, ""));
    }

    @Override
    public List<String> getStringList(String path) {
        return super.getStringList(path).stream().map(ColorUtil::translate).collect(Collectors.toList());
    }
}
