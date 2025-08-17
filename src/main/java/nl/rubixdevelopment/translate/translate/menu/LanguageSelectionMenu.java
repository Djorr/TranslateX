package nl.rubixdevelopment.translate.translate.menu;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import nl.rubixdevelopment.translate.TranslateX;
import nl.rubixdevelopment.translate.data.Config;
import nl.rubixdevelopment.translate.translate.TranslatePlayer;
import nl.rubixdevelopment.translate.util.ColorUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Beautiful language selection menu with flags and native names
 * Compatible with Paper and Spigot servers
 */
public class LanguageSelectionMenu {
    
    @Getter
    private final Inventory inventory;
    private final Player player;
    
    public LanguageSelectionMenu(Player player) {
        this.player = player;
        this.inventory = Bukkit.createInventory(null, 54, ColorUtil.translate("&6&l🌐 Select Your Language"));
        initializeMenu();
    }
    
    private void initializeMenu() {
        // Popular languages (first row)
        addLanguageItem(10, "EN", "🇺🇸 English", "English", Material.BOOK);
        addLanguageItem(11, "DE", "🇩🇪 Deutsch", "German", Material.BOOK);
        addLanguageItem(12, "FR", "🇫🇷 Français", "French", Material.BOOK);
        addLanguageItem(13, "ES", "🇪🇸 Español", "Spanish", Material.BOOK);
        addLanguageItem(14, "IT", "🇮🇹 Italiano", "Italian", Material.BOOK);
        addLanguageItem(15, "NL", "🇳🇱 Nederlands", "Dutch", Material.BOOK);
        addLanguageItem(16, "PL", "🇵🇱 Polski", "Polish", Material.BOOK);
        
        // Second row
        addLanguageItem(19, "PT", "🇵🇹 Português", "Portuguese", Material.BOOK);
        addLanguageItem(20, "RU", "🇷🇺 Русский", "Russian", Material.BOOK);
        addLanguageItem(21, "JA", "🇯🇵 日本語", "Japanese", Material.BOOK);
        addLanguageItem(22, "ZH", "🇨🇳 中文", "Chinese", Material.BOOK);
        addLanguageItem(23, "KO", "🇰🇷 한국어", "Korean", Material.BOOK);
        addLanguageItem(24, "AR", "🇸🇦 العربية", "Arabic", Material.BOOK);
        addLanguageItem(25, "HI", "🇮🇳 हिन्दी", "Hindi", Material.BOOK);
        
        // Third row
        addLanguageItem(28, "TR", "🇹🇷 Türkçe", "Turkish", Material.BOOK);
        addLanguageItem(29, "SV", "🇸🇪 Svenska", "Swedish", Material.BOOK);
        addLanguageItem(30, "DA", "🇩🇰 Dansk", "Danish", Material.BOOK);
        addLanguageItem(31, "NO", "🇳🇴 Norsk", "Norwegian", Material.BOOK);
        addLanguageItem(32, "FI", "🇫🇮 Suomi", "Finnish", Material.BOOK);
        addLanguageItem(33, "CS", "🇨🇿 Čeština", "Czech", Material.BOOK);
        addLanguageItem(34, "HU", "🇭🇺 Magyar", "Hungarian", Material.BOOK);
        
        // Fourth row
        addLanguageItem(37, "RO", "🇷🇴 Română", "Romanian", Material.BOOK);
        addLanguageItem(38, "BG", "🇧🇬 Български", "Bulgarian", Material.BOOK);
        addLanguageItem(39, "HR", "🇭🇷 Hrvatski", "Croatian", Material.BOOK);
        addLanguageItem(40, "SK", "🇸🇰 Slovenčina", "Slovak", Material.BOOK);
        addLanguageItem(41, "SL", "🇸🇮 Slovenščina", "Slovenian", Material.BOOK);
        addLanguageItem(42, "ET", "🇪🇪 Eesti", "Estonian", Material.BOOK);
        addLanguageItem(43, "LV", "🇱🇻 Latviešu", "Latvian", Material.BOOK);
        
        // Bottom row - special items
        addInfoItem(45, Material.BOOK, "&6&lℹ️ Information", 
            "&7Click on a language to set it as your preferred language.",
            "&7Your messages will be translated to this language.",
            "&7Other players will see your messages in their language."
        );
        
        addCurrentLanguageItem(49);
        
        addCloseItem(53);
    }
    
