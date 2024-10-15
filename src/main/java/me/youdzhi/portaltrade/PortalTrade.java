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



public class PortalTrade extends JavaPlugin implements Listener {
    public static final String VERSION = "1.0";

    private static PortalTrade instance;

    int redstoneCount = 0;
    int diamondCount = 0;
    int yellowDyeCount = 0;
    int stoneCount = 0;
    int obsidianCount = 0;
    int sandCount = 0;

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
        redstoneCount = 0;
        diamondCount = 0;
        yellowDyeCount = 0;
        stoneCount = 0;
        obsidianCount = 0;
        sandCount = 0;
    }
    @EventHandler
    public void DropItem(PlayerDropItemEvent drop) {
        Item dropitem = drop.getItemDrop();
        Location loc = dropitem.getLocation();
        System.out.printf("Dropped Item!");
        System.out.printf(dropitem.toString());
        System.out.printf("At:");
        System.out.printf(String.valueOf(loc));
        System.out.printf(loc.getBlock().toString());
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            RecipeGlowstone(drop.getItemDrop().getLocation().getBlock(), drop.getItemDrop().getLocation());
            RecipeNetherrack(drop.getItemDrop().getLocation().getBlock(), drop.getItemDrop().getLocation());
            RecipeSoulsand(drop.getItemDrop().getLocation().getBlock(), drop.getItemDrop().getLocation());
        }, 60L);

    }

    private void visualeffect(Block portalBlock){
        portalBlock.getWorld().playEffect(portalBlock.getLocation(), Effect.SMOKE, 100, 100);
    }

    private void RecipeGlowstone(Block portalBlock, Location loc) {

        for (Entity entity : portalBlock.getWorld().getEntities()) {
            if (entity instanceof CraftItem) {
                ItemStack itemStack = ((Item) entity).getItemStack();
                Material matblock = entity.getLocation().getBlock().getType();
                if (matblock.toString().equals("PORTAL")) {
                    if (itemStack.getType() == Material.REDSTONE) {
                        redstoneCount += itemStack.getAmount();
                        System.out.println(redstoneCount);
                        System.out.println(diamondCount);
                        System.out.println(yellowDyeCount);
                    }
                    else if (itemStack.getType() == Material.DIAMOND) {
                        diamondCount += itemStack.getAmount();
                        System.out.println(redstoneCount);
                        System.out.println(diamondCount);
                        System.out.println(yellowDyeCount);
                    }
                    else if (itemStack.getType() == Material.YELLOW_FLOWER) {
                        yellowDyeCount += itemStack.getAmount();
                        System.out.println(redstoneCount);
                        System.out.println(diamondCount);
                        System.out.println(yellowDyeCount);
                    }
                }
            }
        }

        if (redstoneCount == 2 && diamondCount == 2 && yellowDyeCount == 2) {
            System.out.printf("Crafting started!");
            visualeffect(portalBlock);
            redstoneCount = 0;
            diamondCount = 0;
            yellowDyeCount = 0;

            portalBlock.getWorld().dropItem(portalBlock.getLocation(), new ItemStack(Material.GLOWSTONE_DUST, 1));
            removeItems(portalBlock, loc);
        }  else if (redstoneCount > 2 || diamondCount > 2 || yellowDyeCount > 2){
        portalBlock.getWorld().createExplosion(loc, 3, true);
    }


    }

    private void RecipeNetherrack(Block portalBlock, Location loc) {

        for (Entity entity : portalBlock.getWorld().getEntities()) {
            if (entity instanceof CraftItem) {
                ItemStack itemStack = ((Item) entity).getItemStack();
                Material matblock = entity.getLocation().getBlock().getType();
                if (matblock.toString().equals("PORTAL")) {
                    if (itemStack.getType() == Material.STONE) {
                        stoneCount += itemStack.getAmount();
                        System.out.println(stoneCount);
                    }
                }
            }
        }

        // Check if we have the required amounts
        if (stoneCount == 32) {
            System.out.printf("Crafting started!");
            visualeffect(portalBlock);
            stoneCount = 0;

            // Drop Netherrack
            portalBlock.getWorld().dropItem(portalBlock.getLocation(), new ItemStack(Material.NETHERRACK, 1));
            removeItems(portalBlock, loc);
        } else if (stoneCount > 32){
            portalBlock.getWorld().createExplosion(loc, 3, true);
        }


    }

    private void RecipeSoulsand(Block portalBlock, Location loc) {

        for (Entity entity : portalBlock.getWorld().getEntities()) {
            if (entity instanceof CraftItem) {
                ItemStack itemStack = ((Item) entity).getItemStack();
                Material matblock = entity.getLocation().getBlock().getType();
                if (matblock.toString().equals("PORTAL")) {
                    if (itemStack.getType() == Material.OBSIDIAN) {
                        obsidianCount += itemStack.getAmount();
                        System.out.println(obsidianCount);
                        System.out.println(sandCount);
                    } else if (itemStack.getType() == Material.SAND) {
                        sandCount += itemStack.getAmount();
                        System.out.println(obsidianCount);
                        System.out.println(sandCount);
                    }
                }
            }
        }

        // Check if we have the required amounts
        if (obsidianCount == 2 && sandCount == 2) {
            System.out.printf("Crafting started!");
            visualeffect(portalBlock);
            obsidianCount = 0;
            sandCount = 0;

            // Drop Soul Sand
            portalBlock.getWorld().dropItem(portalBlock.getLocation(), new ItemStack(Material.SOUL_SAND, 1));
            removeItems(portalBlock, loc);
        } else if (obsidianCount > 2 || sandCount > 2){
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




