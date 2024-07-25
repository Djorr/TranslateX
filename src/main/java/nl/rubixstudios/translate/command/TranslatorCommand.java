package nl.rubixstudios.translate.command;

import com.deepl.api.Language;
import com.deepl.api.Translator;
import lombok.SneakyThrows;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import nl.rubixstudios.translate.TranslateX;
import nl.rubixstudios.translate.data.ConfigFile;
import nl.rubixstudios.translate.translate.TranslateController;
import nl.rubixstudios.translate.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class TranslatorCommand implements CommandExecutor, TabCompleter {

    private final TranslateController translateController;

    public TranslatorCommand() {
        this.translateController = TranslateController.getInstance();
    }

    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!command.getName().equals("translatorr") && !command.getName().equals("tr") && !command.getName().equals("translator") && !command.getName().equals("t"))
            return true;

        if (args.length == 0) {
            this.sendHelpMessage(sender);
            return false;
        }

        switch (args[0]) {
            case "help":
            case "?":
            case "h": {
                this.sendHelpMessage(sender);
                break;
            }
            case "reload":
            case "rl": {
                if (!sender.hasPermission("translatorl.reload")) {
                    sender.sendMessage(ColorUtil.translate("&cYou don't have permission to do this."));
                    return false;
                }

                TranslateX.getInstance().reloadConfig();
                sender.sendMessage(ColorUtil.translate("&aReloaded the plugin."));
                break;
            }
            case "translate":
            case "t": {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ColorUtil.translate("&cYou must be a player to do this."));
                    return false;
                }

                final Player player = (Player) sender;
                if (!player.hasPermission("translatorl.translate")) {
                    sender.sendMessage(ColorUtil.translate("&cYou don't have permission to do this."));
                    return false;
                }

                if (args.length < 3) {
                    sender.sendMessage(ColorUtil.translate("&cUsage: /translator translate <langcode> <message>"));
                    return false;
                }

                final String language = args[1];

                final StringBuilder message = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    message.append(args[i]).append(" ");
                }

                this.translateController.sendTranslatedMessage(player, language, message.toString());
                break;
            }
            case "list":
            case "l":{
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ColorUtil.translate("&cYou must be a player to do this."));
                    return false;
                }

                final Player player = (Player) sender;
                if (!player.hasPermission("translatorl.list")) {
                    sender.sendMessage(ColorUtil.translate("&cYou don't have permission to do this."));
                    return false;
                }

                final Translator translator = this.translateController.getTranslator();
                if (translator == null) {
                    sender.sendMessage(ColorUtil.translate("&cThe translator is not initialized yet."));
                    return false;
                }

                this.sendLanguageList(player, translator.getSourceLanguages(), args);
                break;
            }
            case "setlang": {
                if (args.length < 2) {
                    sender.sendMessage(ColorUtil.translate("&cUsage: /translator setlang <langcode>"));
                    return false;
                }

                if (!(sender instanceof Player)) {
                    sender.sendMessage(ColorUtil.translate("&cYou must be a player to do this."));
                    return false;
                }

                final Player player = (Player) sender;
                if (!player.hasPermission("translatorl.setlang")) {
                    sender.sendMessage(ColorUtil.translate("&cYou don't have permission to do this."));
                    return false;
                }

                final String language = args[1];
                this.translateController.setPlayerLanguage(player, language);
                break;
            }
        }

        return false;
    }

    @SneakyThrows
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();

        final String commandName = cmd.getName();
        if (commandName.equalsIgnoreCase("translatorr")
                || commandName.equalsIgnoreCase("tr")
                || commandName.equalsIgnoreCase("translator")
                || commandName.equalsIgnoreCase("t")) {
            if (args.length == 1) {
                return Arrays.asList("help", "?", "h", "reload", "rl", "translate", "t", "list", "l", "setlang", "sl");
            }

            if (args.length == 2) {
                final String subCommand = args[0];
                if (subCommand.equalsIgnoreCase("setlang")) {
                    return this.translateController.getTranslator().getSourceLanguages().stream().map(Language::getCode).collect(Collectors.toList());
                }
            }
        }

        return completions;
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ColorUtil.translate("&7&m----------------------------------------"));
        sender.sendMessage(ColorUtil.translate("&6&lTranslateX &8- &fHelp"));
        sender.sendMessage(ColorUtil.translate("&7&m----------------------------------------"));
        sender.sendMessage(ColorUtil.translate("&e/translator help &8- &fShows this help message."));
        sender.sendMessage(ColorUtil.translate("&e/translator reload &8- &fReloads the plugin."));
        sender.sendMessage(ColorUtil.translate("&e/translator translate <langcode> <message> &8- &fTranslates a message."));
        sender.sendMessage(ColorUtil.translate("&e/translator list &8- &7Lists all available languages."));
        sender.sendMessage(ColorUtil.translate("&7&m----------------------------------------"));
    }

    private void sendLanguageList(Player player, List<Language> languages, String[] args) {
        if (languages.isEmpty()) {
            player.sendMessage(ColorUtil.translate("&6&lTranslateX &8» &7There are no languages available."));
            return;
        }

        final int linesEachPage = 5;
        final int languageSize = languages.size();

        int currentPage = 1;
        if (args.length >= 1) {
            try {
                if (args.length == 2) {
                    currentPage = Integer.parseInt(args[1]);
                    currentPage = Math.max(currentPage, 1);
                } else {
                    currentPage = Integer.parseInt(args[0]);
                    currentPage = Math.max(currentPage, 1);
                }
            } catch (NumberFormatException e) {
                //do nothing
            }
        }
        int totalPages = (int) Math.ceil(languageSize / (double) linesEachPage);
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        player.sendMessage(ColorUtil.translate("&7&m----------------------------------------"));
        player.sendMessage(ColorUtil.translate("&6&lTranslateX &8- &fList of all languages &7(&e<page>/<maxPage>&7)"
                .replace("<page>", String.valueOf(currentPage))
                .replace("<maxPage>", String.valueOf(totalPages))));
        player.sendMessage(ColorUtil.translate("&7 langcode &8| &elanguage"));
        player.sendMessage(ColorUtil.translate(""));

        for (int i = 0; i < linesEachPage; i++) {
            final int index = (currentPage - 1) * linesEachPage + i;
            if (index >= languageSize) {
                break;
            }

            final Language language = languages.get(index);
            player.sendMessage(ColorUtil.translate("&8- &7<id> &8| &e<name>"
                    .replace("<id>", String.valueOf(language.getCode()))
                    .replace("<name>", language.getName())));
        }


        final TextComponent goToPreviousPage = new TextComponent(ColorUtil.translate("\n&cPrevious page"));
        goToPreviousPage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtil.translate("&7Click to go to the previous page.")).create()));
        goToPreviousPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/translator list " + (currentPage - 1)));

        final TextComponent tussenPage = new TextComponent(ColorUtil.translate(" &8| &7"));

        final TextComponent goToNextPage = new TextComponent(ColorUtil.translate("&aNext page\n"));
        goToNextPage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtil.translate("&7Click to go to the next page.")).create()));
        goToNextPage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/translator list " + (currentPage + 1)));

        final TextComponent end = new TextComponent(ColorUtil.translate("&7&m----------------------------------------"));

        player.spigot().sendMessage(goToPreviousPage, tussenPage, goToNextPage, end);
    }
}
