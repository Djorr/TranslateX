package nl.rubixstudios.translate.translate.listener;

import com.deepl.api.Language;
import lombok.SneakyThrows;
import net.md_5.bungee.api.chat.*;
import nl.rubixstudios.translate.translate.TranslateController;
import nl.rubixstudios.translate.translate.TranslatePlayer;
import nl.rubixstudios.translate.util.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerJoinEventListener implements Listener {

    private final TranslateController translateController;

    public PlayerJoinEventListener() {
        this.translateController = TranslateController.getInstance();
    }

    @SneakyThrows
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        final Player player = event.getPlayer();
        if (player == null) return;

        final TranslatePlayer translatePlayer = this.translateController.getTransLatePlayer(player);
        if (translatePlayer != null) {
            player.sendMessage(ColorUtil.translate("&7&m----------------------------------------"));

            final String translatedMessage1 = this.translateController.transLateMessage("Your data has been loaded!", translatePlayer.getLanguageCode()).getText();
            player.sendMessage(ColorUtil.translate("&6&lTranslateX &8» &f") + translatedMessage1);

            final String transLatedMessage2 = this.translateController.transLateMessage("Your language is set to " + translatePlayer.getLanguage() + " (" + translatePlayer.getLanguageCode() + ")", translatePlayer.getLanguageCode()).getText();
            player.sendMessage(ColorUtil.translate("&6&lTranslateX &8» &f") + transLatedMessage2);

            player.sendMessage(ColorUtil.translate("&7&m----------------------------------------"));
            return;
        }

        player.sendMessage(ColorUtil.translate("&7&m----------------------------------------"));
        player.sendMessage(ColorUtil.translate("&6&lTranslateX &8» &fPlease choose your language:"));
        player.sendMessage(ColorUtil.translate(""));

        final List<TextComponent> textComponentList = new ArrayList<>();

        for (Language language : this.translateController.getTranslator().getSourceLanguages()) {
            final TextComponent textComponent = new TextComponent(ColorUtil.translate("&e<languageName>&8, ".replace("<languageName>", language.getName())));
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtil.translate("&eClick to set your language to &f" + language.getName())).create()));

            String langCode = language.getCode();
            if (langCode.contains("en")) langCode = "en-US";
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/translator setlang " + langCode));

            textComponentList.add(textComponent);
        }

        player.spigot().sendMessage(textComponentList.toArray(new BaseComponent[textComponentList.size()]));

        player.sendMessage(ColorUtil.translate("&7&m----------------------------------------"));
    }
}
