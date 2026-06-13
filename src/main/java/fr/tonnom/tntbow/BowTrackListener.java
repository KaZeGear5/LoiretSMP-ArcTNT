package fr.tonnom.tntbow;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class BowTrackListener implements Listener {

    private final Main plugin;

    public BowTrackListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        for (ItemStack drop : event.getDrops()) {
            if (Main.isTNTBow(drop)) {
                Bukkit.broadcastMessage("§6[TNTBow] §e" + event.getEntity().getName()
                    + " §cest mort ! L'§cArc TNT§c a été droppé au sol, récupérez-le vite !");
                Main.setBowOwner(null);
                Main.saveBowState(plugin);
                break;
            }
        }
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {
        ItemStack item = event.getEntity().getItemStack();
        if (!Main.isTNTBow(item)) return;

        String owner = Main.getBowOwner();
