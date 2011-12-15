package plugin.arcwolf.neopaintingswitch;

import java.util.Hashtable;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;


public class npSettings {
    public static Hashtable<String, npSettings> playerSettings = new Hashtable<String, npSettings>();
    //command used
    public boolean clicked = false;
    public Block block = null;
    public Painting painting = null;
    public Location location = null;
    
    public static npSettings getSettings(Player player){
        npSettings settings = (npSettings) playerSettings.get(player.getName());
        if (settings == null) {
            playerSettings.put(player.getName(), new npSettings());
            settings = (npSettings) playerSettings.get(player.getName());
        }
        return (settings);
    }
}
