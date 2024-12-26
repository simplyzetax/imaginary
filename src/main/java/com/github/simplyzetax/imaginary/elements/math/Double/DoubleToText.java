package com.github.simplyzetax.imaginary.elements.math.Double;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;

public class DoubleToText extends Element {

    public DoubleToText(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Double To Text";
    }

    public String getInternalName() {
        return "double-to-text";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.CHISELED_BOOKSHELF;
    }

    public String[] getDescription() {
        return new String[] {
                "Converts a double to a text"
        };
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[] {
                new Argument("double", "Double", DataType.DOUBLE, elementInfo)
        };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[] {
                new OutcomingVariable("text", "Text", DataType.STRING, elementInfo)
        };
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] { new DefaultChild(elementInfo, "next") };
    }

    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {

        try {
            final Double doubleValue = (Double) getArguments(elementInfo)[0].getValue(scriptInstance);
            final String textValue = doubleValue.toString();
            getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return textValue;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return null;
                }
            });
        }

        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}