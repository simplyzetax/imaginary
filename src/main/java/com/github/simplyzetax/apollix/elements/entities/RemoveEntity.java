package com.github.simplyzetax.apollix.elements.entities;

import com.github.simplyzetax.apollix.data.EntityStore;
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
                "Removes an Entity from the HashMap",
                "Only use this if you know what you are doing"
        };
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("entity_id", "Entity UUID", DataType.STRING, elementInfo),
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

        UUID EntityID = UUID.fromString(getArguments(elementInfo)[0].getValue(scriptInstance).toString());

        LivingEntity entity = EntityStore.entities.get(EntityID);

        if (entity != null) {
            EntityStore.entities.remove(EntityID);
        }

        getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            public Object request() {
                return entity != null;
            }
        });

        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}