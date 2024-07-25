package nl.rubixstudios.translate;

import lombok.Getter;
import lombok.Setter;
import nl.rubixstudios.translate.command.TranslatorCommand;
import nl.rubixstudios.translate.data.Config;
import nl.rubixstudios.translate.data.ConfigFile;
import nl.rubixstudios.translate.translate.TranslateController;
import nl.rubixstudios.translate.util.ColorUtil;
import nl.rubixstudios.translate.util.Metrics;
import nl.rubixstudios.translate.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

@Getter
public final class TranslateX extends JavaPlugin {

    @Getter private static TranslateX instance;
    @Setter private ConfigFile configFile;
    private Metrics metrics;

    private TranslateController translatorController;

    @Override
    public void onEnable() {
        instance = this;

        this.log("&e                                                                                                    ");
        this.log("&e /$$$$$$$$                                     &f/$$              &f/$$               &f/$$   /$$");
        this.log("&e|__  $$__/                                    &f| $$              &f| $$              |&f $$  / $$");
        this.log("&e   | $$  /&f$$$$$$  &e/$$$$$$  &f/$$$$$$$    &e/$$$$$$$&f| $$  &e/$$$$$$  &f/$$$$$$    &e/$$$$$$ &f| &f$$/$$/");
        this.log("&e   | $$ /&f$$__  $$&e|____  $$| &f$$__  $$  &e/$$_____/&f| $$ &e|____  $$|_  &f$$_/   &e/$$__  $$ &f\\ &f$$$$/");
        this.log("&e   | $$| &f$$  \\__/ &e/$$$$$$$| &f$$  \\ &f$$&e | $$$$$$  &f| $$  &e/$$$$$$$  &f| $$    &e| $$$$$$$$ &f$$  $$ ");
        this.log("&e   | $$| &f$$      &e/$$__  $$| &f$$  | $$  &e\\____  $$&f| $$ &e/$$__  $$  | &f$$ /$$| &e$$_____/ &f/$$/\\ $$");
        this.log("&e   | $$| &f$$     &e|  $$$$$$$| &f$$  | $$ / &e$$$$$$$/| &f$$|  &e$$$$$$$  |  &f$$$$/|  &e$$$$$$$| &f$$  \\ $$");
        this.log("&e   |__/&f|__/      &e\\_______/&f|__/  |__/ &e|_______/ &f|__/ &e\\_______/   &f\\___/   &e\\_______/&f|__/  |__/");
        this.log("&e                                                                                                    ");
        this.log("&e&lDescription: &fTranslateX is a plugin that allows you to translate messages in your server.             ");
        this.log("&e&lAuthor: &fDjorr (Rubix Development)                                                             ");
        this.log("&eVersion: &f0.1-BETA                                                                             ");
        this.log("&eDiscord: &fhttps://discord.rubixdevelopment.nl/                                                        ");
        this.log("                                                                                                    ");

        try {
            this.configFile = new ConfigFile("config.yml", this);
            reloadConfig();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.translatorController = new TranslateController();
        this.translatorController.intializeTranslator();

        final PluginCommand command = this.getCommand("translator");
        if (command != null) {
            command.setExecutor(new TranslatorCommand());
            command.setTabCompleter(new TranslatorCommand());
            command.setAliases(Arrays.asList("t", "translatorx", "tx"));
            command.setPermission("translatorx.translate");
        }

        this.log("");
        this.log("- &aSuccesfully enabled &e&lTranslateX &aplugin.");
        this.log("&e                                                                                                    ");

        int pluginId = 19970;
        metrics = new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {
        if (this.translatorController != null) this.translatorController.disable();
        if (metrics != null) metrics.shutdown();
    }

    @Override
    public void reloadConfig() {
        configFile.load();

        configFile.setHeader("                                                                                                    \n" +
                "|__  $$__/                                    | $$              | $$              | $$  / $$\n" +
                "   | $$  /$$$$$$  /$$$$$$  /$$$$$$$    /$$$$$$$| $$  /$$$$$$  /$$$$$$    /$$$$$$ | $$/$$/\n" +
                "   | $$ /$$__  $$|____  $$| $$__  $$  /$$_____/| $$ |____  $$|_  $$_/   /$$__  $$ \\ $$$$/\n" +
                "   | $$| $$  \\__/ /$$$$$$$| $$  \\ $$ | $$$$$$  | $$  /$$$$$$$  | $$    | $$$$$$$$ $$  $$ \n" +
                "   | $$| $$      /$$__  $$| $$  | $$  \\____  $$| $$ /$$__  $$  | $$ /$$| $$_____/ /$$/\\ $$\n" +
                "   | $$| $$     |  $$$$$$$| $$  | $$ / $$$$$$$/| $$|  $$$$$$$  |  $$$$/|  $$$$$$$| $$  \\ $$\n" +
                "   |__/|__/      \\_______/|__/  |__/ |_______/ |__/ \\_______/   \\___/   \\_______/|__/  |__/\n" +
                "                                                                                                    \n" +
                "Description: TranslateX is a plugin that allows you to translate messages in your server.             \n" +
                "Author: Djorr (Rubix Development)                                                             \n" +
                "Version: 0.1-BETA                                                                             \n" +
                "Discord: https://discord.rubixdevelopment.nl/                                                        \n");
        for (Config setting : Config.values()) {
            String path = setting.getConfigSection();
            if (!path.isEmpty()) {
                if (!StringUtil.isNullOrEmpty(setting.getComment()))
                    configFile.addComment(path, setting.getComment());
                setting.setValue(configFile.get(path, setting));
            }
        }
        configFile.save();

        if (this.translatorController != null) {
            this.translatorController.reloadTranslator();
        }
    }

    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(message));
    }
}
