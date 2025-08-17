package xyz.aizsargs.translate.command;

import lombok.extern.java.Log;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import xyz.aizsargs.translate.TranslateX;
import xyz.aizsargs.translate.data.Config;
import xyz.aizsargs.translate.translate.TranslatePlayer;
import xyz.aizsargs.translate.translate.menu.LanguageSelectionMenu;
import xyz.aizsargs.translate.util.ColorUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main command for TranslateX plugin
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
        
        if (!player.hasPermission(Config.TRANSLATE_PERMISSION)) {
            player.sendMessage(ColorUtil.translate(Config.MESSAGE_PREFIX + Config.NO_PERMISSION_MESSAGE));
            return true;
        }
        
        if (args.length == 0) {
            // Open language selection menu
            openLanguageMenu(player);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "language":
            case "lang":
            case "set":
                if (args.length < 2) {
                    player.sendMessage(ColorUtil.translate(Config.MESSAGE_PREFIX + "&cUsage: /translator set <language>"));
                    return true;
                }
                setPlayerLanguage(player, args[1]);
                break;
                
            case "menu":
                openLanguageMenu(player);
                break;
                
            case "info":
                showPluginInfo(player);
                break;
                
            case "reload":
                if (!player.hasPermission(Config.ADMIN_PERMISSION)) {
                    player.sendMessage(ColorUtil.translate(Config.MESSAGE_PREFIX + Config.NO_PERMISSION_MESSAGE));
                    return true;
                }
                reloadPlugin(player);
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
    
    private void openLanguageMenu(Player player) {
        LanguageSelectionMenu menu = new LanguageSelectionMenu(player);
        menu.open();
        player.sendMessage(ColorUtil.translate(Config.MESSAGE_PREFIX + "&aOpening language selection menu..."));
    }
    
    private void setPlayerLanguage(Player player, String languageInput) {
        // Get the language from input
        String languageCode = getLanguageCode(languageInput);
        if (languageCode == null) {
            player.sendMessage(ColorUtil.translate(Config.MESSAGE_PREFIX + Config.INVALID_LANGUAGE_MESSAGE));
            return;
        }
        
        // Set the player's language
        TranslatePlayer translatePlayer = TranslateX.getInstance().getTranslatorController().getTransLatePlayer(player);
        if (translatePlayer == null) {
            translatePlayer = new TranslatePlayer(player);
            TranslateX.getInstance().getTranslatorController().getTranslatePlayers().add(translatePlayer);
        }
        
        translatePlayer.setLanguageCode(languageCode);
        translatePlayer.setLanguage(getLanguageName(languageCode));
        
        // Save the data
        TranslateX.getInstance().saveConfig();
        
        // Send confirmation message
        String message = Config.LANGUAGE_SET_MESSAGE
            .replace("{language}", getLanguageName(languageCode))
            .replace("{code}", languageCode);
        
        player.sendMessage(ColorUtil.translate(Config.MESSAGE_PREFIX + message));
        
        log.info("Player " + player.getName() + " set their language to " + languageCode);
    }
    
    private void showPluginInfo(Player player) {
        player.sendMessage(ColorUtil.translate("&6&l🌐 TranslateX Plugin Information"));
        player.sendMessage(ColorUtil.translate("&7Version: &f1.0.0"));
        player.sendMessage(ColorUtil.translate("&7Author: &fDjorr (Rubix Development)"));
        player.sendMessage(ColorUtil.translate("&7Description: &fAdvanced translation plugin with multi-API support"));
        player.sendMessage(ColorUtil.translate("&7Discord: &fhttps://discord.rubixdevelopment.nl/"));
        player.sendMessage("");
        
        // Show current language
        TranslatePlayer translatePlayer = TranslateX.getInstance().getTranslatorController().getTransLatePlayer(player);
        if (translatePlayer != null) {
            player.sendMessage(ColorUtil.translate("&7Your current language: &a" + translatePlayer.getLanguage() + " (" + translatePlayer.getLanguageCode() + ")"));
        } else {
            player.sendMessage(ColorUtil.translate("&7Your current language: &cNot set"));
        }
    }
    
    private void reloadPlugin(Player player) {
        player.sendMessage(ColorUtil.translate(Config.MESSAGE_PREFIX + "&aReloading TranslateX plugin..."));
        TranslateX.getInstance().reloadConfig();
        player.sendMessage(ColorUtil.translate(Config.MESSAGE_PREFIX + "&aPlugin reloaded successfully!"));
    }
    
    private void showHelp(Player player) {
        player.sendMessage(ColorUtil.translate("&6&l🌐 TranslateX Commands"));
        player.sendMessage(ColorUtil.translate("&7/translator &f- Open language selection menu"));
        player.sendMessage(ColorUtil.translate("&7/translator menu &f- Open language selection menu"));
        player.sendMessage(ColorUtil.translate("&7/translator set <lang> &f- Set your language"));
        player.sendMessage(ColorUtil.translate("&7/translator info &f- Show plugin information"));
        player.sendMessage(ColorUtil.translate("&7/translator help &f- Show this help message"));
        
        if (player.hasPermission(Config.ADMIN_PERMISSION)) {
            player.sendMessage(ColorUtil.translate("&7/translator reload &f- Reload the plugin"));
        }
    }
    
    private String getLanguageCode(String input) {
        String upperInput = input.toUpperCase();
        
        // Direct language codes
        if (upperInput.matches("^[A-Z]{2}$")) {
            return upperInput;
        }
        
        // Language names
        switch (upperInput) {
            case "ENGLISH": return "EN";
            case "GERMAN": return "DE";
            case "FRENCH": return "FR";
            case "SPANISH": return "ES";
            case "ITALIAN": return "IT";
            case "DUTCH": return "NL";
            case "POLISH": return "PL";
            case "PORTUGUESE": return "PT";
            case "RUSSIAN": return "RU";
            case "JAPANESE": return "JA";
            case "CHINESE": return "ZH";
            case "KOREAN": return "KO";
            case "ARABIC": return "AR";
            case "HINDI": return "HI";
            case "TURKISH": return "TR";
            case "SWEDISH": return "SV";
            case "DANISH": return "DA";
            case "NORWEGIAN": return "NO";
            case "FINNISH": return "FI";
            case "CZECH": return "CS";
            case "HUNGARIAN": return "HU";
            case "ROMANIAN": return "RO";
            case "BULGARIAN": return "BG";
            case "CROATIAN": return "HR";
            case "SLOVAK": return "SK";
            case "SLOVENIAN": return "SL";
            case "ESTONIAN": return "ET";
            case "LATVIAN": return "LV";
            case "LITHUANIAN": return "LT";
            case "MALTESE": return "MT";
            case "GREEK": return "EL";
            case "IRISH": return "GA";
            case "WELSH": return "CY";
            default: return null;
        }
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
            List<String> subCommands = Arrays.asList("language", "lang", "set", "menu", "info", "help");
            
            if (sender.hasPermission(Config.ADMIN_PERMISSION)) {
                subCommands.add("reload");
            }
            
            for (String subCommand : subCommands) {
                if (subCommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subCommand);
                }
            }
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("language") || args[0].equalsIgnoreCase("lang"))) {
            // Language codes for tab completion
            String[] languages = {"EN", "DE", "FR", "ES", "IT", "NL", "PL", "PT", "RU", "JA", "ZH", "KO", "AR", "HI", "TR", "SV", "DA", "NO", "FI", "CS", "HU", "RO", "BG", "HR", "SK", "SL", "ET", "LV", "LT", "MT", "EL", "GA", "CY"};
            
            for (String language : languages) {
                if (language.toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(language);
                }
            }
        }
        
        return completions;
    }
}
