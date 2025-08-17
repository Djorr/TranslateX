package xyz.aizsargs.translate.translate.provider;

import xyz.aizsargs.translate.translate.TranslationResult;

/**
 * Interface for translation providers
 */
public interface TranslationProvider {
    
    /**
     * Get the name of this provider
     * @return provider name
     */
    String getName();
    
    /**
     * Check if this provider is available
     * @return true if available
     */
    boolean isAvailable();
    
    /**
     * Translate text to target language
     * @param text text to translate
     * @param targetLanguage target language code
     * @return translation result
     */
    TranslationResult translate(String text, String targetLanguage);
    
    /**
     * Translate text from source language to target language
     * @param text text to translate
     * @param sourceLanguage source language code
     * @param targetLanguage target language code
     * @return translation result
     */
    TranslationResult translate(String text, String sourceLanguage, String targetLanguage);
    
    /**
     * Get supported languages
     * @return array of supported language codes
     */
    String[] getSupportedLanguages();
    
    /**
     * Get provider priority (lower number = higher priority)
     * @return priority number
     */
    int getPriority();
}
