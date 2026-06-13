package fr.tonnom.tntbow;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class BowTrackListener implements Listener {

    private final Main plugin;

    public BowTrackListener(Main plugin) {
        this.plugin = plugin;
    }

    // Quand le joueur meurt et drop l'arc
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        for (ItemStack drop : event.getDrops()) {
            if (Main.isTNTBow(drop)) {
                String owner = Main.getBowOwner();
                Bukkit.broadcastMessage("§6[TNTBow] §e" + event.getEntity().getName()
                    + " §cest mort ! L'§cArc TNT§c a été droppé au sol, récupérez-le vite !");
                // Le propriétaire change : l'arc est "sans maître" jusqu'à récupération
                Main.setBowOwner(null);
                Main.saveBowState(plugin);
                break;
            }
        }
    }

    // Quand l'arc disparaît (vide, feu, lave, despawn naturel)
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
            // Despawn naturel (5 minutes au sol)
            Bukkit.broadcastMessage("§4§l[TNTBow] §cL'Arc TNT"
                + (owner != null ? " de §6" + owner + " §c" : " ")
                + "a disparu du sol... Il est perdu à tout jamais !");
        }

        plugin.getLogger().info("[TNTBow] Arc TNT disparu. Ancien propriétaire : " + (owner != null ? owner : "inconnu"));
    }

    // Quand l'arc brûle dans le feu ou la lave
    @EventHandler
    public void onItemBurn(org.bukkit.event.entity.EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Item item)) return;
        if (!Main.isTNTBow(item.getItemStack())) return;

        org.bukkit.event.entity.EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause == org.bukkit.event.entity.EntityDamageEvent.DamageCause.FIRE
            || cause == org.bukkit.event.entity.EntityDamageEvent.DamageCause.FIRE_TICK
            || cause == org.bukkit.event.entity.EntityDamageEvent.DamageCause.LAVA) {

            String owner = Main.getBowOwner();

            // On attend qu'il soit vraiment détruit (item a 0 HP)
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (!item.isValid()) {
                    Main.setBowExists(false);
                    Main.setBowOwner(null);
                    Main.saveBowState(plugin);

                    String cause_str = (cause == org.bukkit.event.entity.EntityDamageEvent.DamageCause.LAVA)
                        ? "dans la lave" : "dans le feu";

                    Bukkit.broadcastMessage("§4§l[TNTBow] §cL'Arc TNT"
                        + (owner != null ? " de §6" + owner + " §c" : " ")
                        + "a brûlé " + cause_str + "... Il est détruit à tout jamais !");

                    plugin.getLogger().info("[TNTBow] Arc TNT détruit par " + cause_str + ". Ancien propriétaire : " + (owner != null ? owner : "inconnu"));
                }
            }, 20L);
        }
    }
}
