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
        // Enregistre l'évenement de tir à l'arc
        getServer().getPluginManager().registerEvents(new ExplosiveBowListener(), this);
        // Enregistre la commande /givetntbow
        this.getCommand("givetntbow").setExecutor(this);
        getLogger().info("Plugin TNTBow activé avec succès !");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            
            ItemStack tntBow = new ItemStack(Material.BOW);
            ItemMeta meta = tntBow.getItemMeta();

            if (meta != null) {
                meta.setDisplayName("§cArc TNT"); 
                meta.setCustomModelData(12345); // Pour ton Resource Pack
                tntBow.setItemMeta(meta);
            }

            player.getInventory().addItem(tntBow);
            player.sendMessage("§a[TNTBow] Tu as reçu l'Arc TNT !");
            return true;
        }
        sender.sendMessage("Seul un joueur peut exécuter cette commande.");
        return true;
    }
}
