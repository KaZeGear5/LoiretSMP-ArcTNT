package fr.tonnom.tntbow;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;

public class ExplosiveBowListener implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();

            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();
                ItemStack bow = player.getInventory().getItemInMainHand();

                if (bow.hasItemMeta() && bow.getItemMeta().hasCustomModelData() && bow.getItemMeta().getCustomModelData() == 12345) {
                    Location hitLocation = arrow.getLocation();
                    World world = hitLocation.getWorld();

                    if (world != null) {
                        // 1. Un éclair frappe le point d'impact pour le style
                        world.strikeLightningEffect(hitLocation);

                        // 2. L'explosion principale (Puissance 18.0F : idéal pour un trou géant sans crash)
                        world.createExplosion(hitLocation, 18.0F, true, true, player);

                        // 3. Pluie de TNT bonus : Fait apparaître 5 TNT allumées qui volent aux alentours
                        for (int i = 0; i < 5; i++) {
                            TNTPrimed tnt = world.spawn(hitLocation, TNTPrimed.class);
                            tnt.setFuseTicks(20 + random.nextInt(20)); // Explose entre 1 et 2 secondes après
                            
                            // Propulsion de la TNT dans une direction aléatoire
                            double x = (random.nextDouble() - 0.5) * 1.5;
                            double y = random.nextDouble() * 1.2;
                            double z = (random.nextDouble() - 0.5) * 1.5;
                            tnt.setVelocity(new Vector(x, y, z));
                        }
                    }
                    // Supprime la flèche pour éviter les doubles explosions
                    arrow.remove();
                }
            }
        }
    }
}
