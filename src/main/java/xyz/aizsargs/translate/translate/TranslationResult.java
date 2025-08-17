package xyz.aizsargs.translate.translate;

import lombok.Data;
import lombok.Builder;

/**
 * Result of a translation operation
 */
@Data
@Builder
public class TranslationResult {
    
    /**
     * The translated text
     */
    private final String translatedText;
    
    /**
     * The original text
     */
    private final String originalText;
    
    /**
     * Source language code
     */
    private final String sourceLanguage;
    
    /**
     * Target language code
     */
    private final String targetLanguage;
    
    /**
     * Translation provider name
     */
    private final String provider;
    
    /**
     * Whether the translation was successful
     */
    private final boolean success;
    
    /**
     * Error message if translation failed
     */
    private final String errorMessage;
    
    /**
     * Translation quality score (0.0 - 1.0)
     */
    private final double quality;
    
    /**
     * Translation duration in milliseconds
     */
    private final long duration;
    
    /**
     * Create a successful translation result
     */
    public static TranslationResult success(String originalText, String translatedText, 
                                         String sourceLanguage, String targetLanguage, 
                                         String provider, double quality, long duration) {
        return TranslationResult.builder()
                .translatedText(translatedText)
                .originalText(originalText)
                .sourceLanguage(sourceLanguage)
                .targetLanguage(targetLanguage)
                .provider(provider)
                .success(true)
                .quality(quality)
                .duration(duration)
                .build();
    }
    
    /**
     * Create a failed translation result
     */
    public static TranslationResult failure(String originalText, String sourceLanguage, 
                                         String targetLanguage, String provider, 
                                         String errorMessage, long duration) {
        return TranslationResult.builder()
                .originalText(originalText)
                .sourceLanguage(sourceLanguage)
                .targetLanguage(targetLanguage)
                .provider(provider)
                .success(false)
                .errorMessage(errorMessage)
                .duration(duration)
                .build();
    }
}
