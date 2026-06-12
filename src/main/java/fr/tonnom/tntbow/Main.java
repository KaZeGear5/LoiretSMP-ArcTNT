package fr.tonnom.tntbow;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ExplosiveBowListener(), this);
        this.getCommand("givetntbow").setExecutor(this);
        getLogger().info("Plugin TNTBow active avec succes !");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            
            ItemStack tntBow = new ItemStack(Material.BOW);
            ItemMeta meta = tntBow.getItemMeta();

            if (meta != null) {
                meta.setDisplayName("§cArc TNT"); 
                meta.setCustomModelData(12345); // Active la texture personnalisée
                tntBow.setItemMeta(meta);
            }

            player.getInventory().addItem(tntBow);
            player.sendMessage("§a[TNTBow] Tu as recu l'Arc TNT Surpuissant !");
            return true;
        }
        sender.sendMessage("Seul un joueur peut executer cette commande.");
        return true;
    }
}
