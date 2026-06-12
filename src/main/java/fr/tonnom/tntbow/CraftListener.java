package fr.tonnom.tntbow;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;

public class CraftListener implements Listener {

    private final Main plugin;

    public CraftListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();
        ItemMeta meta = result.getItemMeta();

        if (meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 12345) {
            if (event.getWhoClicked() instanceof Player player) {
                plugin.getServer().broadcastMessage(
                    "§6" + player.getName() + " §aa craft le §cTNT Bow §a!"
                );
            }
        }
    }
}
