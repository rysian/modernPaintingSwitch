package plugin.arcwolf.neopaintingswitch;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;

public class npSettings {

    public static Map<String, npSettings> playerSettings = new HashMap<String, npSettings>();
    //command used
    public boolean clicked = false;
    public Block block = null;
    public Painting painting = null;
    public Painting previousPainting = null;
    public Location location = null;

    public static npSettings getSettings(Player player) {
        npSettings settings = (npSettings) playerSettings.get(player.getName());
        if (settings == null) {
            playerSettings.put(player.getName(), new npSettings());
            settings = (npSettings) playerSettings.get(player.getName());
        }
        return (settings);
    }

    public static void clear(String playerName) {
        playerSettings.get(playerName).painting = null;
        playerSettings.get(playerName).block = null;
        playerSettings.get(playerName).clicked = false;
        playerSettings.get(playerName).location = null;
    }

    public static void clear(Player player) {
        clear(player.getName());
    }
}
