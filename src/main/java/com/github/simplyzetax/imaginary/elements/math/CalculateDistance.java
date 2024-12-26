package com.github.simplyzetax.imaginary.elements.math;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;

public class CalculateDistance extends Element {

    public CalculateDistance(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Calculate Distance";
    }

    @Override
    public String getInternalName() {
        return "calculate-distance";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Calculates the distance between two sets of coordinates."};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("x1", "X1 Coordinate", DataType.DOUBLE, elementInfo),
                new Argument("y1", "Y1 Coordinate", DataType.DOUBLE, elementInfo),
                new Argument("z1", "Z1 Coordinate", DataType.DOUBLE, elementInfo),
                new Argument("x2", "X2 Coordinate", DataType.DOUBLE, elementInfo),
                new Argument("y2", "Y2 Coordinate", DataType.DOUBLE, elementInfo),
                new Argument("z2", "Z2 Coordinate", DataType.DOUBLE, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("distance", "Distance", DataType.DOUBLE, elementInfo)
        };
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        // Get input values
        double x1 = Double.parseDouble(getArguments(elementInfo)[0].getValue(scriptInstance).toString());
        double y1 = Double.parseDouble(getArguments(elementInfo)[1].getValue(scriptInstance).toString());
        double z1 = Double.parseDouble(getArguments(elementInfo)[2].getValue(scriptInstance).toString());
        double x2 = Double.parseDouble(getArguments(elementInfo)[3].getValue(scriptInstance).toString());
        double y2 = Double.parseDouble(getArguments(elementInfo)[4].getValue(scriptInstance).toString());
        double z2 = Double.parseDouble(getArguments(elementInfo)[5].getValue(scriptInstance).toString());

        // Calculate the distance using the Euclidean distance formula
        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));

        // Store the result in an outcoming variable
        getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            public Object request() {
                return distance;
            }
        });

        // Continue the script
        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}