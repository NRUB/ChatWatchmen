package chatwatchmen.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author NRUB
 */
public class Config {

    private static final List<String> commands = new LinkedList<>();
    private static String prefix;

    public static void load(JavaPlugin plugin) throws IOException, FileNotFoundException, InvalidConfigurationException {
        commands.clear();
        InputStream loadConfig;

        try {
            loadConfig = new FileInputStream(new File("plugins/ChatWatchmen/config.yml"));
            loadConfig.close();
        } catch (FileNotFoundException FNFex) {
            InputStream in = plugin.getResource("config.yml");
            OutputStream out = new FileOutputStream(new File("plugins/ChatWatchmen", "config.yml"));
            byte[] buf = new byte[2048];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/ChatWatchmen/config.yml"));

        List<String> tmp = config.getStringList("BlockedCmds");

        for (String s : tmp) {
            commands.add(s.toLowerCase().trim());
        }

        prefix = config.getString("Colors.prefix", "§f§l").replace('&', '§');
    }

    public static List<String> getCommands() {
        return commands;
    }

    public static String getPrefix() {
        return prefix;
    }
}
