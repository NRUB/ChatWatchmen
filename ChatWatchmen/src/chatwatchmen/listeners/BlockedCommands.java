package chatwatchmen.listeners;

import chatwatchmen.system.Config;
import chatwatchmen.system.Lang;
import static org.bukkit.Bukkit.getServer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 *
 * @author NRUB
 */
public class BlockedCommands implements Listener {

    @EventHandler
    public void onRestrictedCommand(PlayerCommandPreprocessEvent PCPevent) {
        for (String cmd : Config.getCommands()) {
            if (isFit(PCPevent.getMessage(), cmd)) {
                getServer().getConsoleSender().sendMessage(ChatColor.RED + PCPevent.getPlayer().getName().replaceAll("§.", "").replaceAll("&.", "") + ": " + PCPevent.getMessage());
                PCPevent.setMessage("/helpop " + PCPevent.getPlayer().getName().replaceAll("§.", "").replaceAll("&.", "") + ": " + PCPevent.getMessage());
                PCPevent.getPlayer().kickPlayer(Lang.getMessage("KM"));
            }
        }
    }

    private boolean isFit(String message, String template) {
        if (message.length() >= template.length()) {
            for (int i = 0; i < template.length(); i++) {
                String m = message.charAt(i) + "";
                String t = template.charAt(i) + "";
                if (!m.equalsIgnoreCase(t)) {
                    return false;
                }
            }
            if (message.length() == template.length()) {
                return true;
            }
            if (message.charAt(template.length()) == ' ') {
                return true;
            }
        }
        return false;
    }
}
