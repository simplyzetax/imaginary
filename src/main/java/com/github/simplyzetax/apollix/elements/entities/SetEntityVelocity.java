package com.github.simplyzetax.apollix.elements.entities;

import com.github.simplyzetax.apollix.specifications.EntitySpecification;
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
        return new String[]{"Sets the velocity of the entity"};
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("x", "X Axis", DataType.DOUBLE, elementInfo),
                new Argument("y", "Y Axis", DataType.DOUBLE, elementInfo),
                new Argument("z", "Z Axis", DataType.DOUBLE, elementInfo),
                new Argument("entity-spec", "Entity", DataType.getCustomDataType("entityspecification"), elementInfo),
        };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{};
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        try {
            final Number xValue = (Number) getArguments(elementInfo)[0].getValue(scriptInstance);
            final Number yValue = (Number) getArguments(elementInfo)[1].getValue(scriptInstance);
            final Number zValue = (Number) getArguments(elementInfo)[2].getValue(scriptInstance);
            final String entityString = getArguments(elementInfo)[3].getValue(scriptInstance).toString();

            final EntitySpecification entitySpec = EntitySpecification.deserialize(entityString);
            final LivingEntity entity = entitySpec.getEntity();

            if (entity == null) {
                UltraCustomizer.getInstance().log(ChatColor.RED + "Entity not found for velocity change.");
            } else {
                Vector velocity = new Vector(xValue.doubleValue(), yValue.doubleValue(), zValue.doubleValue());
                entity.setVelocity(velocity);
            }

            getConnectors(elementInfo)[0].run(scriptInstance);
        } catch (Exception ex) {
            ex.printStackTrace();
            getConnectors(elementInfo)[0].run(scriptInstance);
        }
    }
}