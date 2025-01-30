package xyz.aizsargs.translate.translate.listener;

import com.deepl.api.TextResult;
import xyz.aizsargs.translate.translate.TranslateController;
import xyz.aizsargs.translate.translate.TranslatePlayer;
import xyz.aizsargs.translate.util.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlayerChatEventListener implements Listener {

    private final Map<String, TextResult> textResults;
    private final TranslateController translateController;

    public PlayerChatEventListener() {
        this.textResults = new HashMap<>();
        this.translateController = TranslateController.getInstance();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        final Player sender = event.getPlayer();
        final String messageBySender = event.getMessage();

        final Set<Player> recipients = event.getRecipients();

        for (Player recipient : recipients) {
            if (sender != null && sender.getUniqueId().equals(recipient.getUniqueId())) continue; // skip sending message to sender

            final TranslatePlayer translatePlayer = this.translateController.getTransLatePlayer(recipient);
            if (translatePlayer == null) continue;

            final String languageCode = translatePlayer.getLanguageCode();
            TextResult textResult = textResults.get(languageCode);

            if (textResult == null) {
                textResult = this.translateController.transLateMessage(messageBySender, languageCode);
                if (textResult == null) continue;

                textResults.put(languageCode, textResult);
            }

            final String transLatedMessage = textResult.getText();
            recipient.sendMessage(ColorUtil.translate("<message>"
                    .replace("<message>", transLatedMessage)));
        }
    }
}
