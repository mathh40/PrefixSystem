package at.mathh40.prefixsystem;

import at.mathh40.prefixsystem.commands.PrefixCommand;
import at.mathh40.prefixsystem.placeholder.ColorExpansion;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class PrefixSystem extends JavaPlugin {
    public static LuckPerms api;
    @Override
    public void onEnable() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();

        }

        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            /*
             * We register the EventListener here, when PlaceholderAPI is installed.
             * Since all events are in the main class (this class), we simply use "this"
             */

            new ColorExpansion(this).register();
        } else {
            /*
             * We inform about the fact that PlaceholderAPI isn't installed and then
             * disable this plugin to prevent issues.
             */
            getLogger().log(Level.WARNING,"Could not find PlaceholderAPI! This plugin is required.");
            this.getServer().getPluginManager().disablePlugin(this);
        }

        this.getCommand("prefix").setExecutor(new PrefixCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
