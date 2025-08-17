package xyz.aizsargs.translate.translate.provider;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.java.Log;
import xyz.aizsargs.translate.data.Config;
import xyz.aizsargs.translate.translate.TranslationResult;

import java.util.Arrays;
import java.util.List;

/**
 * OpenAI translation provider implementation
 */
@Log
public class OpenAITranslationProvider implements TranslationProvider {
    
    private OpenAiService openAiService;
    private final List<String> apiKeys;
    private final String model;
    private final int maxTokens;
    private final double temperature;
    
    public OpenAITranslationProvider() {
        this.apiKeys = Config.OPENAI_API_KEYS;
        this.model = Config.OPENAI_MODEL;
        this.maxTokens = Config.OPENAI_MAX_TOKENS;
        this.temperature = Config.OPENAI_TEMPERATURE;
        initializeService();
    }
    
    private void initializeService() {
        if (apiKeys == null || apiKeys.isEmpty()) {
            log.warning("No OpenAI API keys configured");
            return;
        }
        
        for (String apiKey : apiKeys) {
            if (apiKey.equals("<YOUR OPENAI API KEY>") || apiKey.trim().isEmpty()) {
                continue;
            }
            
            try {
                this.openAiService = new OpenAiService(apiKey);
                log.info("OpenAI service initialized successfully with API key: " + apiKey.substring(0, 8) + "...");
                return;
            } catch (Exception e) {
                log.warning("Failed to initialize OpenAI service with API key: " + apiKey.substring(0, 8) + "... Error: " + e.getMessage());
            }
        }
        
        log.severe("Failed to initialize OpenAI service with any API key");
    }
    
    @Override
    public String getName() {
        return "OpenAI";
    }
    
    @Override
    public boolean isAvailable() {
        return openAiService != null;
    }
    
    @Override
    public TranslationResult translate(String text, String targetLanguage) {
        return translate(text, null, targetLanguage);
    }
    
    @Override
    public TranslationResult translate(String text, String sourceLanguage, String targetLanguage) {
        if (!isAvailable()) {
            return TranslationResult.failure(text, sourceLanguage, targetLanguage, getName(), 
                    "OpenAI service not available", 0);
        }
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Build the prompt for translation
            String prompt = buildTranslationPrompt(text, sourceLanguage, targetLanguage);
            
            // Create chat completion request
            List<ChatMessage> messages = Arrays.asList(new ChatMessage("user", prompt));
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model(model)
                    .messages(messages)
                    .maxTokens(maxTokens)
                    .temperature(temperature)
                    .build();
            
            // Get response
            String response = openAiService.createChatCompletion(request)
                    .getChoices().get(0).getMessage().getContent();
            
            long duration = System.currentTimeMillis() - startTime;
            
            return TranslationResult.success(
                text,
                response.trim(),
                sourceLanguage != null ? sourceLanguage : "auto",
                targetLanguage,
                getName(),
                0.90, // OpenAI provides good quality translations
                duration
            );
            
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.warning("OpenAI translation failed: " + e.getMessage());
            
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
        return new String[]{
            "EN", "DE", "FR", "ES", "IT", "NL", "PL", "PT", "RU", "JA", "ZH", "KO",
            "AR", "HI", "TR", "SV", "DA", "NO", "FI", "CS", "HU", "RO", "BG", "HR",
            "SK", "SL", "ET", "LV", "LT", "MT", "EL", "GA", "CY"
        };
    }
    
    @Override
    public int getPriority() {
        return 2; // Medium priority
    }
    
    /**
     * Build the translation prompt for OpenAI
     */
    private String buildTranslationPrompt(String text, String sourceLanguage, String targetLanguage) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a professional translator. Translate the following text");
        
        if (sourceLanguage != null) {
            prompt.append(" from ").append(getLanguageName(sourceLanguage));
        }
        
        prompt.append(" to ").append(getLanguageName(targetLanguage));
        prompt.append(". Only provide the translated text, nothing else.\n\n");
        prompt.append("Text to translate:\n");
        prompt.append(text);
        
        return prompt.toString();
    }
    
    /**
     * Get language name from code
     */
    private String getLanguageName(String languageCode) {
        if (languageCode == null) return "its detected language";
        
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
}
