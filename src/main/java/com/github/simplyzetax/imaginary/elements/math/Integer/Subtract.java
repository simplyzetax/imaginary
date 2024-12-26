package com.github.simplyzetax.imaginary.elements.math.Integer;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;

public class Subtract extends Element {
    public Subtract(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Subtract";
    }

    public String getInternalName() {
        return "computation-subtract";
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
        return new Argument[] { new Argument("valueOne", "1st Value", DataType.NUMBER, elementInfo), new Argument("valueTwo", "2nd Value", DataType.NUMBER, elementInfo) };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[] { new OutcomingVariable("result", "Result", DataType.NUMBER, elementInfo) };
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] { (Child)new DefaultChild(elementInfo, "next") };
    }

    public void run(ElementInfo info, ScriptInstance instance) {
        try {
            Long valueOne = (Long)getArguments(info)[0].getValue(instance);
            Long valueTwo = (Long)getArguments(info)[1].getValue(instance);
            final Long result = Long.valueOf(valueOne.longValue() - valueTwo.longValue());
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
