package chatwatchmen.listeners;

import chatwatchmen.ChatWatchmen;
import chatwatchmen.system.Config;
import chatwatchmen.system.Lang;
import chatwatchmen.system.Spy;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import static org.bukkit.Bukkit.getServer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 *
 * @author NRUB
 */
public class ChatMessage implements Listener {

    private static String suffix="";
    private static String originalMessage;
    private static String[] messageWords;
    private static String censoredMessage;

    private static ArrayList<String> WORDS;
    private static ArrayList<String> PHRASES;
    private static ArrayList<String> EXCEPTIONS;

    @EventHandler
    synchronized public void BukkitCatchMessage(AsyncPlayerChatEvent PCevent) {

        setPrefix(PCevent.getFormat());

        boolean wasCensored=false;

        censoredMessage="";
        originalMessage=PCevent.getMessage();
        WORDS=ChatWatchmen.getWords();
        PHRASES=ChatWatchmen.getPhrases();
        EXCEPTIONS=ChatWatchmen.getExceptions();

        messageWords=originalMessage.split(" ");

        for (String w:messageWords) { //checking all words in message
            String tempWord=w;
            String prepared=prepareWord(w);
            boolean wasVulgar=false;
            if (stepA(prepared)) {
                tempWord=replaceVulgar(prepared);
                wasCensored=true;
                wasVulgar=true;
            }
            else {
                if (stepB(prepared)&&!stepC(prepared)) {
                    tempWord=replaceVulgar(prepared);
                    wasCensored=true;
                    wasVulgar=true;
                }
            }
            if (!wasVulgar) {
                setPrefix(w);
            }
            wasVulgar=false;
            censoredMessage+=tempWord+" ";
        }
        censoredMessage=censoredMessage.trim();

        if (wasCensored) {
            getServer().getConsoleSender().sendMessage(ChatColor.RED+Lang.getMessage("WC")+"\""+originalMessage+"\" ~"+PCevent.getPlayer().getName().replaceAll("§.", "").replaceAll("&.", ""));

            for (Player p:Bukkit.getOnlinePlayers()) {
                if (Spy.canSee(p.getName())) {
                    p.sendMessage("§4[ChatWatchmen]: §f"+PCevent.getPlayer().getName()+": "+originalMessage);
                }
                else {
                }
            }
        }
        PCevent.setMessage(censoredMessage);
    }

    private String prepareWord(String wordToMask) {
        String maskedWord=wordToMask;

        //catch all prefix even if it's not a color prefix like "Hide&Seek"
        maskedWord=maskedWord.replaceAll("§.", "");
        maskedWord=maskedWord.replaceAll("&.", "");
        maskedWord=maskedWord.trim();
        maskedWord=maskedWord.replaceAll(" ", "");

        return maskedWord;
    }

    private boolean stepA(String word) { //true if vulgar
        return WORDS.contains(word.toLowerCase());
    }

    private boolean stepB(String word) { //true if containt vulgar phrase
        for (String p:PHRASES) {
            if ((word.toLowerCase()).contains(p)) {
                return true;
            }
        }
        return false;
    }

    private boolean stepC(String word) { //true if is exception
        for (String e:EXCEPTIONS) {
            if (word.equalsIgnoreCase(e)) {
                return true;
            }
        }
        return false;
    }

    private String replaceVulgar(String vulgar) {
        int letters=vulgar.length();
        String censored=Config.getPrefix();
        for (; letters>0; letters--) {
            censored+="*";
        }
        censored+=suffix;
        return censored;
    }

    private void setPrefix(String string) {
        char[] c=string.toCharArray();
        for (int i=0; i<c.length; i++) {
            if (c[i]=='&'||c[i]=='§') {
                suffix="§"+c[i+1];
            }
        }
    }
}
