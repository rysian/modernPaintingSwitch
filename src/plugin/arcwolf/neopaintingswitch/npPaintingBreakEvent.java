package plugin.arcwolf.neopaintingswitch;

import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

public class npPaintingBreakEvent implements Listener {

    @EventHandler
    public void onPaintingBreak(HangingBreakEvent event) {
        if (event.isCancelled())
            return;
        Set<Entry<String, npSettings>> keys = npSettings.playerSettings.entrySet();
        if (event instanceof HangingBreakByEntityEvent) {
            HangingBreakByEntityEvent entityBreakEvent = (HangingBreakByEntityEvent) event;
            if (entityBreakEvent.getRemover() instanceof Player) {
                Player player = (Player) entityBreakEvent.getRemover();
                npSettings settings = npSettings.getSettings(player);
                if (settings.painting != null && settings.painting.getEntityId() == event.getEntity().getEntityId()) {
                    npSettings.playerSettings.get(player.getName()).painting = null;
                    npSettings.playerSettings.get(player.getName()).block = null;
                    npSettings.playerSettings.get(player.getName()).location = null;
                    npSettings.playerSettings.get(player.getName()).clicked = false;
                }
                else {
                    for(Entry<String, npSettings> set : keys) {
                        String playerName = set.getKey();
                        if (npSettings.playerSettings.get(playerName).painting != null && npSettings.playerSettings.get(playerName).painting.getEntityId() == event.getEntity().getEntityId() && !playerName.equals(player.getName())) {
                            player.sendMessage(ChatColor.RED + "This painting is being edited by " + ChatColor.WHITE + set.getKey());
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
        else {
            for(Entry<String, npSettings> set : keys) {
                String playerName = set.getKey();
                if (npSettings.playerSettings.get(playerName).painting != null && npSettings.playerSettings.get(playerName).painting.getEntityId() == event.getEntity().getEntityId()) {
                    npSettings.playerSettings.get(playerName).painting = null;
                    npSettings.playerSettings.get(playerName).block = null;
                    npSettings.playerSettings.get(playerName).location = null;
                    npSettings.playerSettings.get(playerName).clicked = false;
                    return;
                }
            }
        }
    }
}
