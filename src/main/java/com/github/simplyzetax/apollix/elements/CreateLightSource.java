package com.github.simplyzetax.apollix.elements;

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
                new Argument("x", "X", DataType.NUMBER, elementInfo),
                new Argument("y", "Y", DataType.NUMBER, elementInfo),
                new Argument("z", "Z", DataType.NUMBER, elementInfo),
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
        int strength = Integer.parseInt(getArguments(elementInfo)[3].getValue(scriptInstance).toString());

        World world = Bukkit.getWorlds().get(0);
        Location location = new Location(world, x, y, z);
        Block block = location.getBlock();
        BlockData blockData = Bukkit.createBlockData("minecraft:light[level=" + strength + "]");
        block.setBlockData(blockData);

        // fade out the light source over 5 seconds
        int interval = 20; // 1 second (20 ticks)
        int totalDuration = 100; // 5 seconds (100 ticks)
        int steps = totalDuration / interval;
        int decrement = strength / steps;

        Bukkit.getScheduler().runTaskTimer(UltraCustomizer.getInstance().getBootstrap(), new Runnable() {
            int currentStrength = strength;

            @Override
            public void run() {
                if (currentStrength > 0) {
                    currentStrength -= decrement;
                    if (currentStrength < 0) currentStrength = 0;
                    BlockData blockData = Bukkit.createBlockData("minecraft:light[level=" + currentStrength + "]");
                    block.setBlockData(blockData);
                } else {
                    block.setType(Material.AIR);
                    Bukkit.getScheduler().cancelTask(this.hashCode());
                }
            }
        }, 0L, interval);

        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}