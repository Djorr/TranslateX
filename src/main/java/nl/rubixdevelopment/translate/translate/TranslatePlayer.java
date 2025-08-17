package nl.rubixdevelopment.translate.translate;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class TranslatePlayer {

    private final UUID playerId;

    private String language;
    private String languageCode;

    public TranslatePlayer(Player player) {
        this.playerId = player.getUniqueId();
    }

    public TranslatePlayer(UUID playerId, String language, String languageCode) {
        this.playerId = playerId;
        this.language = language;
        this.languageCode = languageCode;
    }
}
