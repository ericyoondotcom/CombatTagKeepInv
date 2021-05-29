package com.yoonicode.combattagkeepinv;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class PluginMain extends JavaPlugin {
    public HashMap<Player, Long> cooldowns;

    public HashMap<Player, BossBar> bossBars;
    Logger logger;
    PluginListener listener;
    int updateTask;
    double cooldownTime;
    boolean killCombatLoggers;
    List<String> activeWorlds;


    @Override
    public void onEnable(){
        logger = Bukkit.getLogger();
        logger.info("Thanks for using CombatTagKeepInv plugin!\nMade by yoonicode.com\nSource code: https://github.com/yummypasta/CombatTagKeepInv");

        saveDefaultConfig();
        cooldownTime = getConfig().getDouble("cooldownTime");
        activeWorlds = getConfig().getStringList("worlds");
        killCombatLoggers = getConfig().getBoolean("killCombatLoggers");

        cooldowns = new HashMap<Player, Long>();
        bossBars = new HashMap<Player, BossBar>();
        listener = new PluginListener(this);
        getServer().getPluginManager().registerEvents(listener, this);

        BukkitScheduler scheduler = Bukkit.getScheduler();
        updateTask = scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                updateTaskCallback();
            }
        }, 0L, 20L);
    }

    public void combatTagPlayer(Player player){
        cooldowns.put(player, System.currentTimeMillis());
        if(!bossBars.containsKey(player)){
            BossBar bar = Bukkit.createBossBar("In Combat!", BarColor.RED, BarStyle.SOLID);
            bar.addPlayer(player);
            bossBars.put(player, bar);

        } else {
            BossBar bar = bossBars.get(player);
            bar.setVisible(true);
        }
    }

    public void updateTaskCallback(){
        Set<Player> players = cooldowns.keySet();
        for(Player player : players){
            long startTime = cooldowns.get(player);
            double differenceSecs = (System.currentTimeMillis() - startTime) / 1000.0;
            if(differenceSecs >= cooldownTime){
                cooldowns.remove(player);
                if(bossBars.containsKey(player)) {
                    bossBars.get(player).setVisible(false);
                }
            } else {
                if(bossBars.containsKey(player)){
                    bossBars.get(player).setProgress((cooldownTime - differenceSecs) / cooldownTime);
                    bossBars.get(player).setTitle("In Combat! [" + (int)(cooldownTime - differenceSecs) + "s]");
                }
            }
        }
    }
}
