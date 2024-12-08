package com.github.simplyzetax.apollix.elements;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RemoveItemOffhand extends Element {

    public RemoveItemOffhand(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Remove Item Offhand";
    }

    public String getInternalName() {
        return "remove-item-offhand";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.CHEST;
    }

    public String[] getDescription() {
        return new String[]{"Removes the item in the player's offhand."};
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("player", "Player", DataType.PLAYER, elementInfo),
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

        player.getInventory().setItemInOffHand(null);

        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}