package fr.tonnom.tntbow;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

public class ExplosiveBowListener implements Listener {

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();

            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();
                ItemStack bow = player.getInventory().getItemInMainHand();

                if (bow.hasItemMeta() && "§cArc TNT".equals(bow.getItemMeta().getDisplayName())) {
                    Location hitLocation = arrow.getLocation();
                    World world = hitLocation.getWorld();

                    if (world != null) {
                        // Force poussée à 25.0F pour un cratère monstrueux
                        world.createExplosion(hitLocation, 25.0F, true, true, player);
                    }
                    arrow.remove();
                }
            }
        }
    }
}
