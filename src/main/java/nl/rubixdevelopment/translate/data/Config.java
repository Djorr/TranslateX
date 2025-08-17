package nl.rubixdevelopment.translate.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import nl.rubixdevelopment.translate.TranslateX;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Configuration management for TranslateX
 */
public class Config {
    
    // Plugin settings
    public static boolean PLUGIN_ENABLED = true;
    public static String DEFAULT_LANGUAGE = "EN";
    public static boolean DEBUG = false;
    public static int MAX_MESSAGE_LENGTH = 1000;
    public static int CACHE_DURATION = 30;
    
    // OpenAI settings
    public static boolean OPENAI_ENABLED = true;
    public static List<String> OPENAI_API_KEYS = Arrays.asList("<YOUR_OPENAI_API_KEY>");
    public static String OPENAI_MODEL = "gpt-3.5-turbo";
    public static int OPENAI_MAX_TOKENS = 150;
    public static double OPENAI_TEMPERATURE = 0.3;
    
    // Features
    public static boolean CHAT_TRANSLATION_ENABLED = true;
    public static boolean AUTO_TRANSLATE_INCOMING = true;
    public static boolean SHOW_ORIGINAL_MESSAGE = true;
    public static boolean ITEM_TRANSLATION_ENABLED = true;
    
    // Performance
    public static int THREAD_POOL_SIZE = 4;
    public static int MAX_CACHE_SIZE = 10000;
    public static int EXPIRATION_TIME = 60;
    
    // Messages
    public static String MESSAGE_PREFIX = "&6&lTranslateX &8» &f";
    public static String LANGUAGE_SET_MESSAGE = "&aYour language has been set to: {language} ({code})";
    public static String TRANSLATION_SUCCESS = "&aMessage translated successfully!";
    public static String TRANSLATION_FAILED = "&cTranslation failed. Please try again.";
    public static String NO_PERMISSION = "&cYou don't have permission to use this command.";
    public static String INVALID_LANGUAGE = "&cInvalid language specified.";
    
    // Permissions
    public static String TRANSLATE_PERMISSION = "translatorx.translate";
    public static String TRANSLATE_ITEMS_PERMISSION = "translatorx.translate.items";
    public static String ADMIN_PERMISSION = "translatorx.admin";
    public static String BYPASS_RATELIMIT_PERMISSION = "translatorx.bypass.ratelimit";
    
    /**
     * Load configuration from config.yml
     */
    public static void loadConfig() {
        File configFile = new File(TranslateX.getInstance().getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            TranslateX.getInstance().saveDefaultConfig();
        }
        
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        
        // Load values from config
        PLUGIN_ENABLED = config.getBoolean("GENERAL.enabled", true);
        DEFAULT_LANGUAGE = config.getString("GENERAL.default_language", "EN");
        DEBUG = config.getBoolean("GENERAL.debug", false);
        MAX_MESSAGE_LENGTH = config.getInt("GENERAL.max_message_length", 1000);
        CACHE_DURATION = config.getInt("GENERAL.cache_duration", 30);
        
        // OpenAI settings
        OPENAI_ENABLED = config.getBoolean("TRANSLATION_PROVIDERS.openai.enabled", true);
        OPENAI_API_KEYS = config.getStringList("TRANSLATION_PROVIDERS.openai.api_keys");
        OPENAI_MODEL = config.getString("TRANSLATION_PROVIDERS.openai.model", "gpt-3.5-turbo");
        OPENAI_MAX_TOKENS = config.getInt("TRANSLATION_PROVIDERS.openai.max_tokens", 150);
        OPENAI_TEMPERATURE = config.getDouble("TRANSLATION_PROVIDERS.openai.temperature", 0.3);
        
        // Features
        CHAT_TRANSLATION_ENABLED = config.getBoolean("FEATURES.chat_translation.enabled", true);
        AUTO_TRANSLATE_INCOMING = config.getBoolean("FEATURES.chat_translation.auto_translate_incoming", true);
        SHOW_ORIGINAL_MESSAGE = config.getBoolean("FEATURES.chat_translation.show_original", true);
        ITEM_TRANSLATION_ENABLED = config.getBoolean("FEATURES.item_translation.enabled", true);
        
        // Performance
        THREAD_POOL_SIZE = config.getInt("PERFORMANCE.async.thread_pool_size", 4);
        MAX_CACHE_SIZE = config.getInt("PERFORMANCE.caching.max_cache_size", 10000);
        EXPIRATION_TIME = config.getInt("PERFORMANCE.caching.expiration_time", 60);
        
        // Messages
        MESSAGE_PREFIX = config.getString("MESSAGES.prefix", "&6&lTranslateX &8» &f");
        LANGUAGE_SET_MESSAGE = config.getString("MESSAGES.language_set", "&aYour language has been set to: {language} ({code})");
        TRANSLATION_SUCCESS = config.getString("MESSAGES.translation_success", "&aMessage translated successfully!");
        TRANSLATION_FAILED = config.getString("MESSAGES.translation_failed", "&cTranslation failed. Please try again.");
        NO_PERMISSION = config.getString("MESSAGES.no_permission", "&cYou don't have permission to use this command.");
        INVALID_LANGUAGE = config.getString("MESSAGES.invalid_language", "&cInvalid language specified.");
        
        // Permissions
        TRANSLATE_PERMISSION = config.getString("PERMISSIONS.translate", "translatorx.translate");
        TRANSLATE_ITEMS_PERMISSION = config.getString("PERMISSIONS.translate_items", "translatorx.translate.items");
        ADMIN_PERMISSION = config.getString("PERMISSIONS.admin", "translatorx.admin");
        BYPASS_RATELIMIT_PERMISSION = config.getString("PERMISSIONS.bypass_rate_limit", "translatorx.bypass.ratelimit");
    }
}
