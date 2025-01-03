package com.github.simplyzetax.imaginary.elements.math.Double;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;

public class Add extends Element {
    public Add(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Add (Double)";
    }

    public String getInternalName() {
        return "computation-add-double";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.EMERALD;
    }

    public String[] getDescription() {
        return new String[] { "Allows you to add two double values." };
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[] {
            new Argument("valueOne", "1st Value", DataType.DOUBLE, elementInfo),
            new Argument("valueTwo", "2nd Value", DataType.DOUBLE, elementInfo)
        };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[] {
            new OutcomingVariable("result", "Result", DataType.DOUBLE, elementInfo)
        };
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] {
            new DefaultChild(elementInfo, "next")
        };
    }

    public void run(ElementInfo info, ScriptInstance instance) {
        try {
            Double valueOne = (Double) getArguments(info)[0].getValue(instance);
            Double valueTwo = (Double) getArguments(info)[1].getValue(instance);
            final Double result = valueOne + valueTwo;
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