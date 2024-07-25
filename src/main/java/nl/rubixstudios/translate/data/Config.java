package nl.rubixstudios.translate.data;

import lombok.Getter;
import nl.rubixstudios.translate.TranslateX;
import nl.rubixstudios.translate.util.JavaUtil;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Config {
    API_KEYS(Arrays.asList("PUT", "HERE", "YOUR", "API KEYS"), "", "API_KEYS");

    private final String configSection, comment;
    private Object value;

    Config(Object value, String comment, String configSection) {
        this.value = value;
        this.comment = comment;
        this.configSection = configSection;
    }

    public boolean asBoolean() {
        return (Boolean) value;
    }

    public double asDouble() {
        if (value instanceof Integer)
            return (Integer) value;
        return (Double) value;
    }

    public List<?> asList() {
        return (List<?>) value;
    }

    public int asInt() {
        if (value instanceof Double)
            return ((Double) value).intValue();
        return (Integer) value;
    }

    public void setValue(Object value) {
        if (value instanceof List) {
            value = JavaUtil.createList(value, String.class, false);
        }
        this.value = value;
    }

    public void set(Object value) {
        setValue(value);
        ConfigFile config = TranslateX.getInstance().getConfigFile();
        config.set(configSection, value);
        config.save();
    }

    public String asString() {
        return ChatColor.translateAlternateColorCodes('&', ((String) value));
    }
}
