package at.mathh40.prefixsystem;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class PrefixMenu implements Listener {
    private final Gui gui;
    private final Player ply;
    private TextColor tcolor;
    public PrefixMenu(Player ply) {
        this.ply = ply;

        gui = Gui.gui()
                .title(Component.text("Prefix Color"))
                .rows(3)
                .create();


        initializeItems();
        gui.disableItemDrop();
        gui.disableItemPlace();
        gui.disableItemTake();
        gui.disableItemSwap();
    }

    // You can call this whenever you want to put the items in
    public void initializeItems() {

        User user = PrefixSystem.api.getPlayerAdapter(Player.class).getUser(ply);
        CachedMetaData metaData = user.getCachedData().getMetaData();
        int  c = metaData.getMetaValue("prefixcolor", Integer::parseInt).orElse(TextColor.color(255,255,255).value());

        gui.getFiller().fill(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());
        gui.setItem(0,new GuiItem(createGuiItem(Material.STONE_BUTTON, Component.text("Red Value"), Component.text(""))));
        gui.setItem(8,new GuiItem(createGuiItem(Material.STONE_BUTTON, Component.text("Red Value"), Component.text(""))));
        gui.setItem(18,new GuiItem(createGuiItem(Material.STONE_BUTTON, Component.text("Red Value"), Component.text(""))));
        gui.setItem(26,new GuiItem(createGuiItem(Material.STONE_BUTTON, Component.text("Red Value"), Component.text(""))));

        tcolor = TextColor.color(c);
        gui.setItem(11,new GuiItem(createGuiItem(Material.RED_DYE, Component.text("Red Value"), Component.text(tcolor.red()).color(TextColor.color(255,0,0))), this::ClickRed));
        gui.setItem(12,new GuiItem(createGuiItem(Material.GREEN_DYE, Component.text("Green Value"), Component.text(tcolor.green()).color(TextColor.color(0,255,0))), this::ClickGreen));
        gui.setItem(13,new GuiItem(createGuiItem(Material.BLUE_DYE, Component.text("Blue Value"), Component.text(tcolor.blue()).color(TextColor.color(0,0,255))), this::ClickBlue));
        TextComponent comp = Component.text("Set Color ").append(Component.text(PlaceholderAPI.setPlaceholders(ply,"%vault_prefix%")).color(tcolor));
        gui.setItem(15,new GuiItem(createGuiItem(Material.ITEM_FRAME, comp , Component.text(tcolor.asHexString())), this::SetColor));
        gui.setItem(16,new GuiItem(createGuiItem(Material.BARRIER, Component.text("Exit"), Component.text("0")), this::menuClose));
    }

    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createGuiItem(final Material material ,final TextComponent name, final TextComponent... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.displayName(name);

        // Set the lore of the item
        meta.lore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        gui.open(ent);
    }

    // Check for clicks on items

    public void ClickRed(final InventoryClickEvent e) {
        int count = count(e);

        tcolor = TextColor.color(tcolor.red() + count, tcolor.green(), tcolor.blue());
        gui.updateItem(11,createGuiItem(Material.RED_DYE, Component.text("Red Value"), Component.text(tcolor.red()).color(TextColor.color(255,0,0))));
        TextComponent comp = Component.text("Set Color ").append(Component.text(PlaceholderAPI.setPlaceholders(ply,"%vault_prefix%")).color(tcolor));
        gui.updateItem(15,createGuiItem(Material.ITEM_FRAME, comp , Component.text(tcolor.asHexString())));
    }

    public void ClickBlue(final InventoryClickEvent e) {
        int count = count(e);

        tcolor = TextColor.color(tcolor.red(), tcolor.green(), tcolor.blue() + count);

        gui.updateItem(13,createGuiItem(Material.BLUE_DYE, Component.text("Blue Value"), Component.text(tcolor.blue()).color(TextColor.color(0,0,255))));
        TextComponent comp = Component.text("Set Color ").append(Component.text(PlaceholderAPI.setPlaceholders(ply,"%vault_prefix%")).color(tcolor));
        gui.updateItem(15,createGuiItem(Material.ITEM_FRAME, comp , Component.text(tcolor.asHexString())));
    }

    public void ClickGreen(final InventoryClickEvent e) {
        int count = count(e);

        tcolor = TextColor.color(tcolor.red(), tcolor.green() + count, tcolor.blue());
        gui.updateItem(12,createGuiItem(Material.GREEN_DYE, Component.text("Green Value"), Component.text(tcolor.green()).color(TextColor.color(0,255,0))));
        TextComponent comp = Component.text("Set Color ").append(Component.text(PlaceholderAPI.setPlaceholders(ply,"%vault_prefix%")).color(tcolor));
        gui.updateItem(15,createGuiItem(Material.ITEM_FRAME, comp , Component.text(tcolor.asHexString())));
    }

    public void SetColor(final InventoryClickEvent e) {
        User user = PrefixSystem.api.getPlayerAdapter(Player.class).getUser(ply);
        // create a new MetaNode holding the level value
        // of course, this can have context/expiry/etc too!
        MetaNode node = MetaNode.builder("prefixcolor", Integer.toString(tcolor.value())).build();

        // clear any existing meta nodes with the same key - we want to override
        user.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals("prefixcolor")));
        // add the new node
        user.data().add(node);

        // save!
        PrefixSystem.api.getUserManager().saveUser(user);
    }

    public void menuClose(final InventoryClickEvent e) {
        gui.close(ply);
    }

    private int count(InventoryClickEvent e) {
        int count = 0;
        if(e.getClick().isLeftClick())
        {
            if(e.getClick().isShiftClick())
            {
                count = 10;
            }
            else {
                count = 1;
            }
        }
        if(e.getClick().isRightClick())
        {
            if(e.getClick().isShiftClick())
            {
                count = -10;
            }
            else {
                count = -1;
            }

        }
        return count;
    }
}
