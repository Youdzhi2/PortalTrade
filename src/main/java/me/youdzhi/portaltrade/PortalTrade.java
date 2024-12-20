package me.youdzhi.portaltrade;


import org.bukkit.*;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.HashMap;


public class PortalTrade extends JavaPlugin implements Listener {
    public static final String VERSION = "1.0";

    private static PortalTrade instance;

    public static final int RECIPES = 3;

    private HashMap<Location, HashMap<Material, Integer>> itemCounts = new HashMap<>();

    private HashMap<Material, Integer> getCountsForLocation(Location loc) {
        return itemCounts.computeIfAbsent(loc, k -> new HashMap<Material, Integer>() {{
            put(Material.REDSTONE, 0);
            put(Material.DIAMOND, 0);
            put(Material.YELLOW_FLOWER, 0);
            put(Material.STONE, 0);
            put(Material.OBSIDIAN, 0);
            put(Material.SAND, 0);
        }});
    }

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(this, this);

    }


    @Override
    public void onDisable() {
    }

    @EventHandler
    public void GetItem(PlayerPickupItemEvent drop) {
        // Reset counts for the specific location
        Location loc = drop.getPlayer().getLocation();
        itemCounts.put(loc, new HashMap<Material, Integer>() {{
            put(Material.REDSTONE, 0);
            put(Material.DIAMOND, 0);
            put(Material.YELLOW_FLOWER, 0);
            put(Material.STONE, 0);
            put(Material.OBSIDIAN, 0);
            put(Material.SAND, 0);
        }});
    }
    @EventHandler
    public void DropItem(PlayerDropItemEvent drop) {
        Item dropitem = drop.getItemDrop();
        Location loc = dropitem.getLocation();


        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            RecipeGlowstone(drop.getItemDrop().getLocation().getBlock(), loc);
            RecipeNetherrack(drop.getItemDrop().getLocation().getBlock(), loc);
            RecipeSoulsand(drop.getItemDrop().getLocation().getBlock(), loc);
        }, 60L);
    }

    private void visualeffect(Block portalBlock){
        portalBlock.getWorld().playEffect(portalBlock.getLocation(), Effect.SMOKE, 100, 100);
    }

    private void RecipeGlowstone(Block portalBlock, Location loc) {
        HashMap<Material, Integer> counts = getCountsForLocation(loc);

        for (Entity entity : portalBlock.getWorld().getEntities()) {
            if (entity != null){
            if (entity instanceof CraftItem) {
                ItemStack itemStack = ((Item) entity).getItemStack();
                Material matblock = entity.getLocation().getBlock().getType();
                if (matblock.toString().equals("PORTAL")) {
                    counts.put(itemStack.getType(), counts.get(itemStack.getType()) + itemStack.getAmount());
                }
            }}
        }

        if (counts.get(Material.REDSTONE) == 2 && counts.get(Material.DIAMOND) == 2 && counts.get(Material.YELLOW_FLOWER) == 2) {
            visualeffect(portalBlock);
            // Reset counts
            counts.put(Material.REDSTONE, 0);
            counts.put(Material.DIAMOND, 0);
            counts.put(Material.YELLOW_FLOWER, 0);

            portalBlock.getWorld().dropItem(portalBlock.getLocation(), new ItemStack(Material.GLOWSTONE_DUST, 1));
            removeItems(portalBlock, loc);
        } else if (counts.get(Material.REDSTONE) > 2 || counts.get(Material.DIAMOND) > 2 || counts.get(Material.YELLOW_FLOWER ) > 2) {
            portalBlock.getWorld().createExplosion(loc, 1, true);
        }
    }

    private void RecipeNetherrack(Block portalBlock, Location loc) {
        HashMap<Material, Integer> counts = getCountsForLocation(loc);

        for (Entity entity : portalBlock.getWorld().getEntities()) {
            if (entity != null){
            if (entity instanceof CraftItem) {
                ItemStack itemStack = ((Item) entity).getItemStack();
                Material matblock = entity.getLocation().getBlock().getType();
                if (matblock.toString().equals("PORTAL")) {
                    counts.put(itemStack.getType(), counts.get(itemStack.getType()) + itemStack.getAmount());
                }
            }}
        }

        if (counts.get(Material.STONE) == (32 * (RECIPES - 1))) {
            visualeffect(portalBlock);
            // Reset counts
            counts.put(Material.STONE, 0);

            portalBlock.getWorld().dropItem(portalBlock.getLocation(), new ItemStack(Material.NETHERRACK, 1));
            removeItems(portalBlock, loc);
        } else if (counts.get(Material.STONE) > (32 * (RECIPES-1))) {
            portalBlock.getWorld().createExplosion(loc, 3, true);
        }
    }

    private void RecipeSoulsand(Block portalBlock, Location loc) {
        HashMap<Material, Integer> counts = getCountsForLocation(loc);

        for (Entity entity : portalBlock.getWorld().getEntities()) {
            if (entity != null){
            if (entity instanceof CraftItem) {
                ItemStack itemStack = ((Item) entity).getItemStack();
                Material matblock = entity.getLocation().getBlock().getType();
                if (matblock.toString().equals("PORTAL")) {
                    counts.put(itemStack.getType(), counts.get(itemStack.getType()) + itemStack.getAmount());
                }
            }}
        }

        if (counts.get(Material.OBSIDIAN) == (2 * RECIPES) && counts.get(Material.SAND) == (2 * RECIPES)) {
            visualeffect(portalBlock);
            // Reset counts
            counts.put(Material.OBSIDIAN, 0);
            counts.put(Material.SAND, 0);

            portalBlock.getWorld().dropItem(portalBlock.getLocation(), new ItemStack(Material.SOUL_SAND, 1));
            removeItems(portalBlock, loc);
        } else if (counts.get(Material.OBSIDIAN) > (2 * RECIPES) || counts.get(Material.SAND) > (2 * RECIPES)) {
            portalBlock.getWorld().createExplosion(loc, 3, true);
        }
    }

    private void removeItems(Block portalBlock, Location loc){
        for (Entity entity : portalBlock.getWorld().getEntities()) {
            if (entity instanceof CraftItem) {
                ItemStack itemStack = ((Item) entity).getItemStack();
                String checkportal = entity.getLocation().getBlock().getType().toString();
                if (checkportal.toString().equals("PORTAL")) {
                    if (itemStack.getType() == Material.REDSTONE) {
                        entity.remove();
                    }
                    if (itemStack.getType() == Material.DIAMOND) {
                        entity.remove();
                    }
                    if (itemStack.getType() == Material.YELLOW_FLOWER) {
                        entity.remove();
                    }
                    if (itemStack.getType() == Material.STONE) {
                        entity.remove();
                    }
                    if (itemStack.getType() == Material.SAND) {
                        entity.remove();
                    }
                    if (itemStack.getType() == Material.OBSIDIAN) {
                        entity.remove();
                    }
                }
            }
        }
    }

}
