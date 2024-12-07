package com.github.simplyzetax.apollix.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.github.simplyzetax.apollix.data.EntityStore;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

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
                new Argument("x", "X", DataType.NUMBER, elementInfo),
                new Argument("y", "Y", DataType.NUMBER, elementInfo),
                new Argument("z", "Z", DataType.NUMBER, elementInfo),
                new Argument("power", "Power", DataType.NUMBER, elementInfo),
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

        double x = Double.parseDouble(getArguments(elementInfo)[0].getValue(scriptInstance).toString());
        double y = Double.parseDouble(getArguments(elementInfo)[1].getValue(scriptInstance).toString());
        double z = Double.parseDouble(getArguments(elementInfo)[2].getValue(scriptInstance).toString());
        double powerDouble = ((Number) getArguments(elementInfo)[3].getValue(scriptInstance)).doubleValue();
        float power = (float) powerDouble;
        boolean fire = (boolean) getArguments(elementInfo)[4].getValue(scriptInstance);
        boolean breakBlocks = (boolean) getArguments(elementInfo)[5].getValue(scriptInstance);

        Bukkit.getWorlds().get(0).createExplosion(x, y, z, power, fire, breakBlocks);

        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}