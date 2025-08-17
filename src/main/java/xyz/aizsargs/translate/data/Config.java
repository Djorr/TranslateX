package xyz.aizsargs.translate.data;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.aizsargs.translate.TranslateX;

import java.util.List;
import java.util.Arrays;

@Getter
public class Config {

    // General settings
    public static boolean PLUGIN_ENABLED;
    public static String DEFAULT_LANGUAGE;
    public static boolean DEBUG_MODE;
    public static int MAX_MESSAGE_LENGTH;
    public static int CACHE_DURATION;

    // Translation providers
    public static List<String> PROVIDER_PRIORITY;
    public static boolean DEEPL_ENABLED;
    public static List<String> DEEPL_API_KEYS;
    public static String DEEPL_FORMALITY;
    public static boolean DEEPL_PRESERVE_FORMATTING;
    
    public static boolean OPENAI_ENABLED;
    public static List<String> OPENAI_API_KEYS;
    public static String OPENAI_MODEL;
    public static int OPENAI_MAX_TOKENS;
    public static double OPENAI_TEMPERATURE;
    
    public static boolean GOOGLE_ENABLED;
    public static List<String> GOOGLE_API_KEYS;
    public static String GOOGLE_SOURCE_LANGUAGE;
    
    public static boolean FALLBACK_ENABLED;

    // Features
    public static boolean CHAT_TRANSLATION_ENABLED;
    public static boolean AUTO_TRANSLATE_INCOMING;
    public static boolean AUTO_TRANSLATE_OUTGOING;
    public static boolean SHOW_ORIGINAL_MESSAGE;
    
    public static boolean ITEM_TRANSLATION_ENABLED;
    public static boolean TRANSLATE_ITEM_NAMES;
    public static boolean TRANSLATE_ITEM_LORE;
    public static boolean TRANSLATE_ITEM_DESCRIPTIONS;
    public static boolean CACHE_ITEM_TRANSLATIONS;
    
    public static boolean COMMAND_TRANSLATION_ENABLED;
    public static boolean TRANSLATE_COMMAND_HELP;
    public static boolean TRANSLATE_COMMAND_FEEDBACK;
    
    public static boolean SIGN_TRANSLATION_ENABLED;
    public static boolean TRANSLATE_SIGNS_ON_PLACE;
    public static boolean TRANSLATE_EXISTING_SIGNS;

    // User interface
    public static boolean LANGUAGE_MENU_ENABLED;
    public static boolean SHOW_LANGUAGE_FLAGS;
    public static boolean SHOW_NATIVE_NAMES;
    public static boolean SHOW_LANGUAGE_CODES;
    
    public static boolean TRANSLATION_FEEDBACK_ENABLED;
    public static boolean SHOW_QUALITY_INDICATOR;
    public static boolean SHOW_SOURCE_LANGUAGE;
    
    public static String CHAT_FORMAT;
    public static boolean SHOW_TRANSLATION_PROVIDER;

    // Performance
    public static boolean RATE_LIMITING_ENABLED;
    public static int MAX_REQUESTS_PER_MINUTE;
    public static int MAX_GLOBAL_REQUESTS_PER_MINUTE;
    
    public static boolean CACHING_ENABLED;
    public static int MAX_CACHE_SIZE;
    public static int CACHE_EXPIRATION_TIME;
    
    public static boolean ASYNC_ENABLED;
    public static int THREAD_POOL_SIZE;

    // Messages
    public static String MESSAGE_PREFIX;
    public static String LANGUAGE_SET_MESSAGE;
    public static String TRANSLATION_SUCCESS_MESSAGE;
    public static String TRANSLATION_FAILED_MESSAGE;
    public static String NO_PERMISSION_MESSAGE;
    public static String INVALID_LANGUAGE_MESSAGE;
    public static String RATE_LIMIT_EXCEEDED_MESSAGE;
    public static String API_KEY_INVALID_MESSAGE;

    // Permissions
    public static String TRANSLATE_PERMISSION;
    public static String TRANSLATE_ITEMS_PERMISSION;
    public static String ADMIN_PERMISSION;
    public static String BYPASS_RATE_LIMIT_PERMISSION;

