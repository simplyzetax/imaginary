package com.github.simplyzetax.imaginary.elements.math.Integer;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;

public class IntegerToDouble extends Element {

    public IntegerToDouble(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Integer To Double";
    }

    public String getInternalName() {
        return "integer-to-double";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE;
    }

    public String[] getDescription() {
        return new String[] {
                "Converts an integer to a dobule"
        };
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[] {
                new Argument("int", "Integer", DataType.NUMBER, elementInfo)
        };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[] {
                new OutcomingVariable("double", "Double", DataType.DOUBLE, elementInfo)
        };
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] { new DefaultChild(elementInfo, "next") };
    }

   public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
    try {
        final Number intValue = (Number) getArguments(elementInfo)[0].getValue(scriptInstance);
        final double doubleValue = intValue.doubleValue();
        getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            public Object request() {
                return doubleValue;
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