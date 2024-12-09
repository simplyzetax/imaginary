package com.github.simplyzetax.apollix.elements.player;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportPlayer extends Element {

    public TeleportPlayer(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Teleport Player";
    }

    public String getInternalName() {
        return "teleport-player";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.ENDER_PEARL;
    }

    public String[] getDescription() {
        return new String[]{"Teleports the player to a given location."};
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("player", "Player", DataType.PLAYER, elementInfo),
                new Argument("x", "X", DataType.DOUBLE, elementInfo),
                new Argument("y", "Y", DataType.DOUBLE, elementInfo),
                new Argument("z", "Z", DataType.DOUBLE, elementInfo),
                new Argument("yaw", "Yaw", DataType.DOUBLE, elementInfo)
        };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{};
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        Player player = (Player) getArguments(elementInfo)[0].getValue(scriptInstance);
        double x = Double.parseDouble(getArguments(elementInfo)[1].getValue(scriptInstance).toString());
        double y = Double.parseDouble(getArguments(elementInfo)[2].getValue(scriptInstance).toString());
        double z = Double.parseDouble(getArguments(elementInfo)[3].getValue(scriptInstance).toString());
        float yaw = Float.parseFloat(getArguments(elementInfo)[4].getValue(scriptInstance).toString());

        Location location = new Location(player.getWorld(), x, y, z, yaw, player.getLocation().getPitch());
        player.teleport(location);

        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}