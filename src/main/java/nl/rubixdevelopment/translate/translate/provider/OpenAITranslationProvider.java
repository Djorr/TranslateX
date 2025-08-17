package nl.rubixdevelopment.translate.translate.provider;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import nl.rubixdevelopment.translate.data.Config;
import nl.rubixdevelopment.translate.translate.TranslationResult;

import java.util.Arrays;
import java.util.List;

/**
 * OpenAI-based translation provider
 */
public class OpenAITranslationProvider implements TranslationProvider {
    
    private final OpenAiService openAiService;
    
    public OpenAITranslationProvider() {
        String apiKey = Config.OPENAI_API_KEYS.isEmpty() ? "" : Config.OPENAI_API_KEYS.get(0);
        this.openAiService = new OpenAiService(apiKey);
    }
    
    @Override
    public String getName() {
        return "OpenAI";
    }
    
    @Override
    public boolean isAvailable() {
        return !Config.OPENAI_API_KEYS.isEmpty() && 
               !Config.OPENAI_API_KEYS.get(0).equals("<YOUR_OPENAI_API_KEY>");
    }
    
    @Override
    public TranslationResult translate(String text, String targetLanguage) {
        return translate(text, "auto", targetLanguage);
    }
    
    @Override
    public TranslationResult translate(String text, String sourceLanguage, String targetLanguage) {
        long startTime = System.currentTimeMillis();
        
        try {
            String prompt = buildTranslationPrompt(text, sourceLanguage, targetLanguage);
            
            ChatMessage message = new ChatMessage("user", prompt);
            List<ChatMessage> messages = Arrays.asList(message);
            
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model(Config.OPENAI_MODEL)
                    .messages(messages)
                    .maxTokens(Config.OPENAI_MAX_TOKENS)
                    .temperature(Config.OPENAI_TEMPERATURE)
                    .build();
            
            String response = openAiService.createChatCompletion(request)
                    .getChoices().get(0).getMessage().getContent();
            
            long duration = System.currentTimeMillis() - startTime;
            
            return TranslationResult.success(text, response.trim(), sourceLanguage, targetLanguage, getName(), duration);
            
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            return TranslationResult.failure(text, sourceLanguage, targetLanguage, getName(), e.getMessage(), duration);
        }
    }
    
    @Override
    public String[] getSupportedLanguages() {
        return new String[]{"EN", "DE", "FR", "ES", "IT", "NL", "PL", "PT", "RU", "JA", "ZH", "KO", "AR", "HI", "TR", "SV", "DA", "NO", "FI", "CS", "HU", "RO", "BG", "HR", "SK", "SL", "ET", "LV", "LT", "MT", "EL", "GA", "CY"};
    }
    
    @Override
    public int getPriority() {
        return 1;
    }
    
    private String buildTranslationPrompt(String text, String sourceLanguage, String targetLanguage) {
        String sourceLangName = getLanguageName(sourceLanguage);
        String targetLangName = getLanguageName(targetLanguage);
        
        return String.format(
            "Translate the following text from %s to %s. " +
            "Only return the translated text, nothing else. " +
            "Preserve the original meaning and tone:\n\n%s",
            sourceLangName, targetLangName, text
        );
    }
    
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
}
