package com.github.simplyzetax.apollix.elements.entities;

import java.util.UUID;

import com.github.simplyzetax.apollix.data.EntityStore;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.entity.LivingEntity;

public class DamageEntity extends Element {

    public DamageEntity(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Damage Entity";
    }

    public String getInternalName() {
        return "damage-entity";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE;
    }

    public String[] getDescription() {
        return new String[] { "Damages the provided entity" };
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[] {
                new Argument("entity_id", "Entity UUID", DataType.STRING, elementInfo),
                new Argument("damage", "Damage", DataType.DOUBLE, elementInfo)
        };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[] {};
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] { new DefaultChild(elementInfo, "next") };
    }

    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
    try {
        UUID EntityID = UUID.fromString(getArguments(elementInfo)[0].getValue(scriptInstance).toString());
        double damage = (double) getArguments(elementInfo)[1].getValue(scriptInstance);

        LivingEntity entity = EntityStore.entities.get(EntityID);

        if (entity != null) {
            entity.damage(damage);
        }
    } catch (IllegalArgumentException e) {
        if (getOutcomingVariables(elementInfo).length > 0) {
            getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return false;
                }
            });
            if (getOutcomingVariables(elementInfo).length > 1) {
                getOutcomingVariables(elementInfo)[1].register(scriptInstance, new DataRequester() {
                    public Object request() {
                        return 0.0;
                    }
                });
            }
        }
    }

    getConnectors(elementInfo)[0].run(scriptInstance);
}
}