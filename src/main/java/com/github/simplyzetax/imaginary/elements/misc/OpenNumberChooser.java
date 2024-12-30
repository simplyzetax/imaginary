package com.github.simplyzetax.imaginary.elements.misc;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.base.misc.Callback;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.pickers.DoubleChooser;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.pickers.NumberChooser;
import org.bukkit.entity.Player;

public class OpenNumberChooser extends Element {

    public OpenNumberChooser(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Open number chooser";
    }

    @Override
    public String getInternalName() {
        return "open-number-chooser";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.WAXED_COPPER_BULB;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Just opens the number chooser gui for the provided player."
        };
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("player", "Player", DataType.PLAYER, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("value", "Value", DataType.DOUBLE, elementInfo)
        };
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        Player player = (Player) getArguments(elementInfo)[0].getValue(scriptInstance);

        Callback<Object> callback = new Callback<>() {
            @Override
            public void run(Object o) {
                Double value = (Double) o;

                getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                    public Object request() {
                        return value;
                    }
                });

                player.closeInventory();

                getConnectors(elementInfo)[0].run(scriptInstance);
            }
        };

        NumberChooser numberChooser = new NumberChooser(player, UltraCustomizer.getInstance(), "Choose a number", callback);
    }
}