    private void addLanguageItem(int slot, String languageCode, String displayName, 
                               String englishName, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ColorUtil.translate("&a" + displayName));
        
        List<String> lore = new ArrayList<>();
        lore.add(ColorUtil.translate("&7Language Code: &f" + languageCode));
        lore.add(ColorUtil.translate("&7English Name: &f" + englishName));
        lore.add("");
        lore.add(ColorUtil.translate("&eClick to select this language!"));
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        inventory.setItem(slot, item);
    }
    
    private void addInfoItem(int slot, Material material, String title, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ColorUtil.translate(title));
        
        List<String> loreList = new ArrayList<>();
        for (String line : lore) {
            loreList.add(ColorUtil.translate(line));
        }
        
        meta.setLore(loreList);
        item.setItemMeta(meta);
        
        inventory.setItem(slot, item);
    }
    
    private void addCurrentLanguageItem(int slot) {
        TranslatePlayer translatePlayer = TranslateX.getInstance().getTranslatorController().getTransLatePlayer(player);
        String currentLanguage = "EN";
        String currentLanguageName = "English";
        
        if (translatePlayer != null) {
            currentLanguage = translatePlayer.getLanguageCode();
            currentLanguageName = getLanguageDisplayName(currentLanguage);
        }
        
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ColorUtil.translate("&a&l✅ Current Language"));
        
        List<String> lore = new ArrayList<>();
        lore.add(ColorUtil.translate("&7Your current language is:"));
        lore.add(ColorUtil.translate("&a" + currentLanguageName));
        lore.add(ColorUtil.translate("&7Code: &f" + currentLanguage));
        lore.add("");
        lore.add(ColorUtil.translate("&7Click to refresh"));
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        inventory.setItem(slot, item);
    }
    
    private void addCloseItem(int slot) {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ColorUtil.translate("&c&l❌ Close Menu"));
        
        List<String> lore = new ArrayList<>();
        lore.add(ColorUtil.translate("&7Click to close this menu"));
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        inventory.setItem(slot, item);
    }
    
    private String getLanguageDisplayName(String languageCode) {
        switch (languageCode.toUpperCase()) {
            case "EN": return "🇺🇸 English";
            case "DE": return "🇩🇪 German";
            case "FR": return "🇫🇷 French";
            case "ES": return "🇪🇸 Spanish";
            case "IT": return "🇮🇹 Italian";
            case "NL": return "🇳🇱 Dutch";
            case "PL": return "🇵🇱 Polish";
            case "PT": return "🇵🇹 Portuguese";
            case "RU": return "🇷🇺 Russian";
            case "JA": return "🇯🇵 Japanese";
            case "CS": return "🇨🇿 Czech";
            case "HU": return "🇭🇺 Hungarian";
            case "RO": return "🇷🇴 Romanian";
            case "BG": return "🇧🇬 Bulgarian";
            case "HR": return "🇭🇷 Croatian";
            case "SK": return "🇸🇰 Slovak";
            case "SL": return "🇸🇮 Slovenian";
            case "ET": return "🇪🇪 Estonian";
            case "LV": return "🇱🇻 Latvian";
            case "LT": return "🇱🇹 Lithuanian";
            case "MT": return "🇲🇹 Maltese";
            case "EL": return "🇬🇷 Greek";
            case "GA": return "🇮🇪 Irish";
            case "CY": return "🇨🇾 Welsh";
            default: return languageCode;
        }
    }
    
    public void open() {
        player.openInventory(inventory);
    }
}
