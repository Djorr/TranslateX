package nl.rubixdevelopment.translate.translate.provider;

import nl.rubixdevelopment.translate.translate.TranslationResult;

/**
 * Interface for translation providers
 */
public interface TranslationProvider {
    String getName();
    boolean isAvailable();
    TranslationResult translate(String text, String targetLanguage);
    TranslationResult translate(String text, String sourceLanguage, String targetLanguage);
    String[] getSupportedLanguages();
    int getPriority();
}
