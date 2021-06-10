package com.yoonicode.combattagkeepinv;

import org.bukkit.GameRule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class PluginListener implements Listener {
    PluginMain main;
    public PluginListener(PluginMain main){
        this.main = main;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void debugTagPlayer(PlayerDropItemEvent event){
        if(event.getItemDrop() == null || event.getItemDrop().getCustomName() == null) return;
        if(event.getItemDrop().getCustomName().equalsIgnoreCase("ctki debug")) {
            main.combatTagPlayer(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void tagPlayer(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player)) return;
        if(!(event.getEntity() instanceof Player)) return;
        Player damager = (Player)event.getDamager();
        Player victim = (Player)event.getEntity();
        if(main.activeWorlds.contains(damager.getWorld().getName())){
            main.combatTagPlayer(damager);
        }
        if(main.activeWorlds.contains(victim.getWorld().getName())){
            main.combatTagPlayer(victim);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();

        if(!main.activeWorlds.contains(player.getWorld().getName())){
//            main.logger.info("World is not in active worlds list, returning");
            return;
        }

        if(player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY) == true) {
//            main.logger.info("World has keep inv on, returning");
            return;
        }
        if(!player.hasPermission(new Permission("combattagkeepinv.exempt", PermissionDefault.FALSE))){
            if(main.cooldowns.containsKey(player)){
                long startTime = main.cooldowns.get(player);
                double differenceSecs = (System.currentTimeMillis() - startTime) / 1000.0;
                if(differenceSecs < main.cooldownTime){
//                    main.logger.info("Dropping items! player is combat tagged");
                    return;
                }
            } else {
//                main.logger.info("Cooldowns hashmap does not contain player, keeping inv");
            }
        } else {
//            main.logger.info("Player has exempt permission, keeping inv");
        }

//        main.logger.info("Setting keepinv to true");
        event.setKeepLevel(true);
        event.setKeepInventory(true);
        event.setDroppedExp(0);
        event.getDrops().clear();
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event){
        if(!main.killCombatLoggers) return;
        Player player = event.getPlayer();
        if(player.isDead()) return;
        if(!main.activeWorlds.contains(player.getWorld().getName())) return;
        if(player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY) == true) return;
        if(player.hasPermission(new Permission("combattagkeepinv.exempt", PermissionDefault.FALSE))) return;
        if(!main.cooldowns.containsKey(player)) return;
        long startTime = main.cooldowns.get(player);
        double differenceSecs = (System.currentTimeMillis() - startTime) / 1000.0;
        if(differenceSecs >= main.cooldownTime) return;

        player.setHealth(0);
    }
}
