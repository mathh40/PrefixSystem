package at.mathh40.prefixsystem.commands;

import at.mathh40.prefixsystem.PrefixMenu;
import at.mathh40.prefixsystem.PrefixSystem;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PrefixCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player ) {
            Player player = (Player) sender;

            // obtain a User instance (by any means! see above for other ways)
            User user = PrefixSystem.api.getPlayerAdapter(Player.class).getUser(player);
            if(args.length == 1 && args[0].equals("reset"))
            {
                user.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals("prefixcolor")));
                PrefixSystem.api.getUserManager().saveUser(user);
            }
            else{
                PrefixMenu menu = new PrefixMenu(player);
                menu.openInventory(player);
            }

            return true;
            }

        return false;
    }
}
