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

    private static boolean bowExists = false;
    private static String bowOwner = null;

    public static boolean isBowExists() { return bowExists; }
    public static void setBowExists(boolean value) { bowExists = value; }
    public static String getBowOwner() { return bowOwner; }
    public static void setBowOwner(String name) { bowOwner = name; }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadBowState();

        getServer().getPluginManager().registerEvents(new ExplosiveBowListener(), this);
        getServer().getPluginManager().registerEvents(new CraftListener(this), this);
        getServer().getPluginManager().registerEvents(new BowTrackListener(this), this);
        this.getCommand("givetntbow").setExecutor(this);
        registerTNTBowRecipe();
        getLogger().info("Plugin TNTBow active avec succes !");
    }

    private void loadBowState() {
        bowExists = getConfig().getBoolean("bow.exists", false);
        bowOwner = getConfig().getString("bow.owner", null);
        if (bowOwner != null && bowOwner.equals("null")) bowOwner = null;
        getLogger().info("[TNTBow] État chargé — Arc existant : " + bowExists + " | Propriétaire : " + (bowOwner != null ? bowOwner : "aucun"));
    }

    public static void saveBowState(JavaPlugin plugin) {
        plugin.getConfig().set("bow.exists", bowExists);
        plugin.getConfig().set("bow.owner", bowOwner != null ? bowOwner : "null");
        plugin.saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (bowExists) {
                player.sendMessage("§c[TNTBow] L'Arc TNT existe déjà dans le monde !"
                    + (bowOwner != null ? " Il appartient à §6" + bowOwner + "§c." : ""));
                return true;
            }
            player.getInventory().addItem(createTNTBow());
            bowExists = true;
            bowOwner = player.getName();
            saveBowState(this);
            Bukkit.broadcastMessage("§6[TNTBow] §a" + player.getName() + " §aa reçu l'§cArc TNT§a !");
            getLogger().info("[TNTBow] Arc TNT donné à : " + player.getName());
            return true;
        }
        sender.sendMessage("Seul un joueur peut executer cette commande.");
        return true;
    }

    public ItemStack createTNTBow() {
        ItemStack tntBow = new ItemStack(Material.BOW);
        ItemMeta meta = tntBow.getItemMeta();
        if (meta != null) {
            var mm = MiniMessage.miniMessage();
            meta.displayName(mm.deserialize("<b><gradient:red:black>Arc TNT</gradient></b>"));
            meta.setCustomModelData(12345);
            tntBow.setItemMeta(meta);
        }
        return tntBow;
    }

    public static boolean isTNTBow(ItemStack item) {
        if (item == null || item.getType() != Material.BOW) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 12345;
    }

    private void registerTNTBowRecipe() {
        NamespacedKey key = new NamespacedKey(this, "tnt_bow_special");
        ShapedRecipe recipe = new ShapedRecipe(key, createTNTBow());
        recipe.shape("SBS", "BTB", "SBS");
        recipe.setIngredient('T', Material.TNT);
        recipe.setIngredient('B', Material.BOW);
        recipe.setIngredient('S', Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE);
        Bukkit.addRecipe(recipe);
    }
}
