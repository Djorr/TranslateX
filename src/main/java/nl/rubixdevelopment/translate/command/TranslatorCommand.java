package nl.rubixdevelopment.translate.command;

import lombok.extern.java.Log;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import nl.rubixdevelopment.translate.TranslateX;
import nl.rubixdevelopment.translate.data.Config;
import nl.rubixdevelopment.translate.translate.TranslatePlayer;
import nl.rubixdevelopment.translate.translate.menu.LanguageSelectionMenu;
import nl.rubixdevelopment.translate.util.ColorUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main command handler for TranslateX
 */
@Log
public class TranslatorCommand implements CommandExecutor, TabCompleter {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtil.translate(Config.MESSAGE_PREFIX + "&cThis command can only be used by players!"));
            return true;
        }
        
        Player player = (Player) sender;
        
        // Check permission
        if (!player.hasPermission(Config.TRANSLATE_PERMISSION)) {
            player.sendMessage(ColorUtil.translate(Config.MESSAGE_PREFIX + Config.NO_PERMISSION));
            return true;
        }
        
        // No arguments - open language menu
        if (args.length == 0) {
            LanguageSelectionMenu menu = new LanguageSelectionMenu(player);
            menu.open();
            return true;
        }
        
        // Handle subcommands
        String subcommand = args[0].toLowerCase();
        
        switch (subcommand) {
            case "language":
            case "lang":
            case "set":
                if (args.length < 2) {
                    player.sendMessage(ColorUtil.translate(Config.MESSAGE_PREFIX + "&cUsage: /translator set <language>"));
                    return true;
                }
                handleSetLanguage(player, args[1]);
                break;
                
            case "menu":
                LanguageSelectionMenu menu = new LanguageSelectionMenu(player);
                menu.open();
                break;
                
            case "info":
                showPluginInfo(player);
                break;
                
            case "reload":
                if (!player.hasPermission(Config.ADMIN_PERMISSION)) {
                    player.sendMessage(ColorUtil.translate(Config.MESSAGE_PREFIX + Config.NO_PERMISSION));
                    return true;
                }
                handleReload(player);
                break;
                
            case "help":
                showHelp(player);
                break;
                
            default:
                player.sendMessage(ColorUtil.translate(Config.MESSAGE_PREFIX + "&cUnknown subcommand. Use /translator help for help."));
                break;
        }
        
        return true;
    }
    
    private void handleSetLanguage(Player player, String languageCode) {
        // Validate language code
        if (!isValidLanguageCode(languageCode)) {
            player.sendMessage(ColorUtil.translate(Config.MESSAGE_PREFIX + Config.INVALID_LANGUAGE));
            return;
        }
        
        // Set the player's language
        TranslatePlayer translatePlayer = TranslateX.getInstance().getTranslatorController().getTransLatePlayer(player);
        if (translatePlayer == null) {
            translatePlayer = new TranslatePlayer(player);
            TranslateX.getInstance().getTranslatorController().getTranslatePlayers().add(translatePlayer);
        }
        
        translatePlayer.setLanguageCode(languageCode.toUpperCase());
        translatePlayer.setLanguage(getLanguageName(languageCode.toUpperCase()));
        
        // Save the data
        TranslateX.getInstance().saveConfig();
        
        // Send confirmation message
        String message = Config.LANGUAGE_SET_MESSAGE
            .replace("{language}", getLanguageName(languageCode.toUpperCase()))
            .replace("{code}", languageCode.toUpperCase());
        
        player.sendMessage(ColorUtil.translate(Config.MESSAGE_PREFIX + message));
        
        log.info("Player " + player.getName() + " set their language to " + languageCode.toUpperCase());
    }
    
    private void showPluginInfo(Player player) {
        player.sendMessage(ColorUtil.translate("&6&l=== TranslateX Plugin Info ==="));
        player.sendMessage(ColorUtil.translate("&7Version: &f1.0.0"));
        player.sendMessage(ColorUtil.translate("&7Author: &fDjorr (Rubix Development)"));
        player.sendMessage(ColorUtil.translate("&7Description: &fAdvanced translation plugin with OpenAI support"));
        player.sendMessage(ColorUtil.translate("&7Commands: &f/translator help"));
        player.sendMessage(ColorUtil.translate("&6&l================================"));
    }
    
    private void handleReload(Player player) {
        try {
            TranslateX.getInstance().reloadConfig();
            player.sendMessage(ColorUtil.translate(Config.MESSAGE_PREFIX + "&aConfiguration reloaded successfully!"));
            log.info("Configuration reloaded by " + player.getName());
        } catch (Exception e) {
            player.sendMessage(ColorUtil.translate(Config.MESSAGE_PREFIX + "&cFailed to reload configuration: " + e.getMessage()));
            log.severe("Failed to reload configuration: " + e.getMessage());
        }
    }
    
    private void showHelp(Player player) {
        player.sendMessage(ColorUtil.translate("&6&l=== TranslateX Commands ==="));
        player.sendMessage(ColorUtil.translate("&e/translator &7- Open language selection menu"));
        player.sendMessage(ColorUtil.translate("&e/translator set <lang> &7- Set your language"));
        player.sendMessage(ColorUtil.translate("&e/translator menu &7- Open language menu"));
        player.sendMessage(ColorUtil.translate("&e/translator info &7- Show plugin information"));
        player.sendMessage(ColorUtil.translate("&e/translator help &7- Show this help"));
        
        if (player.hasPermission(Config.ADMIN_PERMISSION)) {
            player.sendMessage(ColorUtil.translate("&e/translator reload &7- Reload configuration"));
        }
        
        player.sendMessage(ColorUtil.translate("&6&l================================"));
    }
    
    private boolean isValidLanguageCode(String languageCode) {
        String[] validCodes = {"EN", "DE", "FR", "ES", "IT", "NL", "PL", "PT", "RU", "JA", "ZH", "KO", "AR", "HI", "TR", "SV", "DA", "NO", "FI", "CS", "HU", "RO", "BG", "HR", "SK", "SL", "ET", "LV", "LT", "MT", "EL", "GA", "CY"};
        return Arrays.asList(validCodes).contains(languageCode.toUpperCase());
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
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            completions.addAll(Arrays.asList("language", "lang", "set", "menu", "info", "help"));
            
            if (sender.hasPermission(Config.ADMIN_PERMISSION)) {
                completions.add("reload");
            }
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("lang") || args[0].equalsIgnoreCase("language"))) {
            completions.addAll(Arrays.asList("EN", "DE", "FR", "ES", "IT", "NL", "PL", "PT", "RU", "JA", "ZH", "KO", "AR", "HI", "TR", "SV", "DA", "NO", "FI", "CS", "HU", "RO", "BG", "HR", "SK", "SL", "ET", "LV", "LT", "MT", "EL", "GA", "CY"));
        }
        
        return completions;
    }
}
