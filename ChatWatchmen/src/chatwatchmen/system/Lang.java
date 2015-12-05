package chatwatchmen.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author NRUB
 */
public class Lang {

    private static String PRS; //PLUGIN_RELOADED_SOFT
    private static String PRH; //PLUGIN_RELOADED_HARD
    private static String Son; //SPY_ON
    private static String Soff; //SPY_OFF

    private static String WAL; //WORD_ADD_LEFT
    private static String WAR; //WORD_ADD_RIGHT
    private static String PAL; //PHRASE_ADD_LEFT
    private static String PAR; //PHRASE_ADD_RIGHT
    private static String EAL; //EXCEPTION_ADD_LEFT
    private static String EAR; //EXCEPTION_ADD_RIGHT

    private static String WRL; //WORD_REMOVE_LEFT
    private static String WRR; //WORD_REMOVE_RIGHT
    private static String PRL; //PHRASE_REMOVE_LEFT
    private static String PRR; //PHRASE_REMOVE_RIGHT
    private static String ERL; //EXCEPTION_REMOVE_LEFT
    private static String ERR; //EXCEPTION_REMOVE_RIGHT

    private static String SW; //SHOW_WORDS
    private static String SP; //SHOW_PHRASE
    private static String SE; //SHOW_EXCEPTION

    private static String WC; //WAS_CENSORED
    private static String KM; //KICK_MESSAGE

    private static Map<String, String> langPack;

    public static void load(JavaPlugin plugin) throws IOException {
        Yaml lang = new Yaml();
        InputStream loadLang;
        try {
            loadLang = new FileInputStream(new File("plugins/ChatWatchmen/lang.yml"));
        } catch (FileNotFoundException FNFex) {
            InputStream in = plugin.getResource("lang.yml");
            OutputStream out = new FileOutputStream(new File("plugins/ChatWatchmen", "lang.yml"));
            byte[] buf = new byte[2048];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            loadLang = new FileInputStream(new File("plugins/ChatWatchmen/lang.yml"));
        }
        langPack = (Map<String, String>) lang.load(loadLang);

        PRS = langPack.get("PLUGIN_RELOADED_SOFT");
        PRH = langPack.get("PLUGIN_RELOADED_HARD");
        Son = langPack.get("SPY_ON");
        Soff = langPack.get("SPY_OFF");

        WAL = langPack.get("WORD_ADD_LEFT");
        WAR = langPack.get("WORD_ADD_RIGHT");
        PAL = langPack.get("PHRASE_ADD_LEFT");
        PAR = langPack.get("PHRASE_ADD_RIGHT");
        EAL = langPack.get("EXCEPTION_ADD_LEFT");
        EAR = langPack.get("EXCEPTION_ADD_RIGHT");

        WRL = langPack.get("WORD_REMOVE_LEFT");
        WRR = langPack.get("WORD_REMOVE_RIGHT");
        PRL = langPack.get("PHRASE_REMOVE_LEFT");
        PRR = langPack.get("PHRASE_REMOVE_RIGHT");
        ERL = langPack.get("EXCEPTION_REMOVE_LEFT");
        ERR = langPack.get("EXCEPTION_REMOVE_RIGHT");

        SW = langPack.get("SHOW_WORDS");
        SP = langPack.get("SHOW_PHRASE");
        SE = langPack.get("SHOW_EXCEPTION");

        WC = langPack.get("WAS_CENSORED");
        KM = langPack.get("KICK_MESSAGE");
    }

    public static String getMessage(String msg) {
        switch (msg) {
            case "PRS":
                return PRS;
            case "PRH":
                return PRH;
            case "Son":
                return Son;
            case "Soff":
                return Soff;

            case "WAL":
                return WAL;
            case "WAR":
                return WAR;
            case "PAL":
                return PAL;
            case "PAR":
                return PAR;
            case "EAL":
                return EAL;
            case "EAR":
                return EAR;

            case "WRL":
                return WRL;
            case "WRR":
                return WRR;
            case "PRL":
                return PRL;
            case "PRR":
                return PRR;
            case "ERL":
                return ERL;
            case "ERR":
                return ERR;

            case "SW":
                return SW;
            case "SP":
                return SP;
            case "SE":
                return SE;

            case "WC":
                return WC;
            case "KM":
                return KM;
        }
        return null;
    }
}
