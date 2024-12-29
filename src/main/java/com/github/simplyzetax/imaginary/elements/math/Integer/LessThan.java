package com.github.simplyzetax.imaginary.elements.math.Integer;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;

public class LessThan extends Element {
    public LessThan(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Less Than";
    }

    public String getInternalName() {
        return "computation-lt";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.IRON_NUGGET;
    }

    public String[] getDescription() {
        return new String[] { "Allows you to check if one value", "is less than the other." };
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[] { new Argument("valueOne", "1st Value", DataType.NUMBER, elementInfo), new Argument("valueTwo", "2nd Value", DataType.NUMBER, elementInfo) };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[] { new OutcomingVariable("result", "Result", DataType.BOOLEAN, elementInfo) };
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] { (Child)new DefaultChild(elementInfo, "next") };
    }

    public void run(ElementInfo info, ScriptInstance instance) {
    try {
        final Number valueOne = (Number) getArguments(info)[0].getValue(instance);
        final Number valueTwo = (Number) getArguments(info)[1].getValue(instance);
        getOutcomingVariables(info)[0].register(instance, new DataRequester() {
            public Object request() {
                if (valueOne.longValue() < valueTwo.longValue())
                    return Boolean.valueOf(true);
                return Boolean.valueOf(false);
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