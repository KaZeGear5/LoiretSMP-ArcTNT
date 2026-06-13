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
            if (Main.isBowExists()) {
                event.setCancelled(true);
                if (event.getWhoClicked() instanceof Player player) {
                    player.sendMessage("§c[TNTBow] L'Arc TNT existe déjà dans le monde, il ne peut pas être recrafté !"
                        + (Main.getBowOwner() != null ? " Il appartient à §6" + Main.getBowOwner() + "§c." : ""));
                }
                return;
            }
            if (event.getWhoClicked() instanceof Player player) {
                Main.setBowExists(true);
                Main.setBowOwner(player.getName());
                plugin.getServer().broadcastMessage(
                    "§6[TNTBow] §a" + player.getName() + " §aa crafté l'§cArc TNT§a !"
                );
                plugin.getLogger().info("[TNTBow] Arc TNT crafté par : " + player.getName());
            }
        }
    }
}
