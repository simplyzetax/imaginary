package com.github.simplyzetax.imaginary.elements.math;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.entity.Player;

public class CalculatePositionBehindPlayer extends Element {

    public CalculatePositionBehindPlayer(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Calculate Position Behind Player";
    }

    @Override
    public String getInternalName() {
        return "calculate-position-behind-player";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.NETHERITE_UPGRADE_SMITHING_TEMPLATE;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Calculates the position behind a player using their yaw and coordinates with a given offset."};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("player", "Player", DataType.PLAYER, elementInfo),
                new Argument("offset", "Offset", DataType.DOUBLE, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("x", "X Coordinate", DataType.DOUBLE, elementInfo),
                new OutcomingVariable("y", "Y Coordinate", DataType.DOUBLE, elementInfo),
                new OutcomingVariable("z", "Z Coordinate", DataType.DOUBLE, elementInfo),
                new OutcomingVariable("yaw", "Yaw", DataType.DOUBLE, elementInfo)
        };
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        // Get input values
        Player player = (Player) getArguments(elementInfo)[0].getValue(scriptInstance);
        double offset = Double.parseDouble(getArguments(elementInfo)[1].getValue(scriptInstance).toString());

        // Get the player's yaw and position
        float yaw = player.getLocation().getYaw();
        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();

        // Convert yaw to radians
        double radians = Math.toRadians(yaw);

        // Calculate the position behind the player
        double newX = x - offset * Math.sin(radians);
        double newZ = z + offset * Math.cos(radians);

        // Store the result in outcoming variables
        getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            public Object request() {
                return newX;
            }
        });
        getOutcomingVariables(elementInfo)[1].register(scriptInstance, new DataRequester() {
            public Object request() {
                return y;
            }
        });
        getOutcomingVariables(elementInfo)[2].register(scriptInstance, new DataRequester() {
            public Object request() {
                return newZ;
            }
        });
        getOutcomingVariables(elementInfo)[3].register(scriptInstance, new DataRequester() {
            public Object request() {
                return yaw;
            }
        });

        // Continue the script
        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}