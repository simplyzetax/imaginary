package com.github.simplyzetax.apollix.elements.player;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.entity.Player;

public class GetYaw extends Element {

    public GetYaw(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Get Player Yaw";
    }

    @Override
    public String getInternalName() {
        return "get-player-yaw";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Gets the yaw of a player."};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("player", "Player", DataType.PLAYER, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
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

        // Get the player's yaw
        Double yaw = (double) player.getLocation().getYaw();

        // Store the result in an outcoming variable
        getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            public Object request() {
                return yaw;
            }
        });

        // Continue the script
        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}