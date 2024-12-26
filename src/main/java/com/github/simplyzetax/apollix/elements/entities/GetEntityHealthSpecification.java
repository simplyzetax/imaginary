package com.github.simplyzetax.apollix.elements.entities;

import com.github.simplyzetax.apollix.specifications.entity.Specification;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.entity.LivingEntity;

public class GetEntityHealthSpecification extends Element {

    public GetEntityHealthSpecification(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Get Entity Health";
    }

    public String getInternalName() {
        return "get-entity-health";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.VEX_ARMOR_TRIM_SMITHING_TEMPLATE;
    }

    public String[] getDescription() {
        return new String[] { "Gets the health of the entity" };
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[] {
                new Argument("entity-spec", "Entity NEW", DataType.getCustomDataType("entityspecification"), elementInfo),
        };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[] {
                new OutcomingVariable("success", "Success", DataType.BOOLEAN, elementInfo),
                new OutcomingVariable("health", "Health", DataType.DOUBLE, elementInfo)
        };
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] { new DefaultChild(elementInfo, "next") };
    }

    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {

        try {

            UltraCustomizer.getInstance().log("Running GetEntityHealthSpecification");

            String entityString = getArguments(elementInfo)[0].getValue(scriptInstance).toString();
            Specification entitySpec = Specification.deserialize(entityString);
            LivingEntity entity = entitySpec.getEntity();

            if(entity == null) {
                getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                    public Object request() {
                        return false;
                    }
                });
                getOutcomingVariables(elementInfo)[1].register(scriptInstance, new DataRequester() {
                    public Object request() {
                        return 0.0;
                    }
                });
            } else {

                getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                    public Object request() {
                        return true;
                    }
                });

                getOutcomingVariables(elementInfo)[1].register(scriptInstance, new DataRequester() {
                    public Object request() {
                        return entity.getHealth();
                    }
                });
            }
        } catch (IllegalArgumentException e) {
            getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return false;
                }
            });
            getOutcomingVariables(elementInfo)[1].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return 0.0;
                }
            });
        }

        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}