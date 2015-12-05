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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author NRUB
 */
public class Spy {

    private static ArrayList<String> spy=new ArrayList<>();

    public static void load(JavaPlugin plugin) throws IOException {
        spy.clear();
        InputStream loadConfig;

        try {
            loadConfig=new FileInputStream(new File("plugins/ChatWatchmen/spy.yml"));
            loadConfig.close();
        }
        catch (FileNotFoundException FNFex) {
            InputStream in=plugin.getResource("spy.yml");
            OutputStream out=new FileOutputStream(new File("plugins/ChatWatchmen", "spy.yml"));
            byte[] buf=new byte[2048];
            int len;
            while ((len=in.read(buf))>0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        }
        YamlConfiguration config=YamlConfiguration.loadConfiguration(new File("plugins/ChatWatchmen/spy.yml"));

        spy=(ArrayList<String>) config.getStringList("chatwatchmenSee");
    }

    public static void save() throws UnsupportedEncodingException, FileNotFoundException, IOException {
        BufferedWriter out;
        out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("plugins/ChatWatchmen/spy.yml", false), "UTF-8"));
        out.write("chatwatchmenSee:\n");
        for (String word:spy) {
            out.write("  - "+word+"\n");
        }
        out.close();
    }

    public static void switchSee(String nick) {
        if (spy.contains(nick)) {
            spy.remove(nick);
        }
        else {
            spy.add(nick);
        }
    }

    public static boolean canSee(String player) {
        return spy.contains(player);
    }
}
