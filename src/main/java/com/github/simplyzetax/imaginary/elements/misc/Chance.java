package com.github.simplyzetax.imaginary.elements.misc;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.entity.Player;

public class Chance extends Element {

    public Chance(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public ScriptInstance scriptInstance;

    @Override
    public String getName() {
        return "Chance";
    }

    @Override
    public String getInternalName() {
        return "chance";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.LIGHTNING_ROD;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Executes one of two actions based on a specified probability.",
                "The first action is executed with the given chance, and the second if the chance is not met."
        };
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("chance", "Chance", DataType.DOUBLE, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{};
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{
                new Child(elementInfo, "left") {
                    @Override
                    public String getName() {
                        return "Action 1";
                    }

                    @Override
                    public String[] getDescription() {
                        return new String[]{"Executed when the random chance succeeds."};
                    }

                    @Override
                    public XMaterial getIcon() {
                        return XMaterial.GREEN_STAINED_GLASS_PANE;
                    }
                },
                new Child(elementInfo, "right") {
                    @Override
                    public String getName() {
                        return "Action 2";
                    }

                    @Override
                    public String[] getDescription() {
                        return new String[]{"Executed when the random chance fails."};
                    }

                    @Override
                    public XMaterial getIcon() {
                        return XMaterial.RED_STAINED_GLASS_PANE;
                    }
                }
        };
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        double chance;

        try {
            chance = Double.parseDouble(getArguments(elementInfo)[0].getValue(scriptInstance).toString());
        } catch (NumberFormatException e) {
            UltraCustomizer.getInstance().log("Invalid chance value. Please ensure it's a decimal between 0.0 and 1.0.");
            return;
        }

        if (chance < 0.0 || chance > 1.0) {
            UltraCustomizer.getInstance().log("Chance value out of range. Must be between 0.0 and 1.0.");
            return;
        }

        if (Math.random() < chance) {
            getConnectors(elementInfo)[0].run(scriptInstance); // Execute "Action 1"
        } else {
            getConnectors(elementInfo)[1].run(scriptInstance); // Execute "Action 2"
        }
    }
}
