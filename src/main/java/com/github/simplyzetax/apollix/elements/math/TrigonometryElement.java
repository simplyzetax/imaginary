package com.github.simplyzetax.apollix.elements.math;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;

public class TrigonometryElement extends Element {

    public TrigonometryElement(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Trigonometry Calculation";
    }

    @Override
    public String getInternalName() {
        return "trigonometry-calculation";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Performs a trigonometric calculation (sin, cos, tan)."};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("angle", "Angle (in degrees)", DataType.DOUBLE, elementInfo),
                new Argument("operation", "Operation (sin, cos, tan)", DataType.STRING, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("result", "Result", DataType.DOUBLE, elementInfo)
        };
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        // Get input values
        double angle = Double.parseDouble(getArguments(elementInfo)[0].getValue(scriptInstance).toString());
        String operation = getArguments(elementInfo)[1].getValue(scriptInstance).toString();

        // Convert angle from degrees to radians
        double radians = Math.toRadians(angle);

        // Perform the selected operation
        double result;
        switch (operation.toLowerCase()) {
            case "sin":
                result = Math.sin(radians);
                break;
            case "cos":
                result = Math.cos(radians);
                break;
            case "tan":
                result = Math.tan(radians);
                break;
            default:
                throw new IllegalArgumentException("Invalid operation: " + operation + ". Use 'sin', 'cos', or 'tan'.");
        }

        // Store the result in an outcoming variable
        getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            public Object request() {
                return result;
            }
        });

        // Continue the script
        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}