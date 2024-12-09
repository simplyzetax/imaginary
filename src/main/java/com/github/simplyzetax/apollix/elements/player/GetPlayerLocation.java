package com.github.simplyzetax.apollix.elements.player;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.entity.Player;

public class GetPlayerLocation extends Element {

    public GetPlayerLocation(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Get Player Location";
    }

    public String getInternalName() {
        return "get-player-location";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.COAST_ARMOR_TRIM_SMITHING_TEMPLATE;
    }

    public String[] getDescription() {
        return new String[]{"Gets the player's location"};
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("player", "Player", DataType.PLAYER, elementInfo),
        };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("x", "X", DataType.DOUBLE, elementInfo),
                new OutcomingVariable("y", "Y", DataType.DOUBLE, elementInfo),
                new OutcomingVariable("z", "Z", DataType.DOUBLE, elementInfo),
        };
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {

        Player player = (Player) getArguments(elementInfo)[0].getValue(scriptInstance);

        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();

        getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            public Object request() {
                return x;
            }
        });

        getOutcomingVariables(elementInfo)[1].register(scriptInstance, new DataRequester() {
            public Object request() {
                return y;
            }
        });

        getOutcomingVariables(elementInfo)[2].register(scriptInstance, new DataRequester() {
            public Object request() {
                return z;
            }
        });

        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}