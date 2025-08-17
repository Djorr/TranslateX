package xyz.aizsargs.translate.translate.menu;

import lombok.extern.java.Log;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.aizsargs.translate.TranslateX;
import xyz.aizsargs.translate.data.Config;
import xyz.aizsargs.translate.translate.TranslatePlayer;
import xyz.aizsargs.translate.util.ColorUtil;

/**
 * Handles inventory clicks for the language selection menu
 */
@Log
public class LanguageMenuListener implements Listener {
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        // Check if this is our language selection menu
        if (!title.contains("Select Your Language")) {
            return;
        }
        
        event.setCancelled(true);
        
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }
        
        handleMenuClick(player, clickedItem, event.getSlot());
    }
    
    private void handleMenuClick(Player player, ItemStack clickedItem, int slot) {
        String itemName = clickedItem.getItemMeta().getDisplayName();
        
        // Handle language selection
        if (clickedItem.getType() == Material.BOOK && slot >= 10 && slot <= 43) {
            handleLanguageSelection(player, clickedItem);
        }
        // Handle current language refresh
        else if (slot == 49 && itemName.contains("Current Language")) {
            refreshCurrentLanguage(player);
        }
        // Handle close button
        else if (slot == 53 && itemName.contains("Close")) {
            player.closeInventory();
        }
    }
    
    private void handleLanguageSelection(Player player, ItemStack languageItem) {
        String itemName = languageItem.getItemMeta().getDisplayName();
        
        // Extract language code from the item name
        String languageCode = extractLanguageCode(itemName);
        if (languageCode == null) {
            player.sendMessage(ColorUtil.translate(Config.MESSAGE_PREFIX + "&cCould not determine language code"));
            return;
        }
        
        // Set the player's language
        TranslatePlayer translatePlayer = TranslateX.getInstance().getTranslatorController().getTransLatePlayer(player);
        if (translatePlayer == null) {
            // Create new translate player
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
        
        // Close the inventory
        player.closeInventory();
        
        // Log the language change
        log.info("Player " + player.getName() + " set their language to " + languageCode);
    }
    
    private void refreshCurrentLanguage(Player player) {
        // Reopen the menu to refresh the current language display
        LanguageSelectionMenu menu = new LanguageSelectionMenu(player);
        menu.open();
    }
    
    private String extractLanguageCode(String itemName) {
        // Remove color codes and find the language code
        String cleanName = itemName.replaceAll("§[0-9a-fk-or]", "");
        
        // Look for language codes in the name
        if (cleanName.contains("🇺🇸")) return "EN";
        if (cleanName.contains("🇩🇪")) return "DE";
        if (cleanName.contains("🇫🇷")) return "FR";
        if (cleanName.contains("🇪🇸")) return "ES";
        if (cleanName.contains("🇮🇹")) return "IT";
        if (cleanName.contains("🇳🇱")) return "NL";
        if (cleanName.contains("🇵🇱")) return "PL";
        if (cleanName.contains("🇵🇹")) return "PT";
        if (cleanName.contains("🇷🇺")) return "RU";
        if (cleanName.contains("🇯🇵")) return "JA";
        if (cleanName.contains("🇨🇳")) return "ZH";
        if (cleanName.contains("🇰🇷")) return "KO";
        if (cleanName.contains("🇸🇦")) return "AR";
        if (cleanName.contains("🇮🇳")) return "HI";
        if (cleanName.contains("🇹🇷")) return "TR";
        if (cleanName.contains("🇸🇪")) return "SV";
        if (cleanName.contains("🇩🇰")) return "DA";
        if (cleanName.contains("🇳🇴")) return "NO";
        if (cleanName.contains("🇫🇮")) return "FI";
        if (cleanName.contains("🇨🇿")) return "CS";
        if (cleanName.contains("🇭🇺")) return "HU";
        if (cleanName.contains("🇷🇴")) return "RO";
        if (cleanName.contains("🇧🇬")) return "BG";
        if (cleanName.contains("🇭🇷")) return "HR";
        if (cleanName.contains("🇸🇰")) return "SK";
        if (cleanName.contains("🇸🇮")) return "SL";
        if (cleanName.contains("🇪🇪")) return "ET";
        if (cleanName.contains("🇱🇻")) return "LV";
        
        return null;
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
