package chatwatchmen;

import chatwatchmen.listeners.BlockedCommands;
import chatwatchmen.listeners.ChatMessage;
import chatwatchmen.system.Config;
import chatwatchmen.system.Dictionaries;
import chatwatchmen.system.Lang;
import chatwatchmen.system.Spy;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author NRUB
 */
public class ChatWatchmen extends JavaPlugin {

    public static final Logger log = Logger.getLogger("ChatWatchmen");
    public static boolean CHANGED = false;

    private static ArrayList<String> words;
    private static ArrayList<String> phrases;
    private static ArrayList<String> exceptions;

    @Override
    public void onEnable() {
        if (!(new File("plugins", "ChatWatchmen").isDirectory())) {
            new File("plugins", "ChatWatchmen").mkdir();
        }
        try {
            Lang.load(this);
            Dictionaries.load(this);
            Config.load(this);
            Spy.load(this);
            words = Dictionaries.getWords();
            phrases = Dictionaries.getPhrases();
            exceptions = Dictionaries.getExceptions();
        } catch (IOException | InvalidConfigurationException ex) {
        }

        PluginManager listeners = getServer().getPluginManager();
        listeners.registerEvents(new ChatMessage(), this);
        listeners.registerEvents(new BlockedCommands(), this);
    }

    @Override
    public void onDisable() {
        try {
            Dictionaries.save(words, phrases, exceptions, CHANGED);
            Spy.save();
        } catch (IOException IOex) {
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("chatadd")) { //adding new phrase/word to list of restricted words or exceptions
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("word") || args[0].equalsIgnoreCase("w")) {
                    add(sender, "W", args[1]);
                    return true;
                }
                if (args[0].equalsIgnoreCase("phrase") || args[0].equalsIgnoreCase("p")) {
                    add(sender, "P", args[1]);
                    return true;
                }
                if (args[0].equalsIgnoreCase("exception") || args[0].equalsIgnoreCase("e")) {
                    add(sender, "E", args[1]);
                    return true;
                }
            }
            return false;
        }
        if (cmd.getName().equalsIgnoreCase("chatremove")) { //removing phrase/word from list of restricted words or exceptions
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("word") || args[0].equalsIgnoreCase("w")) {
                    remove(sender, "W", args[1]);
                    return true;
                }
                if (args[0].equalsIgnoreCase("phrase") || args[0].equalsIgnoreCase("p")) {
                    remove(sender, "P", args[1]);
                    return true;
                }
                if (args[0].equalsIgnoreCase("exception") || args[0].equalsIgnoreCase("e")) {
                    remove(sender, "E", args[1]);
                    return true;
                }
            }
            return false;
        }
        if (cmd.getName().equalsIgnoreCase("chatshow")) { //showing list of all restricted phrases/words or all exceptions
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("words") || args[0].equalsIgnoreCase("w")) {
                    show(sender, "W");
                    return true;
                }
                if (args[0].equalsIgnoreCase("phrases") || args[0].equalsIgnoreCase("p")) {
                    show(sender, "P");
                    return true;
                }
                if (args[0].equalsIgnoreCase("exceptions") || args[0].equalsIgnoreCase("e")) {
                    show(sender, "E");
                    return true;
                }
            }
            return false;
        }
        if (cmd.getName().equalsIgnoreCase("chatreload")) { //reloading wordlists
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("soft") || args[0].equalsIgnoreCase("s")) {
                    try {
                        reloadS(sender);
                    } catch (IOException | InvalidConfigurationException ex) {
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("hard") || args[0].equalsIgnoreCase("h")) {
                    try {
                        reloadH(sender);
                    } catch (IOException | InvalidConfigurationException ex) {
                    }
                    return true;
                }
            }
            return false;
        }
        if (cmd.getName().equalsIgnoreCase("chatspy")) { //reloading wordlists
            if (args.length == 0) {
                spy(sender.getName());
                return true;
            }
            if (args.length == 1) {
                spy(args[0]);
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> getWords() {
        return words;
    }

    public static ArrayList<String> getPhrases() {
        return phrases;
    }

    public static ArrayList<String> getExceptions() {
        return exceptions;
    }

    private void add(CommandSender sender, String type, String value) { //types(W-word, P-phrase, E-exception)
        if (type.equals("W")) {
            if (!words.contains(value.toLowerCase())) {
                words.add(value.toLowerCase());
            }
            sender.sendMessage(Lang.getMessage("WAL") + value.toLowerCase() + Lang.getMessage("WAR"));
        }
        if (type.equals("P")) {
            if (!phrases.contains(value.toLowerCase())) {
                phrases.add(value.toLowerCase());
            }
            sender.sendMessage(Lang.getMessage("PAL") + value.toLowerCase() + Lang.getMessage("PAR"));
        }
        if (type.equals("E")) {
            if (!exceptions.contains(value.toLowerCase())) {
                exceptions.add(value.toLowerCase());
            }
            sender.sendMessage(Lang.getMessage("EAL") + value.toLowerCase() + Lang.getMessage("EAR"));
        }
        CHANGED = true;
    }

    private void remove(CommandSender sender, String type, String value) { //types(W-word, P-phrase, E-exception)
        if (type.equals("W")) {
            words.remove(value.toLowerCase());
            sender.sendMessage(Lang.getMessage("WRL") + value.toLowerCase() + Lang.getMessage("WRR"));
        }
        if (type.equals("P")) {
            phrases.remove(value.toLowerCase());
            sender.sendMessage(Lang.getMessage("PRL") + value.toLowerCase() + Lang.getMessage("PRR"));
        }
        if (type.equals("E")) {
            exceptions.remove(value.toLowerCase());
            sender.sendMessage(Lang.getMessage("ERL") + value.toLowerCase() + Lang.getMessage("ERR"));
        }
        CHANGED = true;
    }

    private void show(CommandSender sender, String type) { //types(W-word, P-phrase, E-exception)
        if (type.equals("W")) {
            sender.sendMessage(Lang.getMessage("SW"));
            for (String next : words) {
                sender.sendMessage(next);
            }
        }
        if (type.equals("P")) {
            sender.sendMessage(Lang.getMessage("SP"));
            for (String next : phrases) {
                sender.sendMessage(next);
            }
        }
        if (type.equals("E")) {
            sender.sendMessage(Lang.getMessage("SE"));
            for (String next : exceptions) {
                sender.sendMessage(next);
            }
        }
    }

    private void reloadS(CommandSender sender) throws IOException, FileNotFoundException, InvalidConfigurationException { //soft reload
        Lang.load(this);
        Config.load(this);
        Dictionaries.load(this);
        words = Dictionaries.getWords();
        phrases = Dictionaries.getPhrases();
        exceptions = Dictionaries.getExceptions();

        CHANGED = false;
        sender.sendMessage(Lang.getMessage("PRS"));
    }

    private void reloadH(CommandSender sender) throws IOException, FileNotFoundException, InvalidConfigurationException { //hard reload
        Lang.load(this);
        Config.load(this);
        Dictionaries.save(words, phrases, exceptions, CHANGED);
        Dictionaries.load(this);
        words = Dictionaries.getWords();
        phrases = Dictionaries.getPhrases();
        exceptions = Dictionaries.getExceptions();

        CHANGED = false;
        sender.sendMessage(Lang.getMessage("PRH"));
    }

    private void spy(String name) {
        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            if (Spy.canSee(name)) {
                player.sendMessage(Lang.getMessage("Soff"));
            }
            else {
                player.sendMessage(Lang.getMessage("Son"));
            }
            Spy.switchSee(name);
        }
    }
}
