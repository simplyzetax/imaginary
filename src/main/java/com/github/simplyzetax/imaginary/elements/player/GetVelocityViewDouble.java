package com.github.simplyzetax.imaginary.elements.player;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class GetVelocityViewDouble extends Element {
    public GetVelocityViewDouble(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Get Velocity From View (Double)";
    }

    public String getInternalName() {
        return "get-velocity-from-p-direction-double";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.ENDER_EYE;
    }

    public String[] getDescription() {
        return new String[]{"Will return X, Z axis of where player is looking at as a double."};
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{new Argument("player", "Player", DataType.PLAYER, elementInfo),
                new Argument("power", "Power", DataType.DOUBLE, elementInfo)};
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{new OutcomingVariable("xAxis", "X Axis", DataType.DOUBLE, elementInfo), new OutcomingVariable("zAxis", "Z Axis", DataType.DOUBLE, elementInfo)};
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        Player player = (Player) getArguments(elementInfo)[0].getValue(scriptInstance);
        double power = ((Number) getArguments(elementInfo)[1].getValue(scriptInstance)).doubleValue();
        Vector direction = player.getLocation().getDirection().normalize().multiply(power); // Normalize the direction vector
        final double x = direction.getX();
        final double z = direction.getZ();

        getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            public Object request() {
                return x;
            }
        });
        getOutcomingVariables(elementInfo)[1].register(scriptInstance, new DataRequester() {
            public Object request() {
                return z;
            }
        });
        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}