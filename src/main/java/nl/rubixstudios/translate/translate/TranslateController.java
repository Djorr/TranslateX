package nl.rubixstudios.translate.translate;

import com.deepl.api.*;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import nl.rubixstudios.translate.TranslateX;
import nl.rubixstudios.translate.data.Config;
import nl.rubixstudios.translate.translate.listener.PlayerChatEventListener;
import nl.rubixstudios.translate.translate.listener.PlayerJoinEventListener;
import nl.rubixstudios.translate.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TranslateController implements Listener {

    @Getter private static TranslateController instance;

    private Translator translator;

    private String currentKey;
    private final List<String> authKeysPool;

    private final List<TranslatePlayer> translatePlayers;

    public TranslateController() {
        instance = this;

        this.translatePlayers = new ArrayList<>();
        this.loadData();

        this.authKeysPool = new ArrayList<>();
        this.authKeysPool.addAll(Config.API_KEYS);

        Bukkit.getPluginManager().registerEvents(this, TranslateX.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerChatEventListener(), TranslateX.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerJoinEventListener(), TranslateX.getInstance());
    }

    public void disable() {
        this.saveData();
    }

    @SneakyThrows
    private void loadData() {
        FileConfiguration config = TranslateX.getInstance().getConfig();
        if (config == null) {
            TranslateX.getInstance().saveDefaultConfig();
            config = TranslateX.getInstance().getConfig();
        }

        ConfigurationSection section = config.getConfigurationSection("PLAYER_DATA");
        if (section == null) section = config.createSection("PLAYER_DATA");
        if (section.getKeys(false) == null) return;

        for (String key : section.getKeys(false)) {
            final ConfigurationSection playerSection = section.getConfigurationSection(key);
            if (playerSection == null) continue;

            final String language = playerSection.getString("LANGUAGE");
            final String languageCode = playerSection.getString("LANGUAGE_CODE");
            final String playerId = playerSection.getString("PLAYER_ID");

            if (language == null || languageCode == null || playerId == null) continue;

            final TranslatePlayer translatePlayer = new TranslatePlayer(UUID.fromString(playerId), language, languageCode);
            this.translatePlayers.add(translatePlayer);
        }

        TranslateX.getInstance().log("- &aLoaded " + this.translatePlayers.size() + " player data");
    }

    private void saveData() {
        FileConfiguration config = TranslateX.getInstance().getConfig();
        if (config == null) {
            TranslateX.getInstance().saveDefaultConfig();
            config = TranslateX.getInstance().getConfig();
        }

        ConfigurationSection section = config.getConfigurationSection("PLAYER_DATA");
        if (section == null) section = config.createSection("PLAYER_DATA");

        for (TranslatePlayer translatePlayer : this.translatePlayers) {
            final ConfigurationSection playerSection = section.createSection(translatePlayer.getPlayerId().toString());
            playerSection.set("LANGUAGE", translatePlayer.getLanguage());
            playerSection.set("LANGUAGE_CODE", translatePlayer.getLanguageCode());
            playerSection.set("PLAYER_ID", translatePlayer.getPlayerId().toString());
        }

        TranslateX.getInstance().saveConfig();

        TranslateX.getInstance().log("- &aSaved " + this.translatePlayers.size() + " player data");
    }

    public void intializeTranslator() {
        if (this.authKeysPool.isEmpty()) {
            TranslateX.getInstance().log("&cNo auth keys found in config.yml! Please add some auth keys to the config.yml file.");
            return;
        }

        for (String authKey : this.authKeysPool) {
            if (hasReachedUsageLimit(authKey)) {
                this.authKeysPool.set(this.authKeysPool.indexOf(authKey), authKey + "[LIMIT_REACHED]");
                continue;
            }
            this.currentKey = authKey;
            this.translator = new Translator(authKey);
            break;
        }
    }

    @SneakyThrows
    private boolean hasReachedUsageLimit(String authKey) {
        if (authKey == null) return true;
        if (authKey.contains("[LIMIT_REACHED]")) return true;

        final Usage usage = new Translator(authKey).getUsage();
        if (usage.anyLimitReached()) {
            System.out.println("Reached usage limit for auth key: " + authKey);
            return true;
        }
        return false;
    }

    public TextResult transLateMessage(String content, String language) {
        TextResult result;

        try {
            if (this.hasReachedUsageLimit(this.currentKey)) {
                this.intializeTranslator();
                result = this.translator.translateText(content, null, language);
            } else {
                result = this.translator.translateText(content, null, language);
            }
        } catch (DeepLException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    // Data

    public TranslatePlayer getTransLatePlayer(Player player) {
        return this.translatePlayers.stream().filter(translatePlayer -> translatePlayer.getPlayerId().equals(player.getUniqueId())).findFirst().orElse(null);
    }

    public void setPlayerLanguage(Player player, String languageInput) {
        TranslatePlayer translatePlayer = this.getTransLatePlayer(player);
        final Language language = this.getLanguageByInput(languageInput);
        if (translatePlayer != null) {
            if (language != null) {
                translatePlayer.setLanguage(language.getName());
                translatePlayer.setLanguageCode(language.getCode());
            }
        } else {
            if (language != null) {
                translatePlayer = new TranslatePlayer(player);
                translatePlayer.setLanguage(language.getName());
                translatePlayer.setLanguageCode(language.getCode());
                this.translatePlayers.add(translatePlayer);
            }
        }

        final String originalMessage = "&fYour language has been set to: &a" + language.getName() + " &f(" + language.getCode() + ")";
        final String translatedMessage = this.transLateMessage(originalMessage, language.getCode()).getText();
        player.sendMessage(ColorUtil.translate("&6&lTranslateX &8» &f") + ColorUtil.translate(translatedMessage));
    }

    // Translate Messages

    public void sendTranslatedMessage(Player player, String targetLang, String contentToTranslate) {
        final TextResult result = this.transLateMessage(contentToTranslate, targetLang);

        final Language sourceLanguage = this.getLanguageByCode(result.getDetectedSourceLanguage());
        final Language targetLanguage = this.getLanguageByCode(targetLang);

        final String url = this.getTranslatedPastebinLink(this.getPreparedContent(sourceLanguage, targetLanguage, result.getText()));

        final TextComponent startMessage = new TextComponent(ColorUtil.translate(
                "&6&lTranslateX &8» &fYour message has been translated.\n"));

        final TextComponent endMessage = new TextComponent(ColorUtil.translate("&6&lTranslateX &8» &aClick here to view the translation."));
        endMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Click to open the link (send to pastebin)").create()));
        endMessage.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));

        player.spigot().sendMessage(startMessage, endMessage);
    }

    private String getPreparedContent(@Nullable Language source, @Nullable Language target, String content) {
        final List<String> preparedContent = new ArrayList<>();

        preparedContent.add("                                                                                                    ");
        preparedContent.add(" /$$$$$$$$                                     /$$             /$$               /$$   /$$");
        preparedContent.add("|__  $$__/                                    | $$            | $$              | $$  / $$");
        preparedContent.add("   | $$  /$$$$$$  /$$$$$$  /$$$$$$$   /$$$$$$$| $$  /$$$$$$  /$$$$$$    /$$$$$$ |  $$/$$/");
        preparedContent.add("   | $$ /$$__  $$|____  $$| $$__  $$ /$$_____/| $$ |____  $$|_  $$_/   /$$__  $$ \\  $$$$/");
        preparedContent.add("   | $$| $$  \\__/ /$$$$$$$| $$  \\ $$|  $$$$$$ | $$  /$$$$$$$  | $$    | $$$$$$$$  >$$  $$ ");
        preparedContent.add("   | $$| $$      /$$__  $$| $$  | $$ \\____  $$| $$ /$$__  $$  | $$ /$$| $$_____/ /$$/\\ $$");
        preparedContent.add("   | $$| $$     |  $$$$$$$| $$  | $$ /$$$$$$$/| $$|  $$$$$$$  |  $$$$/|  $$$$$$$| $$  \\ $$");
        preparedContent.add("   |__/|__/      \\_______/|__/  |__/|_______/ |__/ \\_______/   \\___/   \\_______/|__/  |__/");
        preparedContent.add("                                                                                                    ");
        preparedContent.add("Plugin author: Djorr (RubixDevelopment)                                                             ");
        preparedContent.add("Plugin version: 1.0.0                                                                                ");
        preparedContent.add("Discord: https://discord.rubixdevelopment.nl/                                                        ");
        preparedContent.add("                                                                                                    ");
        if (source != null) {
            preparedContent.add("Translated from language: " + source.getName() + " (" + source.getCode() + ")");
        }
        if (target != null) {
            preparedContent.add("Translated to language: " + target.getName() + " (" + target.getCode() + ")");
        }
        preparedContent.add("                                                                                                    ");
        preparedContent.add("Message:                                                                                             ");
        preparedContent.add(content);

        return String.join("\n", preparedContent);
    }

    public Language getLanguageByInput(String code) {
        Language language = this.getLanguageByCode(code);
        if (language == null) {
            language = this.getLanguageByName(code);
        }
        return language;
    }

    @SneakyThrows
    private Language getLanguageByCode(String code) {
        for (Language language : this.translator.getLanguages(LanguageType.Target)) {
            if (language.getCode().equalsIgnoreCase(code)) {
                return language;
            }
        }
        return null;
    }

    @SneakyThrows
    private Language getLanguageByName(String name) {
        for (Language language : this.translator.getLanguages(LanguageType.Target)) {
            if (language.getName().equalsIgnoreCase(name)) {
                return language;
            }
        }
        return null;
    }

    private String getTranslatedPastebinLink(String pasteCode) {
        String responseLink = null;

        try {
            // Set the API endpoint URL
            URL url = new URL("https://pastebin.com/api/api_post.php");

            // Create a new HTTP connection to the API endpoint
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Enable output for the connection
            connection.setDoOutput(true);

            // Set the required parameters
            String apiKey = "Cus1Ccr9Afc29RNWp8d3-oTlyvGeubrh";
            String option = "paste";

            // Create the POST data string with the required parameters
            String postData = "api_dev_key=" + URLEncoder.encode(apiKey, "UTF-8") +
                    "&api_option=" + URLEncoder.encode(option, "UTF-8") +
                    "&api_paste_code=" + URLEncoder.encode(pasteCode, "UTF-8") +
                    "&api_paste_private=" + URLEncoder.encode("1", "UTF-8") +
                    "&api_paste_expire_date=" + URLEncoder.encode("1D", "UTF-8") +
                    "&api_paste_format=" + URLEncoder.encode("text", "UTF-8") +
                    "&api_user_key=" + URLEncoder.encode("", "UTF-8") +
                    "&api_paste_name=" + URLEncoder.encode("TranslatorL", "UTF-8") +
                    "&api_results_limit=" + URLEncoder.encode("1", "UTF-8");

            // Write the POST data to the connection output stream
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
            writer.write(postData);
            writer.flush();

            // Read the response from the API endpoint
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Clean up
            writer.close();
            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseLink;
    }
}
