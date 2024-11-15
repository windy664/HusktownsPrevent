package org.windy.husktownsPrevent;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import me.clip.placeholderapi.PlaceholderAPI;

import java.util.HashMap;
import java.util.Map;

public final class HusktownsPrevent extends JavaPlugin implements Listener {

    private String cannotUseItem;
    private boolean debug;
    private Map<Player, Integer> moveCancelCountMap = new HashMap<>(); // 记录每个玩家被取消的移动次数
    private String spawnCommand; // 存储 spawn 命令

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfigValues();
        String version = this.getDescription().getVersion();
        String serverName = this.getServer().getName();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getPluginManager().registerEvents(this, this);
            this.getServer().getConsoleSender().sendMessage("v" + "§a" + version + "运行环境：§e " + serverName + "\n");
        } else {
            getLogger().warning("你未安装前置PlaceholderAPI！插件没法启动！");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private boolean isItemDisabled(Material itemType) {
        FileConfiguration config = getConfig();
        return config.getStringList("disabledItems").contains(itemType.name());
    }

    private void loadConfigValues() {
        FileConfiguration config = getConfig();
        cannotUseItem = config.getString("messages", "[✘] 你没有权限在这个城镇使用这个物品");
        spawnCommand = config.getString("commands.spawn", "spawn %player_name%"); // 获取 spawn 命令
        debug = config.getBoolean("debug", false);
    }

    private void log(String messages) {
        if (debug) {
            Bukkit.getConsoleSender().sendMessage(messages);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // 判断玩家是否进入城镇区域
        String notprvent = PlaceholderAPI.setPlaceholders(player, "%husktowns_current_location_can_open_containers%");

        if (notprvent.equals("no")) {  // 玩家进入城镇
            // 获取玩家手持的物品
            ItemStack heldItem = player.getInventory().getItemInMainHand();
            if (heldItem != null && isItemDisabled(heldItem.getType())) {
                // 如果玩家手持禁用物品，掉落该物品
                player.getWorld().dropItem(player.getLocation(), heldItem);  // 将禁用物品掉落在玩家当前位置
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));  // 清空玩家的手持物品
                sendActionBar(player,cannotUseItem);
                log(player.getName() + " 手持禁用物品，已丢弃该物品并清空手持物品。");
            }
        }
    }



    // 查找玩家背包中是否有非禁用物品，作为替换的物品
    private ItemStack getNonDisabledItem(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && !isItemDisabled(item.getType())) {
                return item; // 找到一个非禁用物品并返回
            }
        }
        return null; // 如果没有非禁用物品则返回 null
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack newItem = player.getInventory().getItemInMainHand(); // 获取玩家主手中的物品
        String notprvent = PlaceholderAPI.setPlaceholders(player, "%husktowns_current_location_can_open_containers%");
        log(notprvent);

        if (player.isOp()) {
            return;
        }

        if (newItem != null && isItemDisabled(newItem.getType()) && notprvent.equals("no")) {
            event.setCancelled(true);
            if (cannotUseItem != null) {
                sendActionBar(player, cannotUseItem);
            }
        }
    }

    private void sendActionBar(Player player, String message) {
        TextComponent actionBar = new TextComponent(message);
        actionBar.setColor(net.md_5.bungee.api.ChatColor.RED); // 设置颜色
        player.spigot().sendMessage(actionBar); // 发送 ActionBar 消息
    }

    private void teleportPlayerToSpawn(Player player) {
        // 替换 spawn 命令中的占位符
        String command = PlaceholderAPI.setPlaceholders(player, spawnCommand);

        // 执行命令
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        player.sendMessage("你携带了禁用物品，已被传送回 spawn！");
        moveCancelCountMap.put(player, 0); // 重置取消计数
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
