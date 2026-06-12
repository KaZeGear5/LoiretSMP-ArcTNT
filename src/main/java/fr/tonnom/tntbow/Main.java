package fr.tonnom.tntbow;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ExplosiveBowListener(), this);
        this.getCommand("givetntbow").setExecutor(this);
        
        // Enregistrement du craft personnalisé
        registerTNTBowRecipe();
        
        getLogger().info("Plugin TNTBow active avec succes !");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.getInventory().addItem(createTNTBow());
            player.sendMessage("§a[TNTBow] Tu as recu l'Arc TNT !");
            return true;
        }
        sender.sendMessage("Seul un joueur peut executer cette commande.");
        return true;
    }

    // Fonction qui crée l'arc avec le nom en dégradé et le CustomModelData
    public ItemStack createTNTBow() {
        ItemStack tntBow = new ItemStack(Material.BOW);
        ItemMeta meta = tntBow.getItemMeta();

        if (meta != null) {
            // Dégradé du Rouge (red) vers le Noir (black) en Gras (b)
            var mm = MiniMessage.miniMessage();
            meta.displayName(mm.deserialize("<b><gradient:red:black>Arc TNT</gradient></b>"));
            
            meta.setCustomModelData(12345);
            tntBow.setItemMeta(meta);
        }
        return tntBow;
    }

    // Fonction pour le Craft
    private void registerTNTBowRecipe() {
        NamespacedKey key = new NamespacedKey(this, "tnt_bow");
        ShapedRecipe recipe = new ShapedRecipe(key, createTNTBow());

        // Grille de craft : T = TNT, B = Arc (Bow)
        recipe.shape("TTT", "TBT", "TTT");
        recipe.setIngredient('T', Material.TNT);
        recipe.setIngredient('B', Material.BOW);

        Bukkit.addRecipe(recipe);
    }
}
