package nl.rubixstudios.translate.data;

import nl.rubixstudios.translate.TranslateX;

import java.util.List;

public class Config {

    public static List<String> API_KEYS;

    public Config() {
        ConfigFile configFile = TranslateX.getInstance().getConfigFile();

        API_KEYS = configFile.getStringList("API_KEYS");
    }
}
