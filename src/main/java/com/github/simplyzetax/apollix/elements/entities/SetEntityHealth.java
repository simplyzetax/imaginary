package com.github.simplyzetax.apollix.elements.entities;

import com.github.simplyzetax.apollix.specifications.entity.Specification;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.entity.LivingEntity;

public class SetEntityHealth extends Element {

    public SetEntityHealth(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Set Entity Health";
    }

    public String getInternalName() {
        return "set-entity-health";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.SPYGLASS;
    }

    public String[] getDescription() {
        return new String[] {
                "Sets the health of the entity",
                "Good practice to call the Remove Entity Element afterwards to clean up memory",
                "Unless you want to keep the entity in memory for later Entity manipulation"
        };
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[] {
                new Argument("entity-spec", "Entity", DataType.getCustomDataType("entityspecification"), elementInfo),
                new Argument("health", "Health", DataType.DOUBLE, elementInfo)
        };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[] {
                new OutcomingVariable("success", "Success", DataType.BOOLEAN, elementInfo)
        };
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] { new DefaultChild(elementInfo, "next") };
    }

    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {

        try {
            String entityString = getArguments(elementInfo)[0].getValue(scriptInstance).toString();
            Specification entitySpec = Specification.deserialize(entityString);
            LivingEntity entity = entitySpec.getEntity();
            double Health = ((Number) getArguments(elementInfo)[1].getValue(scriptInstance)).doubleValue();

            if(entity == null) {
                getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                    public Object request() {
                        return false;
                    }
                });
            } else {
                entity.setHealth(Health);
                getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                    public Object request() {
                        return true;
                    }
                });
            }
        } catch (Exception e) {
            getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return false;
                }
            });
        }

        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}