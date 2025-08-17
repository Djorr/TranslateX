package xyz.aizsargs.translate.translate.provider;

import com.deepl.api.*;
import lombok.extern.java.Log;
import xyz.aizsargs.translate.TranslateX;
import xyz.aizsargs.translate.data.Config;
import xyz.aizsargs.translate.translate.TranslationResult;

import java.util.List;

/**
 * DeepL translation provider implementation
 */
@Log
public class DeepLTranslationProvider implements TranslationProvider {
    
    private Translator translator;
    private final List<String> apiKeys;
    private final String formality;
    private final boolean preserveFormatting;
    
    public DeepLTranslationProvider() {
        this.apiKeys = Config.DEEPL_API_KEYS;
        this.formality = Config.DEEPL_FORMALITY;
        this.preserveFormatting = Config.DEEPL_PRESERVE_FORMATTING;
        initializeTranslator();
    }
    
    private void initializeTranslator() {
        if (apiKeys == null || apiKeys.isEmpty()) {
            log.warning("No DeepL API keys configured");
            return;
        }
        
        for (String apiKey : apiKeys) {
            if (apiKey.equals("<YOUR DEEPL API KEY>") || apiKey.trim().isEmpty()) {
                continue;
            }
            
            try {
                this.translator = new Translator(apiKey);
                // Test the connection
                this.translator.getUsage();
                log.info("DeepL translator initialized successfully with API key: " + apiKey.substring(0, 8) + "...");
                return;
            } catch (Exception e) {
                log.warning("Failed to initialize DeepL translator with API key: " + apiKey.substring(0, 8) + "... Error: " + e.getMessage());
            }
        }
        
        log.severe("Failed to initialize DeepL translator with any API key");
    }
    
    @Override
    public String getName() {
        return "DeepL";
    }
    
    @Override
    public boolean isAvailable() {
        return translator != null;
    }
    
    @Override
    public TranslationResult translate(String text, String targetLanguage) {
        return translate(text, null, targetLanguage);
    }
    
    @Override
    public TranslationResult translate(String text, String sourceLanguage, String targetLanguage) {
        if (!isAvailable()) {
            return TranslationResult.failure(text, sourceLanguage, targetLanguage, getName(), 
                    "DeepL translator not available", 0);
        }
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Convert language codes to DeepL format
            String deeplTargetLang = convertToDeepLLanguage(targetLanguage);
            String deeplSourceLang = sourceLanguage != null ? convertToDeepLLanguage(sourceLanguage) : null;
            
            // Perform translation with options
            TextResult result;
            if (deeplSourceLang != null) {
                result = translator.translateText(text, deeplSourceLang, deeplTargetLang);
            } else {
                result = translator.translateText(text, null, deeplTargetLang);
            }
            
            long duration = System.currentTimeMillis() - startTime;
            
            return TranslationResult.success(
                text,
                result.getText(),
                result.getDetectedSourceLanguage(),
                targetLanguage,
                getName(),
                0.95, // DeepL typically provides high quality translations
                duration
            );
            
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.warning("DeepL translation failed: " + e.getMessage());
            
            return TranslationResult.failure(
                text,
                sourceLanguage,
                targetLanguage,
                getName(),
                e.getMessage(),
                duration
            );
        }
    }
    
    @Override
    public String[] getSupportedLanguages() {
        try {
            if (translator == null) return new String[0];
            
            List<Language> languages = translator.getLanguages(LanguageType.Target);
            return languages.stream()
                    .map(Language::getCode)
                    .toArray(String[]::new);
        } catch (Exception e) {
            log.warning("Failed to get DeepL supported languages: " + e.getMessage());
            return new String[0];
        }
    }
    
    @Override
    public int getPriority() {
        return 1; // High priority
    }
    
    /**
     * Convert language code to DeepL format
     */
    private String convertToDeepLLanguage(String languageCode) {
        if (languageCode == null) return null;
        
        // DeepL uses uppercase language codes
        String upperCode = languageCode.toUpperCase();
        
        // Handle special cases
        switch (upperCode) {
            case "EN":
            case "ENGLISH":
                return "EN";
            case "DE":
            case "GERMAN":
                return "DE";
            case "FR":
            case "FRENCH":
                return "FR";
            case "ES":
            case "SPANISH":
                return "ES";
            case "IT":
            case "ITALIAN":
                return "IT";
            case "NL":
            case "DUTCH":
                return "NL";
            case "PL":
            case "POLISH":
                return "PL";
            case "PT":
            case "PORTUGUESE":
                return "PT";
            case "RU":
            case "RUSSIAN":
                return "RU";
            case "JA":
            case "JAPANESE":
                return "JA";
            case "ZH":
            case "CHINESE":
                return "ZH";
            case "KO":
            case "KOREAN":
                return "KO";
            default:
                return upperCode;
        }
    }
}
