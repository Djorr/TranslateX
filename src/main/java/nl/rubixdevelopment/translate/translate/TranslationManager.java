package nl.rubixdevelopment.translate.translate;

import lombok.Getter;
import lombok.extern.java.Log;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;
import nl.rubixdevelopment.translate.TranslateX;
import nl.rubixdevelopment.translate.data.Config;
import nl.rubixdevelopment.translate.translate.provider.TranslationProvider;
import nl.rubixdevelopment.translate.translate.provider.OpenAITranslationProvider;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Manages all translation operations with hover functionality
 */
@Log
public class TranslationManager {
    
    @Getter
    private static TranslationManager instance;
    
    private final List<TranslationProvider> providers;
    private final Map<String, TranslationResult> translationCache;
    private final ExecutorService executorService;
    private final Map<UUID, String> playerLanguages;
    
    public TranslationManager() {
        instance = this;
        this.providers = new ArrayList<>();
        this.translationCache = new ConcurrentHashMap<>();
        this.executorService = Executors.newFixedThreadPool(Config.THREAD_POOL_SIZE);
        this.playerLanguages = new ConcurrentHashMap<>();
        
        initializeProviders();
    }
    
    private void initializeProviders() {
        // Initialize OpenAI provider
        if (Config.OPENAI_ENABLED) {
            providers.add(new OpenAITranslationProvider());
        }
        
        // Sort by priority
        providers.sort(Comparator.comparingInt(TranslationProvider::getPriority));
        
        log.info("Initialized " + providers.size() + " translation providers");
    }
    
    /**
     * Set a player's preferred language
     */
    public void setPlayerLanguage(UUID playerId, String languageCode) {
        playerLanguages.put(playerId, languageCode);
    }
    
    /**
     * Get a player's preferred language
     */
    public String getPlayerLanguage(UUID playerId) {
        return playerLanguages.getOrDefault(playerId, Config.DEFAULT_LANGUAGE);
    }
    
