package nl.rubixdevelopment.translate.translate.listener;

import lombok.extern.java.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import nl.rubixdevelopment.translate.TranslateX;
import nl.rubixdevelopment.translate.data.Config;
import nl.rubixdevelopment.translate.translate.TranslatePlayer;
import nl.rubixdevelopment.translate.util.ColorUtil;

/**
 * Handles player join events for translation setup
 */
@Log
public class PlayerJoinEventListener implements Listener {
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Check if player has a saved language preference
        TranslatePlayer translatePlayer = TranslateX.getInstance().getTranslatorController().getTransLatePlayer(player);
        
        if (translatePlayer == null) {
            // Create new translate player with default language
            translatePlayer = new TranslatePlayer(player);
            TranslateX.getInstance().getTranslatorController().getTranslatePlayers().add(translatePlayer);
            
            // Send welcome message
            player.sendMessage(ColorUtil.translate(Config.MESSAGE_PREFIX + "&aWelcome! Use /translator to set your language preference."));
            log.info("Created new TranslatePlayer for " + player.getName());
        } else {
            // Player already exists, send language info
            String currentLanguage = translatePlayer.getLanguageCode();
            String languageName = translatePlayer.getLanguage();
            
            player.sendMessage(ColorUtil.translate(Config.MESSAGE_PREFIX + 
                "&aWelcome back! Your language is set to: &e" + languageName + " (" + currentLanguage + ")"));
            
            log.info("Loaded existing TranslatePlayer for " + player.getName() + " with language: " + currentLanguage);
        }
    }
}
