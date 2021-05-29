package com.yoonicode.combattagkeepinv;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class PluginMain extends JavaPlugin {
    Logger logger;
    PluginListener listener;

    @Override
    public void onEnable(){
        logger = Bukkit.getLogger();
        logger.info("Thanks for using CombatTagKeepInv plugin!\nMade by yoonicode.com\nSource code: https://github.com/yummypasta/CombatTagKeepInv");
        listener = new PluginListener(this);
    }
}
