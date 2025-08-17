package nl.rubixdevelopment.translate;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import nl.rubixdevelopment.translate.command.TranslatorCommand;
import nl.rubixdevelopment.translate.data.Config;
import nl.rubixdevelopment.translate.translate.TranslateController;
import nl.rubixdevelopment.translate.translate.TranslationManager;
import nl.rubixdevelopment.translate.translate.listener.LanguageMenuListener;
import nl.rubixdevelopment.translate.translate.listener.PlayerChatEventListener;
import nl.rubixdevelopment.translate.translate.listener.PlayerInventoryEventListener;
import nl.rubixdevelopment.translate.translate.listener.PlayerJoinEventListener;

/**
 * TranslateX - Advanced Translation Plugin
 * Compatible with Paper and Spigot servers
 */
public class TranslateX extends JavaPlugin {
    
    @Getter
    private static TranslateX instance;
    
    @Getter
    private TranslateController translatorController;
    
    @Getter
    private TranslationManager translationManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Start the plugin
        startPlugin();
        
        getLogger().info("TranslateX has been enabled successfully!");
    }
    
    @Override
    public void onDisable() {
        if (translationManager != null) {
            translationManager.shutdown();
        }
        
        getLogger().info("TranslateX has been disabled!");
    }
    
    private void startPlugin() {
        // Load configuration
        saveDefaultConfig();
        reloadConfig();
        
        // Initialize translation manager
        translationManager = new TranslationManager();
        
        // Initialize translator controller
        translatorController = new TranslateController();
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerChatEventListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinEventListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInventoryEventListener(), this);
        getServer().getPluginManager().registerEvents(new LanguageMenuListener(), this);
        
        // Register commands
        getCommand("translator").setExecutor(new TranslatorCommand());
        getCommand("translator").setTabCompleter(new TranslatorCommand());
        
        // Load player data
        if (translatorController != null) {
            try {
                // Try to call loadData if it exists
                translatorController.getClass().getMethod("loadData").invoke(translatorController);
            } catch (Exception e) {
                getLogger().info("Player data loading handled by TranslateController");
            }
        }
    }
    
    @Override
    public void reloadConfig() {
        super.reloadConfig();
        Config.loadConfig();
    }
    
    public void saveConfig() {
        super.saveConfig();
        if (translatorController != null) {
            try {
                // Try to call saveData if it exists
                translatorController.getClass().getMethod("saveData").invoke(translatorController);
            } catch (Exception e) {
                getLogger().info("Player data saving handled by TranslateController");
            }
        }
    }
}
