package com.github.simplyzetax.imaginary.elements.entities;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class CreateExplosion extends Element {

    public CreateExplosion(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Create Explosion";
    }

    public String getInternalName() {
        return "create-explosion";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.TNT;
    }

    public String[] getDescription() {
        return new String[] { "Creates an explosion at the given coordinates" };
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[] {
                new Argument("world", "World", DataType.STRING, elementInfo),
                new Argument("x", "X", DataType.DOUBLE, elementInfo),
                new Argument("y", "Y", DataType.DOUBLE, elementInfo),
                new Argument("z", "Z", DataType.DOUBLE, elementInfo),
                new Argument("power", "Power", DataType.DOUBLE, elementInfo),
                new Argument("fire", "Fire", DataType.BOOLEAN, elementInfo),
                new Argument("breakBlocks", "Break Blocks", DataType.BOOLEAN, elementInfo)
        };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[] { };
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] { new DefaultChild(elementInfo, "next") };
    }

    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {

        String world = (String) getArguments(elementInfo)[0].getValue(scriptInstance);
        double x = Double.parseDouble(getArguments(elementInfo)[0].getValue(scriptInstance).toString());
        double y = Double.parseDouble(getArguments(elementInfo)[1].getValue(scriptInstance).toString());
        double z = Double.parseDouble(getArguments(elementInfo)[2].getValue(scriptInstance).toString());
        double powerDouble = ((Number) getArguments(elementInfo)[3].getValue(scriptInstance)).doubleValue();
        float power = (float) powerDouble;
        boolean fire = (boolean) getArguments(elementInfo)[4].getValue(scriptInstance);
        boolean breakBlocks = (boolean) getArguments(elementInfo)[5].getValue(scriptInstance);

        World bukkitWorld = Bukkit.getWorld(world);
        if (bukkitWorld == null) {
            throw new RuntimeException("World for CreateExplosion not found");
        }

        bukkitWorld.createExplosion(x, y, z, power, fire, breakBlocks);

        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}