    public static void loadConfig() {
        FileConfiguration config = TranslateX.getInstance().getConfigFile();
        
        // General settings
        PLUGIN_ENABLED = config.getBoolean("GENERAL.enabled", true);
        DEFAULT_LANGUAGE = config.getString("GENERAL.default_language", "EN");
        DEBUG_MODE = config.getBoolean("GENERAL.debug", false);
        MAX_MESSAGE_LENGTH = config.getInt("GENERAL.max_message_length", 1000);
        CACHE_DURATION = config.getInt("GENERAL.cache_duration", 30);

        // Translation providers
        PROVIDER_PRIORITY = config.getStringList("TRANSLATION_PROVIDERS.priority_order");
        if (PROVIDER_PRIORITY.isEmpty()) {
            PROVIDER_PRIORITY = Arrays.asList("deepl", "openai", "google", "fallback");
        }
        
        DEEPL_ENABLED = config.getBoolean("TRANSLATION_PROVIDERS.deepl.enabled", true);
        DEEPL_API_KEYS = config.getStringList("TRANSLATION_PROVIDERS.deepl.api_keys");
        DEEPL_FORMALITY = config.getString("TRANSLATION_PROVIDERS.deepl.formality", "default");
        DEEPL_PRESERVE_FORMATTING = config.getBoolean("TRANSLATION_PROVIDERS.deepl.preserve_formatting", true);
        
        OPENAI_ENABLED = config.getBoolean("TRANSLATION_PROVIDERS.openai.enabled", true);
        OPENAI_API_KEYS = config.getStringList("TRANSLATION_PROVIDERS.openai.api_keys");
        OPENAI_MODEL = config.getString("TRANSLATION_PROVIDERS.openai.model", "gpt-3.5-turbo");
        OPENAI_MAX_TOKENS = config.getInt("TRANSLATION_PROVIDERS.openai.max_tokens", 150);
        OPENAI_TEMPERATURE = config.getDouble("TRANSLATION_PROVIDERS.openai.temperature", 0.3);
        
        GOOGLE_ENABLED = config.getBoolean("TRANSLATION_PROVIDERS.google.enabled", true);
        GOOGLE_API_KEYS = config.getStringList("TRANSLATION_PROVIDERS.google.api_keys");
        GOOGLE_SOURCE_LANGUAGE = config.getString("TRANSLATION_PROVIDERS.google.source_language", "auto");
        
        FALLBACK_ENABLED = config.getBoolean("TRANSLATION_PROVIDERS.fallback.enabled", true);

        // Features
        CHAT_TRANSLATION_ENABLED = config.getBoolean("FEATURES.chat_translation.enabled", true);
        AUTO_TRANSLATE_INCOMING = config.getBoolean("FEATURES.chat_translation.auto_translate_incoming", true);
        AUTO_TRANSLATE_OUTGOING = config.getBoolean("FEATURES.chat_translation.auto_translate_outgoing", false);
        SHOW_ORIGINAL_MESSAGE = config.getBoolean("FEATURES.chat_translation.show_original", true);
        
        ITEM_TRANSLATION_ENABLED = config.getBoolean("FEATURES.item_translation.enabled", true);
        TRANSLATE_ITEM_NAMES = config.getBoolean("FEATURES.item_translation.translate_names", true);
        TRANSLATE_ITEM_LORE = config.getBoolean("FEATURES.item_translation.translate_lore", true);
        TRANSLATE_ITEM_DESCRIPTIONS = config.getBoolean("FEATURES.item_translation.translate_descriptions", true);
        CACHE_ITEM_TRANSLATIONS = config.getBoolean("FEATURES.item_translation.cache_translations", true);
        
        COMMAND_TRANSLATION_ENABLED = config.getBoolean("FEATURES.command_translation.enabled", true);
        TRANSLATE_COMMAND_HELP = config.getBoolean("FEATURES.command_translation.translate_help", true);
        TRANSLATE_COMMAND_FEEDBACK = config.getBoolean("FEATURES.command_translation.translate_feedback", true);
        
        SIGN_TRANSLATION_ENABLED = config.getBoolean("FEATURES.sign_translation.enabled", true);
        TRANSLATE_SIGNS_ON_PLACE = config.getBoolean("FEATURES.sign_translation.translate_on_place", true);
        TRANSLATE_EXISTING_SIGNS = config.getBoolean("FEATURES.sign_translation.translate_existing", false);

        // User interface
        LANGUAGE_MENU_ENABLED = config.getBoolean("USER_INTERFACE.language_menu.enabled", true);
        SHOW_LANGUAGE_FLAGS = config.getBoolean("USER_INTERFACE.language_menu.show_flags", true);
        SHOW_NATIVE_NAMES = config.getBoolean("USER_INTERFACE.language_menu.show_native_names", true);
        SHOW_LANGUAGE_CODES = config.getBoolean("USER_INTERFACE.language_menu.show_codes", true);
        
        TRANSLATION_FEEDBACK_ENABLED = config.getBoolean("USER_INTERFACE.translation_feedback.enabled", true);
        SHOW_QUALITY_INDICATOR = config.getBoolean("USER_INTERFACE.translation_feedback.show_quality", true);
        SHOW_SOURCE_LANGUAGE = config.getBoolean("USER_INTERFACE.translation_feedback.show_source_language", true);
        
        CHAT_FORMAT = config.getString("USER_INTERFACE.chat_format.format", "&6&l[&e{source_lang}&6&l → &a{target_lang}&6&l] &f{message}");
        SHOW_TRANSLATION_PROVIDER = config.getBoolean("USER_INTERFACE.chat_format.show_provider", true);

        // Performance
        RATE_LIMITING_ENABLED = config.getBoolean("PERFORMANCE.rate_limiting.enabled", true);
        MAX_REQUESTS_PER_MINUTE = config.getInt("PERFORMANCE.rate_limiting.max_requests_per_minute", 30);
        MAX_GLOBAL_REQUESTS_PER_MINUTE = config.getInt("PERFORMANCE.rate_limiting.max_global_requests_per_minute", 1000);
        
        CACHING_ENABLED = config.getBoolean("PERFORMANCE.caching.enabled", true);
        MAX_CACHE_SIZE = config.getInt("PERFORMANCE.caching.max_cache_size", 10000);
        CACHE_EXPIRATION_TIME = config.getInt("PERFORMANCE.caching.expiration_time", 60);
        
        ASYNC_ENABLED = config.getBoolean("PERFORMANCE.async.enabled", true);
        THREAD_POOL_SIZE = config.getInt("PERFORMANCE.async.thread_pool_size", 4);

        // Messages
        MESSAGE_PREFIX = config.getString("MESSAGES.prefix", "&6&lTranslateX &8» &f");
        LANGUAGE_SET_MESSAGE = config.getString("MESSAGES.language_set", "&aYour language has been set to: {language} ({code})");
        TRANSLATION_SUCCESS_MESSAGE = config.getString("MESSAGES.translation_success", "&aMessage translated successfully!");
        TRANSLATION_FAILED_MESSAGE = config.getString("MESSAGES.translation_failed", "&cTranslation failed. Please try again.");
        NO_PERMISSION_MESSAGE = config.getString("MESSAGES.no_permission", "&cYou don't have permission to use this command.");
        INVALID_LANGUAGE_MESSAGE = config.getString("MESSAGES.invalid_language", "&cInvalid language specified.");
        RATE_LIMIT_EXCEEDED_MESSAGE = config.getString("MESSAGES.rate_limit_exceeded", "&cYou've exceeded the rate limit. Please wait before trying again.");
        API_KEY_INVALID_MESSAGE = config.getString("MESSAGES.api_key_invalid", "&cInvalid API key. Please check your configuration.");

        // Permissions
        TRANSLATE_PERMISSION = config.getString("PERMISSIONS.translate", "translatorx.translate");
        TRANSLATE_ITEMS_PERMISSION = config.getString("PERMISSIONS.translate_items", "translatorx.translate.items");
        ADMIN_PERMISSION = config.getString("PERMISSIONS.admin", "translatorx.admin");
        BYPASS_RATE_LIMIT_PERMISSION = config.getString("PERMISSIONS.bypass_rate_limit", "translatorx.bypass.ratelimit");
    }
}
