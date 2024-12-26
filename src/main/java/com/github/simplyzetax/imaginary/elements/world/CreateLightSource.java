package com.github.simplyzetax.imaginary.elements.world;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;

public class CreateLightSource extends Element {

    public CreateLightSource(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Create Light Source";
    }

    public String getInternalName() {
        return "create-light-source";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.COPPER_BULB;
    }

    public String[] getDescription() {
        return new String[]{"Creates a light source at the specified location."};
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("world", "World", DataType.STRING, elementInfo),
                new Argument("x", "X", DataType.DOUBLE, elementInfo),
                new Argument("y", "Y", DataType.DOUBLE, elementInfo),
                new Argument("z", "Z", DataType.DOUBLE, elementInfo),
                new Argument("level", "Light level", DataType.NUMBER, elementInfo),
        };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{};
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {

        double x = Double.parseDouble(getArguments(elementInfo)[0].getValue(scriptInstance).toString());
        double y = Double.parseDouble(getArguments(elementInfo)[1].getValue(scriptInstance).toString());
        double z = Double.parseDouble(getArguments(elementInfo)[2].getValue(scriptInstance).toString());
        int lightLevel = Integer.parseInt(getArguments(elementInfo)[3].getValue(scriptInstance).toString());
        String worldName = getArguments(elementInfo)[4].getValue(scriptInstance).toString();

        World world = Bukkit.getWorld(worldName);
        Location location = new Location(world, x, y, z);
        Block block = location.getBlock();
        BlockData blockData = Bukkit.createBlockData("minecraft:light[level=" + lightLevel + "]");
        block.setBlockData(blockData);

        // fade out the light source over 5 seconds
        int interval = 20; // 1 second (20 ticks)
        int totalDuration = 100; // 5 seconds (100 ticks)
        int steps = totalDuration / interval;
        int decrement = lightLevel / steps;

        new BukkitRunnable() {
            int currentStrength = lightLevel;

            @Override
            public void run() {
                if (currentStrength > 0) {
                    currentStrength -= decrement;
                    if (currentStrength < 0) currentStrength = 0;
                    BlockData blockData = Bukkit.createBlockData("minecraft:light[level=" + currentStrength + "]");
                    block.setBlockData(blockData);
                } else {
                    block.setType(Material.AIR);
                    this.cancel();
                }
            }
        }.runTaskTimer(UltraCustomizer.getInstance().getBootstrap(), 0L, interval);

        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}