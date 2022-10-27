package at.mathh40.prefixsystem.placeholder;

import at.mathh40.prefixsystem.PrefixSystem;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.format.TextColor;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ColorExpansion extends PlaceholderExpansion{
    private final PrefixSystem plugin;

    public ColorExpansion(PrefixSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "prefix";
    }

    @Override
    public @NotNull String getAuthor() {
        return "mathh40";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if(params.equalsIgnoreCase("usercolor")) {
            User user = plugin.api.getPlayerAdapter(Player.class).getUser(player);
            CachedMetaData metaData = user.getCachedData().getMetaData();
            int  color = metaData.getMetaValue("prefixcolor", Integer::parseInt).orElse(TextColor.color(255,255,255).value());
            if(color == 0)
            {
                Group group = plugin.api.getGroupManager().getGroup(user.getPrimaryGroup());
                if (group != null) {
                    // group doesn't exist.
                    metaData = group.getCachedData().getMetaData();
                    color = metaData.getMetaValue("prefixcolor", Integer::parseInt).orElse(TextColor.color(255,255,255).value());
                    return "&" + TextColor.color(color).asHexString();
                }
                else {
                    return "";
                }
            }
            else
            {
                return "&" + TextColor.color(color).asHexString();
            }

        }
        if(params.equalsIgnoreCase("color")) {
            User user = plugin.api.getPlayerAdapter(Player.class).getUser(player);
            Group group = plugin.api.getGroupManager().getGroup(user.getPrimaryGroup());
            if (group != null) {
                // group doesn't exist.
                CachedMetaData metaData = group.getCachedData().getMetaData();
                int  color = metaData.getMetaValue("prefixcolor", Integer::parseInt).orElse(TextColor.color(255,255,255).value());
                return "&" + TextColor.color(color).asHexString();
            }
            else {
                return "";
            }
        }

        return null; // Placeholder is unknown by the Expansion
    }

}
