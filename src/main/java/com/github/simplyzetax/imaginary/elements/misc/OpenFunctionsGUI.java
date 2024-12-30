package com.github.simplyzetax.imaginary.elements.misc;

import me.TechsCode.UltraCustomizer.Folder;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.gui.Overview;
import me.TechsCode.UltraCustomizer.scriptSystem.elements.constructors.Function;
import me.TechsCode.UltraCustomizer.scriptSystem.gui.FunctionBrowser;
import me.TechsCode.UltraCustomizer.scriptSystem.gui.ScriptList;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.pickers.BlockPicker;
import org.bukkit.entity.Player;

public class OpenFunctionsGUI extends Element {

    public OpenFunctionsGUI(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Open functions gui";
    }

    @Override
    public String getInternalName() {
        return "open_functions_gui";
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
                "Just opens the functions gui."
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
        return new OutcomingVariable[]{};
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
            Player player = (Player) getArguments(elementInfo)[0].getValue(scriptInstance);

            Folder folder = elementInfo.getFolder();

        FunctionBrowser b = new FunctionBrowser(player, UltraCustomizer.getInstance(), folder) {
            @Override
            public void onBack() {
                new Overview(player, UltraCustomizer.getInstance());
            }

            @Override
            public void select(Function function, ElementInfo elementInfo) {

            }
        };

        getConnectors(elementInfo)[0].run(scriptInstance);

    }
}
