package me.yvant;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class yvant extends JavaPlugin implements Listener {

    private FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Offhand] Plugin Enabled - by yvant.exe");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Offhand] Plugin Disabled - by yvant.exe");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (label.equalsIgnoreCase("offhand") || label.equalsIgnoreCase("oh")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (!player.isOp()) return true;

                reloadConfig();
                config = getConfig();
                player.sendMessage(ChatColor.YELLOW + "Plugin Offhand Yvant Reloaded");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
                return true;
            }

            ItemStack mainHand = player.getInventory().getItemInMainHand();
            if (mainHand.getType() == Material.AIR) {
                player.sendMessage(ChatColor.RED + config.getString("messages.no-item"));
                Sound noItemSound = Sound.valueOf(config.getString("sounds.no-item").toUpperCase());
                player.playSound(player.getLocation(), noItemSound, 1f, 1f);
                return true;
            }

            List<String> blocked = config.getStringList("blocked-items");
            if (blocked.contains(mainHand.getType().name().toLowerCase())) {
                player.sendMessage(ChatColor.RED + config.getString("messages.blocked"));
                Sound noItemSound = Sound.valueOf(config.getString("sounds.no-item").toUpperCase());
                player.playSound(player.getLocation(), noItemSound, 1f, 1f);
                return true;
            }

            ItemStack offhandItem = player.getInventory().getItem(EquipmentSlot.OFF_HAND);
            player.getInventory().setItem(EquipmentSlot.OFF_HAND, mainHand);
            player.getInventory().setItemInMainHand(offhandItem);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.transferred")));
            Sound transferSound = Sound.valueOf(config.getString("sounds.transferred").toUpperCase());
            player.playSound(player.getLocation(), transferSound, 1f, 1f);
            return true;
        }

        if (label.equalsIgnoreCase("offhand") && args.length == 1 && args[0].equalsIgnoreCase("help")) {
            player.sendMessage(ChatColor.AQUA + "Offhand Plugin by yvant.exe");
            player.sendMessage(ChatColor.GRAY + "/offhand or /oh - Switch item to offhand");
            player.sendMessage(ChatColor.GRAY + "/offhand reload - Reload config (OP only)");
            player.sendMessage(ChatColor.GRAY + "/offhand help - Show this help message");
            player.playSound(player.getLocation(), Sound.ENTITY_CAT_PURREOW, 1f, 1f);
            return true;
        }

        return false;
    }
}
