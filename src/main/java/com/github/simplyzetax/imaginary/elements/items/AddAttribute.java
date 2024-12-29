package com.github.simplyzetax.imaginary.elements.items;

import com.github.simplyzetax.imaginary.AddonMain;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AddAttribute extends Element {

    public AddAttribute(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Add Attribute";
    }

    public String getInternalName() {
        return "add-attribute";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.CHEST;
    }

    public String[] getDescription() {
        return new String[]{"Add an attribute to an item"};
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("item", "Item", DataType.ITEM, elementInfo),
                new Argument("attribute", "Attribute", DataType.STRING, elementInfo),
                new Argument("value", "Value", DataType.DOUBLE, elementInfo)
        };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{};
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {

        // Retrieve the arguments
        ItemStack itemStack = (ItemStack) getArguments(elementInfo)[0].getValue(scriptInstance);
        String attributeName = (String) getArguments(elementInfo)[1].getValue(scriptInstance);
        double value = (double) getArguments(elementInfo)[2].getValue(scriptInstance);

        // Parse the attribute name into an Attribute
        Attribute attribute;
        try {
            attribute = Attribute.valueOf(attributeName.toUpperCase());
        } catch (IllegalArgumentException e) {
            UltraCustomizer.getInstance().log("Invalid attribute name: " + attributeName);
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();

        // Use NamespacedKey to define a unique attribute modifier
        NamespacedKey key = new NamespacedKey(AddonMain.instance.getName().toLowerCase(), attributeName.toLowerCase());

        // Create the AttributeModifier with the new constructor
        AttributeModifier modifier = new AttributeModifier(
                key, // The unique key
                value, // The amount
                AttributeModifier.Operation.ADD_NUMBER, // The operation type
                EquipmentSlot.HAND.getGroup() // The equipment slot
        );

        // Add the modifier to the attribute
        meta.addAttributeModifier(attribute, modifier);

        // Set the modified meta back to the ItemStack
        itemStack.setItemMeta(meta);

        // Log the successful addition of the attribute
        UltraCustomizer.getInstance().log("Added attribute " + attributeName + " with value " + value + " to item " + itemStack.getItemMeta().getDisplayName());
    }

}