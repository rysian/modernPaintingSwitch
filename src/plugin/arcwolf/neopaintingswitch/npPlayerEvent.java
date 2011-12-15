package plugin.arcwolf.neopaintingswitch;

import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.Art;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

public class npPlayerEvent extends PlayerListener {

    private neoPaintingSwitch plugin;

    public npPlayerEvent(neoPaintingSwitch plugin) {
        this.plugin = plugin;
    }

    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity instanceof Painting && plugin.playerCanUseCommand(event.getPlayer(), "neopaintingswitch.use")) {
            Player player = event.getPlayer();
            Set<Entry<String, npSettings>> keys = npSettings.playerSettings.entrySet();
            for(Entry<String, npSettings> set : keys) {
                String playerName = set.getKey();
                if (npSettings.playerSettings.get(playerName).painting != null && npSettings.playerSettings.get(playerName).painting.getEntityId() == entity.getEntityId() && !playerName.equals(player.getName())) {
                    player.sendMessage(ChatColor.RED + playerName + " is already editing this painting.");
                    return;
                }
            }
            npSettings settings = npSettings.getSettings(player);
            settings.block = player.getTargetBlock(null, 100);
            settings.painting = (Painting) entity;
            if (settings.clicked) {
                player.sendMessage(ChatColor.RED + "Painting locked");
                npSettings.playerSettings.get(player.getName()).painting = null;
                npSettings.playerSettings.get(player.getName()).block = null;
                npSettings.playerSettings.get(player.getName()).clicked = false;
            }
            else {
                player.sendMessage(ChatColor.GREEN + "Scroll to change painting");
                settings.clicked = true;
            }
        }
    }

    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        npSettings settings = npSettings.getSettings(player);
        try {
            if (settings.block != null && settings.clicked && !settings.block.equals(player.getTargetBlock(null, 100))) {
                player.sendMessage(ChatColor.RED + "Painting locked");
                npSettings.playerSettings.get(player.getName()).painting = null;
                npSettings.playerSettings.get(player.getName()).block = null;
                npSettings.playerSettings.get(player.getName()).clicked = false;
            }
        } catch (Exception e){
            // Do nothing
        }
    }

    public void onItemHeldChange(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        npSettings settings = npSettings.getSettings(player);
        int previousSlot = event.getPreviousSlot();
        int newSlot = event.getNewSlot();
        boolean reverse = (previousSlot - newSlot) > 0;
        if (((previousSlot == 0) && (newSlot == 8)) || ((previousSlot == 8) && (newSlot == 0))) {
            reverse = !reverse;
        }
        if (settings.clicked && settings.painting != null && settings.block != null && !reverse) {
            Painting painting = settings.painting;
            Art[] art = Art.values();
            int currentID = painting.getArt().getId();
            if (currentID == art.length - 1) {
                int count = 0;
                while (!painting.setArt(art[count])) {
                    if (count == art.length - 1) break;
                    count++;
                }
            }
            else {
                int count = painting.getArt().getId();
                int tempCount = count;
                count++;
                while (!painting.setArt(art[count])) {
                    if (count == art.length - 1)
                        count = 0;
                    else
                        count++;
                    if (count == tempCount) break;
                }
            }
        }
        else if (settings.clicked && settings.painting != null && settings.block != null && reverse) {
            Painting painting = settings.painting;
            Art[] art = Art.values();
            int currentID = painting.getArt().getId();
            if (currentID == 0) {
                int count = art.length - 1;
                while (!painting.setArt(art[count])) {
                    count--;
                    if (count == 0) break;
                }
            }
            else {
                int count = painting.getArt().getId();
                int tempCount = count;
                count--;
                while (!painting.setArt(art[count])) {
                    if (count == 0)
                        count = art.length - 1;
                    else
                        count--;
                    if (count == tempCount) break;
                }
            }
        }
    }
}
