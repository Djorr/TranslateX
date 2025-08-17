package nl.rubixdevelopment.translate.translate;

import lombok.Data;
import lombok.Builder;

/**
 * Result of a translation operation
 */
@Data
@Builder
public class TranslationResult {
    private final String translatedText;
    private final String originalText;
    private final String sourceLanguage;
    private final String targetLanguage;
    private final String provider;
    private final boolean success;
    private final String errorMessage;
    private final long duration;

    public static TranslationResult success(String originalText, String translatedText,
                                         String sourceLanguage, String targetLanguage,
                                         String provider, long duration) {
        return TranslationResult.builder()
                .translatedText(translatedText)
                .originalText(originalText)
                .sourceLanguage(sourceLanguage)
                .targetLanguage(targetLanguage)
                .provider(provider)
                .success(true)
                .duration(duration)
                .build();
    }

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
