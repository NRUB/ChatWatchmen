package chatwatchmen.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author NRUB
 */
public class Config {

    private static ArrayList<String> commands=new ArrayList<>();
    private static String prefix;

    public static void load(JavaPlugin plugin) throws IOException, FileNotFoundException, InvalidConfigurationException {
        commands.clear();
        InputStream loadConfig;

        try {
            loadConfig=new FileInputStream(new File("plugins/ChatWatchmen/config.yml"));
            loadConfig.close();
        }
        catch (FileNotFoundException FNFex) {
            InputStream in=plugin.getResource("config.yml");
            OutputStream out=new FileOutputStream(new File("plugins/ChatWatchmen", "config.yml"));
            byte[] buf=new byte[2048];
            int len;
            while ((len=in.read(buf))>0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        }
        YamlConfiguration config=YamlConfiguration.loadConfiguration(new File("plugins/ChatWatchmen/config.yml"));

        ArrayList<String> tmp=(ArrayList<String>) config.getStringList("BlockedCmds");

        for (String s:tmp) {
            commands.add(s.toLowerCase().trim());
        }

        prefix=config.getString("Colors.prefix", "§f§l").replace('&', '§');
    }

    public static ArrayList<String> getCommands() {
        return commands;
    }

    public static String getPrefix() {
        return prefix;
    }
}
