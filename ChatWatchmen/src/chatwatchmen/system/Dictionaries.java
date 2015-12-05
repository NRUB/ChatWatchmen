package chatwatchmen.system;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author NRUB
 */
public class Dictionaries {

    private static final ArrayList<String> WORDS = new ArrayList<>();
    private static final ArrayList<String> PHRASES = new ArrayList<>();
    private static final ArrayList<String> EXCEPTIONS = new ArrayList<>();

    public static void load(JavaPlugin plugin) throws IOException {
        WORDS.clear();
        PHRASES.clear();
        EXCEPTIONS.clear();

        InputStream loadDictionaries;
        try {
            loadDictionaries = new FileInputStream(new File("plugins/ChatWatchmen/dictionaries.yml"));
        } catch (FileNotFoundException FNFex) {
            InputStream in = plugin.getResource("dictionaries.yml");
            OutputStream out = new FileOutputStream(new File("plugins/ChatWatchmen", "dictionaries.yml"));
            byte[] buf = new byte[2048];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            loadDictionaries = new FileInputStream(new File("plugins/ChatWatchmen/dictionaries.yml"));
        }
        YamlConfiguration dictionaries = YamlConfiguration.loadConfiguration(new File("plugins/ChatWatchmen/dictionaries.yml"));

        ArrayList<String> temp;
        temp = (ArrayList<String>) dictionaries.getStringList("WORDS");
        for (String s : temp) {
            if (!WORDS.contains(s.toLowerCase())) {
                WORDS.add(s.toLowerCase());
            }
        }
        temp = (ArrayList<String>) dictionaries.getStringList("PHRASES");
        for (String s : temp) {
            if (!PHRASES.contains(s.toLowerCase())) {
                PHRASES.add(s.toLowerCase());
            }
        }
        temp = (ArrayList<String>) dictionaries.getStringList("EXCEPTIONS");
        for (String s : temp) {
            if (!EXCEPTIONS.contains(s.toLowerCase())) {
                EXCEPTIONS.add(s.toLowerCase());
            }
        }

        loadDictionaries.close();

        Collections.sort(WORDS);
        Collections.sort(PHRASES);
        Collections.sort(EXCEPTIONS);

        save(WORDS, PHRASES, EXCEPTIONS, false);
    }

    public static void save(ArrayList<String> W, ArrayList<String> P, ArrayList<String> E, boolean makeBackup) throws FileNotFoundException, IOException {
        BufferedWriter out;
        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("plugins/ChatWatchmen/dictionariesUpdate.yml", false), "UTF-8"));
        out.write("WORDS:\n");
        for (String word : W) {
            out.write("  - " + word + "\n");
        }
        out.write("PHRASES:\n");
        for (String phrase : P) {
            out.write("  - " + phrase + "\n");
        }
        out.write("EXCEPTIONS:\n");
        for (String exception : E) {
            out.write("  - " + exception + "\n");
        }
        out.close();
        if (makeBackup) {
            String date = "";
            Date today = new Date(System.currentTimeMillis());
            date += today.getYear() + 1900 + "-";
            date += today.getMonth() + 1;
            date += "-" + today.getDate() + " ";
            date += today.getHours() + "_";
            if (today.getMinutes() < 10) {
                date += "0";
            }
            date += today.getMinutes() + "_";
            if (today.getSeconds() < 10) {
                date += "0";
            }
            date += today.getSeconds();
            new File("plugins/ChatWatchmen/dictionaries.yml").renameTo(new File("plugins/ChatWatchmen/" + date + " - dictionaries.yml"));
        }
        new File("plugins/ChatWatchmen/dictionaries.yml").delete();
        new File("plugins/ChatWatchmen/dictionariesUpdate.yml").renameTo(new File("plugins/ChatWatchmen/dictionaries.yml"));
    }

    public static ArrayList<String> getWords() {
        return WORDS;
    }

    public static ArrayList<String> getPhrases() {
        return PHRASES;
    }

    public static ArrayList<String> getExceptions() {
        return EXCEPTIONS;
    }
}
