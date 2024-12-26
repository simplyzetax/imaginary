package com.github.simplyzetax.imaginary.elements.math.Double;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;

public class Subtract extends Element {
    public Subtract(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Subtract (Double)";
    }

    public String getInternalName() {
        return "computation-subtract-double";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.COAL;
    }

    public String[] getDescription() {
        return new String[] { "Allows you to subtract two values." };
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[] { new Argument("valueOne", "1st Value", DataType.DOUBLE, elementInfo), new Argument("valueTwo", "2nd Value", DataType.DOUBLE, elementInfo) };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[] { new OutcomingVariable("result", "Result", DataType.DOUBLE, elementInfo) };
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] { (Child)new DefaultChild(elementInfo, "next") };
    }

    public void run(ElementInfo info, ScriptInstance instance) {
    try {
        final Number valueOne = (Number) getArguments(info)[0].getValue(instance);
        final Number valueTwo = (Number) getArguments(info)[1].getValue(instance);
        final Double result = valueOne.doubleValue() - valueTwo.doubleValue();
        getOutcomingVariables(info)[0].register(instance, new DataRequester() {
            public Object request() {
                return result;
            }
        });
    } catch (Exception ex) {
        ex.printStackTrace();
        getOutcomingVariables(info)[0].register(instance, new DataRequester() {
            public Object request() {
                return null;
            }
        });
    }
    getConnectors(info)[0].run(instance);
}
}
