package plugin.arcwolf.neopaintingswitch;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.nijikokun.bukkit.Permissions.Permissions;

public class neoPaintingSwitch extends JavaPlugin {

    private GroupManager groupManager;
    private Permissions permissionsPlugin;
    private PermissionsEx permissionsExPlugin;

    private boolean isError = false;
    public boolean free4All = false;
    private boolean permissionsSet = false;
    private Server server;

    public static final Logger LOGGER = Logger.getLogger("Minecraft.neoPaintingSwitch");

    @Override
    public void onEnable() {
        server = this.getServer();
        PluginDescriptionFile pdfFile = getDescription();
        PluginManager pm = getServer().getPluginManager();
        setupConfig();
        getPermissionsPlugin();

        pm.registerEvent(Type.PLAYER_INTERACT_ENTITY, new npPlayerEvent(this), Priority.Normal, this);
        pm.registerEvent(Type.PAINTING_BREAK, new npPaintingBreakEvent(), Priority.Normal, this);
        pm.registerEvent(Type.PLAYER_MOVE, new npPlayerEvent(this), Priority.Normal, this);
        pm.registerEvent(Type.PLAYER_ITEM_HELD, new npPlayerEvent(this), Priority.Normal, this);

        LOGGER.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
    }

    @Override
    public void onDisable() {
        PluginDescriptionFile pdfFile = getDescription();
        LOGGER.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled!");
    }

    public void setupConfig()
    {
        File configFile = new File(this.getDataFolder() + "/config.yml");
        FileConfiguration config = this.getConfig();
        if (!configFile.exists())
        {
            config.set("free4All", Boolean.valueOf(false));
            try
            {
                config.save(configFile);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        free4All = config.getBoolean("free4All", false);
    }

    //test for a players permissions
    public boolean playerCanUseCommand(Player player, String command) {
        getPermissionsPlugin();
        if(free4All){
            return true;
        }
        else if (groupManager != null) {
            return groupManager.getWorldsHolder().getWorldPermissions(player).has(player, command);
        }
        else if (permissionsPlugin != null) {
            return (Permissions.Security.permission(player, command));
        }
        else if (permissionsExPlugin != null) {
            return (PermissionsEx.getPermissionManager().has(player, command));
        }
        else if (player.hasPermission(command)) {
            return true;
        }
        else if (isError && player.isOp()) {
            return true;
        }
        else {
            return false;
        }
    }

    // permissions plugin enabled test
    private void getPermissionsPlugin() {
        if (server.getPluginManager().getPlugin("GroupManager") != null) {
            Plugin p = server.getPluginManager().getPlugin("GroupManager");
            if (!permissionsSet) {
                LOGGER.info("GroupManager detected, neoPaintingSwitch permissions enabled...");
                permissionsSet = true;
            }
            groupManager = (GroupManager) p;
        }
        else if (server.getPluginManager().getPlugin("Permissions") != null) {
            Plugin p = server.getPluginManager().getPlugin("Permissions");
            if (!permissionsSet) {
                LOGGER.info("Permissions detected, neoPaintingSwitch permissions enabled...");
                permissionsSet = true;
            }
            permissionsPlugin = (Permissions) p;
        }
        else if (server.getPluginManager().getPlugin("PermissionsBukkit") != null) {
            if (!permissionsSet) {
                LOGGER.info("Bukkit permissions detected, neoPaintingSwitch permissions enabled...");
                permissionsSet = true;
            }
        }
        else if (server.getPluginManager().getPlugin("PermissionsEx") != null) {
            Plugin p = server.getPluginManager().getPlugin("PermissionsEx");
            if (!permissionsSet) {
                LOGGER.info("PermissionsEx detected, neoPaintingSwitch permissions enabled...");
                permissionsSet = true;
            }
            permissionsExPlugin = (PermissionsEx) p;
        }
        else if(free4All){
            if (!permissionsSet) {
                LOGGER.info("No Permissions, neoPaintingSwitch freeForAll enabled...");
                permissionsSet = true;
            }
        }
        else {
            if (!(isError)) {
                LOGGER.info("Permissions not detected, neoPaintingSwitch in OPs mode...");
                isError = true;
            }
        }
    }
}
