package com.github.simplyzetax.imaginary.elements.player;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SetItemOffhand extends Element {

    public SetItemOffhand(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Set Item Offhand";
    }

    public String getInternalName() {
        return "set-item-offhand";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.CHEST;
    }

    public String[] getDescription() {
        return new String[]{"Sets the item in the player's offhand."};
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("player", "Player", DataType.PLAYER, elementInfo),
                new Argument("item", "Item", DataType.ITEM, elementInfo)
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
        ItemStack itemStack = (ItemStack) getArguments(elementInfo)[1].getValue(scriptInstance);

        player.getInventory().setItemInOffHand(itemStack);

        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}