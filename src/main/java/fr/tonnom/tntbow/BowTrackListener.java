package fr.tonnom.tntbow;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.inventory.ItemStack;

public class BowTrackListener implements Listener {

    private final Main plugin;

    public BowTrackListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {
        ItemStack item = event.getEntity().getItemStack();
        if (Main.isTNTBow(item)) {
            if (event.getEntity().getLocation().getY() < 0) {
                String owner = Main.getBowOwner();
                Main.setBowExists(false);
                Main.setBowOwner(null);
                Main.saveBowState(plugin);
                Bukkit.broadcastMessage("§4§l[TNTBow] §cL'Arc TNT"
                    + (owner != null ? " de §6" + owner + " §c" : " ")
                    + "est tombé dans le vide... Il est perdu à tout jamais et ne peut plus être utilisé !");
                plugin.getLogger().info("[TNTBow] Arc TNT perdu dans le vide. Ancien propriétaire : " + (owner != null ? owner : "inconnu"));
            }
        }
    }
}
