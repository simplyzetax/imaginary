package com.github.simplyzetax.apollix.elements.entities;

import com.github.simplyzetax.apollix.data.EntityStore;
import com.github.simplyzetax.apollix.specifications.EntitySpecification;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public class RemoveEntity extends Element {

    public RemoveEntity(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Removes Entity";
    }

    public String getInternalName() {
        return "get-entity-health";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE;
    }

    public String[] getDescription() {
        return new String[]{
                "Removes an Entity from the world",
        };
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("entity", "Entity", DataType.getCustomDataType("entityspecification"), elementInfo),
        };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("success", "Success", DataType.BOOLEAN, elementInfo),
        };
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {

        String entityString = getArguments(elementInfo)[0].getValue(scriptInstance).toString();
        EntitySpecification entitySpec = EntitySpecification.deserialize(entityString);
        LivingEntity entity = entitySpec.getEntity();

        entity.remove();

        getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            public Object request() {
                return entity != null;
            }
        });

        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}