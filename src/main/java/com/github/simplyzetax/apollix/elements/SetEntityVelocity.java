package com.github.simplyzetax.apollix.elements;

import java.util.UUID;

import com.github.simplyzetax.apollix.data.EntityStore;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class SetEntityVelocity extends Element {
    public SetEntityVelocity(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Set Entity Velocity";
    }

    public String getInternalName() {
        return "set-entity-velocity";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.SLIME_BALL;
    }

    public String[] getDescription() {
        return new String[] { "Sets the velocity of the entity" };
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[] {
            new Argument("x", "X Axis", DataType.DOUBLE, elementInfo),
            new Argument("y", "Y Axis", DataType.DOUBLE, elementInfo),
            new Argument("z", "Z Axis", DataType.DOUBLE, elementInfo),
            new Argument("entity_id", "Entity UUID", DataType.STRING, elementInfo)
        };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[] {};
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] { new DefaultChild(elementInfo, "next") };
    }

    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        double x = (double) getArguments(elementInfo)[0].getValue(scriptInstance);
        double y = (double) getArguments(elementInfo)[1].getValue(scriptInstance);
        double z = (double) getArguments(elementInfo)[2].getValue(scriptInstance);
        String entityIdStr = (String) getArguments(elementInfo)[3].getValue(scriptInstance);

        UUID entity_id;
        try {
            entity_id = UUID.fromString(entityIdStr);
        } catch (IllegalArgumentException e) {
            UltraCustomizer.getInstance().log(ChatColor.RED + "Invalid UUID format: " + entityIdStr);
            getConnectors(elementInfo)[0].run(scriptInstance);
            return;
        }

        LivingEntity entity = EntityStore.entities.get(entity_id);
        if (entity == null) {
            UltraCustomizer.getInstance().log(ChatColor.RED + "Entity not found for velocity change.");
        } else {
            Vector velocity = new Vector(x, y, z);
            entity.setVelocity(velocity);
        }

        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}