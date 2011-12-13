package plugin.arcwolf.neopaintingswitch;

import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingBreakEvent;

public class npPaintingBreakEvent extends EntityListener {

    public void onPaintingBreak(PaintingBreakEvent event) {
        if (event instanceof PaintingBreakByEntityEvent) {
            PaintingBreakByEntityEvent entityBreakEvent = (PaintingBreakByEntityEvent) event;
            if (entityBreakEvent.getRemover() instanceof Player) {
                Player player = (Player) entityBreakEvent.getRemover();
                npSettings settings = npSettings.getSettings(player);
                if (settings.painting != null && settings.painting.getEntityId() == event.getPainting().getEntityId()) {
                    npSettings.playerSettings.get(player.getName()).painting = null;
                    npSettings.playerSettings.get(player.getName()).clicked = false;
                }
            }
        }
        else {
            Set<Entry<String, npSettings>> keys = npSettings.playerSettings.entrySet();
            for(Entry<String, npSettings> set : keys) {
                String playerName = set.getKey();
                if (npSettings.playerSettings.get(playerName).painting != null && npSettings.playerSettings.get(playerName).painting.getEntityId() == event.getPainting().getEntityId()) {
                    npSettings.playerSettings.get(playerName).painting = null;
                    npSettings.playerSettings.get(playerName).clicked = false;
                    return;
                }
            }
        }
    }
}
