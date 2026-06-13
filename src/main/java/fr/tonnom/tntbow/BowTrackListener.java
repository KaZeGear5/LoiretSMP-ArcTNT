package fr.tonnom.tntbow;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class BowTrackListener implements Listener {

    private final Main plugin;

    public BowTrackListener(Main plugin) {
        this.plugin = plugin;
    }

    // Quand un joueur meurt : l'arc drop normalement, rien à faire
    // Mais on surveille si l'arc tombe dans le vide (ItemDespawnEvent avec y < 0)
    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {
        ItemStack item = event.getEntity().getItemStack();
        if (Main.isTNTBow(item)) {
            // L'item despawn = soit tombé dans le vide, soit temps écoulé
            if (event.getEntity().getLocation().getY() < 0) {
                Main.setBowExists(false);
                Bukkit.broadcastMessage("§4§l[TNTBow] §cL'Arc TNT est tombé dans le vide... Il est perdu à tout jamais et ne peut plus être utilisé !");
            }
        }
    }
}
