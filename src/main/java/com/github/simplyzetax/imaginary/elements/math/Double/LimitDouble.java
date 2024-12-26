package com.github.simplyzetax.imaginary.elements.math.Double;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;

public class LimitDouble extends Element {

    public LimitDouble(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Limit Double";
    }

    public String getInternalName() {
        return "limit-double";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE;
    }

    public String[] getDescription() {
        return new String[] {
                "Allows you to set a max value for a double.",
                "If the double is higher than the max value, it will be set to the max value you set."
        };
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[] {
                new Argument("double", "Double", DataType.DOUBLE, elementInfo),
                new Argument("max", "Max Value", DataType.DOUBLE, elementInfo)
        };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[] {
                new OutcomingVariable("res-max-double", "Resulting Max Double", DataType.DOUBLE, elementInfo)
        };
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] { new DefaultChild(elementInfo, "next") };
    }

    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        try {
            final double doubleValue = (double) getArguments(elementInfo)[0].getValue(scriptInstance);
            final double max = (double) getArguments(elementInfo)[1].getValue(scriptInstance);
            final double result = Math.min(doubleValue, max);

            getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return result;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return 0.0;
                }
            });
        }

        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}