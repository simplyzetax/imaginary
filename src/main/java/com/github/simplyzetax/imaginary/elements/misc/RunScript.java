package com.github.simplyzetax.imaginary.elements.misc;

import me.TechsCode.UltraCustomizer.Folder;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.gui.FunctionBrowser;
import me.TechsCode.UltraCustomizer.scriptSystem.gui.ScriptList;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.entity.Player;

public class RunScript extends Element {

    public RunScript(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Run Script (NOT WORKING)";
    }

    @Override
    public String getInternalName() {
        return "run_script";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.WAXED_OXIDIZED_CUT_COPPER;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Runs the provided script."
        };
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("script", "Script", DataType.STRING, elementInfo),
                new Argument("player", "Player", DataType.PLAYER, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("found", "Found", DataType.BOOLEAN, elementInfo)
        };
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        try {
            // Retrieve arguments
            String scriptName = (String) getArguments(elementInfo)[0].getValue(scriptInstance);
            Player player = (Player) getArguments(elementInfo)[1].getValue(scriptInstance);

            // Retrieve the script folder and list
            Folder folder = elementInfo.getFolder();
            ScriptList scriptList = folder.getScripts();

            // Attempt to find the script by name
            Script foundScript = scriptList.stream()
                    .filter(s -> s.getName().equalsIgnoreCase(scriptName))
                    .findFirst()
                    .orElse(null);

            if (foundScript == null) {
                // Script not found: set "found" to false and exit
                getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                    @Override
                    public Object request() {
                        return false;
                    }
                });
                getConnectors(elementInfo)[0].run(scriptInstance);
                return;
            }

            // Prepare to execute the script
            ElementInfo firstElementInfo = foundScript.getFirstElement();
            if (firstElementInfo == null || firstElementInfo.getElement() == null) {
                // Invalid script structure
                getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                    @Override
                    public Object request() {
                        return false;
                    }
                });
                getConnectors(elementInfo)[0].run(scriptInstance);
                return;
            }

            // Log script execution
            UltraCustomizer.getInstance().log("Running script '" + scriptName + "' with first element: "
                    + firstElementInfo.getElement().getName());

            // Set up variable for the player
            Variable playerVariable = new Variable("player");
            playerVariable.setInstance(scriptInstance);
            scriptInstance.setRequester(playerVariable, new DataRequester() {
                @Override
                public Object request() {
                    return player; // Ensure this matches the player's reference
                }
            });

            // Run the first element
            firstElementInfo.getElement().run(firstElementInfo, scriptInstance);

            // Set "found" to true and run the next connector
            getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                @Override
                public Object request() {
                    return true;
                }
            });

            getConnectors(elementInfo)[0].run(scriptInstance);

        } catch (Exception e) {
            // Log unexpected errors
            UltraCustomizer.getInstance().log("Error while running script: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
