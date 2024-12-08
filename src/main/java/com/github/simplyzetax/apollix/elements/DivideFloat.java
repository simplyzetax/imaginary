package com.github.simplyzetax.apollix.elements;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;

public class DivideFloat extends Element {
    public DivideFloat(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Divide Float";
    }

    public String getInternalName() {
        return "computation-divide-float";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.IRON_INGOT;
    }

    public String[] getDescription() {
        return new String[]{"Allows you to divide two values (float)."};
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{new Argument("valueOne", "1st Value", DataType.NUMBER, elementInfo), new Argument("valueTwo", "2nd Value", DataType.NUMBER, elementInfo)};
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{new OutcomingVariable("result", "Result", DataType.NUMBER, elementInfo)};
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    public void run(ElementInfo info, ScriptInstance instance) {
        try {
            Float valueOne = ((Number) getArguments(info)[0].getValue(instance)).floatValue();
            Float valueTwo = ((Number) getArguments(info)[1].getValue(instance)).floatValue();
            final Float result = valueOne / valueTwo;
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
