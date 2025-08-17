package xyz.aizsargs.translate.translate.listener;

import lombok.extern.java.Log;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.aizsargs.translate.TranslateX;
import xyz.aizsargs.translate.data.Config;
import xyz.aizsargs.translate.translate.TranslationManager;
import xyz.aizsargs.translate.translate.TranslatePlayer;

import java.util.UUID;

/**
 * Handles chat events and applies translations with hover functionality
 */
@Log
public class PlayerChatEventListener implements Listener {
    
    private final TranslationManager translationManager;
    
    public PlayerChatEventListener() {
        this.translationManager = TranslationManager.getInstance();
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!Config.CHAT_TRANSLATION_ENABLED) {
            return;
        }
        
        Player sender = event.getPlayer();
        String message = event.getMessage();
        
        // Skip if message is too long
        if (message.length() > Config.MAX_MESSAGE_LENGTH) {
            return;
        }
        
        // Get sender's language
        TranslatePlayer translatePlayer = TranslateX.getInstance().getTranslatorController().getTransLatePlayer(sender);
        if (translatePlayer == null) {
            return;
        }
        
        String senderLanguage = translatePlayer.getLanguageCode();
        
        // Process message for each recipient
        for (Player recipient : event.getRecipients()) {
            processMessageForRecipient(sender, recipient, message, senderLanguage, event);
        }
    }
    
    private void processMessageForRecipient(Player sender, Player recipient, String message, 
                                         String senderLanguage, AsyncPlayerChatEvent event) {
        // Get recipient's preferred language
        TranslatePlayer recipientTranslatePlayer = TranslateX.getInstance().getTranslatorController().getTransLatePlayer(recipient);
        if (recipientTranslatePlayer == null) {
            return;
        }
        
        String recipientLanguage = recipientTranslatePlayer.getLanguageCode();
        
        // If languages are the same, no translation needed
        if (senderLanguage.equalsIgnoreCase(recipientLanguage)) {
            return;
        }
        
        // Cancel the original event for this recipient
        event.getRecipients().remove(recipient);
        
        // Create hoverable message component
        BaseComponent[] messageComponent = translationManager.createHoverableMessage(
            message, recipientLanguage, sender
        );
        
        // Send the hoverable message to the recipient
        recipient.spigot().sendMessage(messageComponent);
        
        // If we should show the original message alongside translation
        if (Config.SHOW_ORIGINAL_MESSAGE) {
            // Send original message in small text below
            String originalIndicator = "§7§o[Original: " + message + "]";
            recipient.sendMessage(originalIndicator);
        }
    }
    
    /**
     * Handle translation completion callback
     */
    private void onTranslationComplete(Player recipient, String originalMessage, 
                                     String translatedMessage, String sourceLanguage, 
                                     String targetLanguage) {
        // Create a beautiful translation display
        String translationDisplay = createTranslationDisplay(
            originalMessage, translatedMessage, sourceLanguage, targetLanguage
        );
        
        recipient.sendMessage(translationDisplay);
    }
    
    /**
     * Create a beautiful translation display message
     */
    private String createTranslationDisplay(String original, String translated, 
                                         String sourceLang, String targetLang) {
        StringBuilder display = new StringBuilder();
        display.append("§6§l🌐 Translation\n");
        display.append("§7Original (").append(sourceLang).append("): §f").append(original).append("\n");
        display.append("§7Translated (").append(targetLang).append("): §a").append(translated);
        
        return display.toString();
    }
}
