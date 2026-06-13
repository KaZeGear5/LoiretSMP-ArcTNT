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
        Main.setBowExists(false);
        Main.setBowOwner(null);
        Main.saveBowState(plugin);

        double y = event.getEntity().getLocation().getY();

        if (y < 0) {
            Bukkit.broadcastMessage("§4§l[TNTBow] §cL'Arc TNT"
                + (owner != null ? " de §6" + owner + " §c" : " ")
                + "est tombé dans le vide... Il est perdu à tout jamais !");
        } else {
            Bukkit.broadcastMessage("§4§l[TNTBow] §cL'Arc TNT"
                + (owner != null ? " de §6" + owner + " §c" : " ")
                + "a disparu du sol... Il est perdu à tout jamais !");
        }

        plugin.getLogger().info("[TNTBow] Arc TNT disparu. Ancien propriétaire : " + (owner != null ? owner : "inconnu"));
    }

    @EventHandler
    public void onItemBurn(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Item item)) return;
        if (!Main.isTNTBow(item.getItemStack())) return;

        EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause == EntityDamageEvent.DamageCause.FIRE
            || cause == EntityDamageEvent.DamageCause.FIRE_TICK
            || cause == EntityDamageEvent.DamageCause.LAVA) {

            String owner = Main.getBowOwner();

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (!item.isValid()) {
                    Main.setBowExists(false);
                    Main.setBowOwner(null);
                    Main.saveBowState(plugin);

                    String causeStr = (cause == EntityDamageEvent.DamageCause.LAVA)
                        ? "dans la lave" : "dans le feu";

                    Bukkit.broadcastMessage("§4§l[TNTBow] §cL'Arc TNT"
                        + (owner != null ? " de §6" + owner + " §c" : " ")
                        + "a brûlé " + causeStr + "... Il est détruit à tout jamais !");

                    plugin.getLogger().info("[TNTBow] Arc TNT détruit. Ancien propriétaire : " + (owner != null ? owner : "inconnu"));
                }
            }, 20L);
        }
    }
}