    /**
     * Create a hoverable message component
     */
    public BaseComponent[] createHoverableMessage(String originalText, String targetLanguage, Player sender) {
        String playerLanguage = getPlayerLanguage(sender.getUniqueId());
        
        // If player's language is the same as target, no translation needed
        if (playerLanguage.equalsIgnoreCase(targetLanguage)) {
            return new ComponentBuilder(originalText).create();
        }
        
        // Create the main text component (always shows original)
        TextComponent mainText = new TextComponent(originalText);
        
        // Create hover text showing translation
        String hoverText = createHoverText(originalText, playerLanguage, targetLanguage);
        mainText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
                new ComponentBuilder(hoverText).create()));
        
        return new BaseComponent[]{mainText};
    }
    
    /**
     * Create hover text with translation information
     */
    private String createHoverText(String originalText, String sourceLanguage, String targetLanguage) {
        StringBuilder hover = new StringBuilder();
        hover.append("§6§l🌐 Translation Info\n");
        hover.append("§7Original: §f").append(originalText).append("\n");
        hover.append("§7From: §e").append(getLanguageDisplayName(sourceLanguage)).append("\n");
        hover.append("§7To: §a").append(getLanguageDisplayName(targetLanguage)).append("\n");
        hover.append("§7§oHover to see translation");
        
        return hover.toString();
    }
    
    /**
     * Get language display name with flag
     */
    private String getLanguageDisplayName(String languageCode) {
        String flag = getLanguageFlag(languageCode);
        String name = getLanguageName(languageCode);
        return flag + " " + name + " (" + languageCode + ")";
    }
    
    /**
     * Get language flag emoji
     */
    private String getLanguageFlag(String languageCode) {
        switch (languageCode.toUpperCase()) {
            case "EN": return "🇺🇸";
            case "DE": return "🇩🇪";
            case "FR": return "🇫🇷";
            case "ES": return "🇪🇸";
            case "IT": return "🇮🇹";
            case "NL": return "🇳🇱";
            case "PL": return "🇵🇱";
            case "PT": return "🇵🇹";
            case "RU": return "🇷🇺";
            case "JA": return "🇯🇵";
            case "ZH": return "🇨🇳";
            case "KO": return "🇰🇷";
            case "AR": return "🇸🇦";
            case "HI": return "🇮🇳";
            case "TR": return "🇹🇷";
            case "SV": return "🇸🇪";
            case "DA": return "🇩🇰";
            case "NO": return "🇳🇴";
            case "FI": return "🇫🇮";
            case "CS": return "🇨🇿";
            case "HU": return "🇭🇺";
            case "RO": return "🇷🇴";
            case "BG": return "🇧🇬";
            case "HR": return "🇭🇷";
            case "SK": return "🇸🇰";
            case "SL": return "🇸🇮";
            case "ET": return "🇪🇪";
            case "LV": return "🇱🇻";
            case "LT": return "🇱🇹";
            case "MT": return "🇲🇹";
            case "EL": return "🇬🇷";
            case "GA": return "🇮🇪";
            case "CY": return "🇨🇾";
            default: return "🌐";
        }
    }
    
    /**
     * Get language name
     */
    private String getLanguageName(String languageCode) {
        switch (languageCode.toUpperCase()) {
            case "EN": return "English";
            case "DE": return "German";
            case "FR": return "French";
            case "ES": return "Spanish";
            case "IT": return "Italian";
            case "NL": return "Dutch";
            case "PL": return "Polish";
            case "PT": return "Portuguese";
            case "RU": return "Russian";
            case "JA": return "Japanese";
            case "ZH": return "Chinese";
            case "KO": return "Korean";
            case "AR": return "Arabic";
            case "HI": return "Hindi";
            case "TR": return "Turkish";
            case "SV": return "Swedish";
            case "DA": return "Danish";
            case "NO": return "Norwegian";
            case "FI": return "Finnish";
            case "CS": return "Czech";
            case "HU": return "Hungarian";
            case "RO": return "Romanian";
            case "BG": return "Bulgarian";
            case "HR": return "Croatian";
            case "SK": return "Slovak";
            case "SL": return "Slovenian";
            case "ET": return "Estonian";
            case "LV": return "Latvian";
            case "LT": return "Lithuanian";
            case "MT": return "Maltese";
            case "EL": return "Greek";
            case "GA": return "Irish";
            case "CY": return "Welsh";
            default: return languageCode;
        }
    }
    
    /**
     * Translate text asynchronously
     */
    public void translateAsync(String text, String targetLanguage, Player sender, TranslationCallback callback) {
        executorService.submit(() -> {
            try {
                TranslationResult result = translate(text, targetLanguage);
                callback.onTranslationComplete(result);
            } catch (Exception e) {
                log.warning("Async translation failed: " + e.getMessage());
                callback.onTranslationFailed(e);
            }
        });
    }
    
    /**
     * Translate text synchronously
     */
    public TranslationResult translate(String text, String targetLanguage) {
        // Check cache first
        String cacheKey = text + ":" + targetLanguage;
        if (translationCache.containsKey(cacheKey)) {
            return translationCache.get(cacheKey);
        }
        
        // Try each provider in order
        for (TranslationProvider provider : providers) {
            if (provider.isAvailable()) {
                try {
                    TranslationResult result = provider.translate(text, targetLanguage);
                    if (result.isSuccess()) {
                        // Cache the result
                        translationCache.put(cacheKey, result);
                        return result;
                    }
                } catch (Exception e) {
                    log.warning("Provider " + provider.getName() + " failed: " + e.getMessage());
                }
            }
        }
        
        // All providers failed
        return TranslationResult.failure(text, "auto", targetLanguage, "None", 
                "All translation providers failed", 0);
    }
    
    /**
     * Clear translation cache
     */
    public void clearCache() {
        translationCache.clear();
    }
    
    /**
     * Shutdown the translation manager
     */
    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
    
    /**
     * Callback interface for async translations
     */
    public interface TranslationCallback {
        void onTranslationComplete(TranslationResult result);
        void onTranslationFailed(Exception e);
    }
}
