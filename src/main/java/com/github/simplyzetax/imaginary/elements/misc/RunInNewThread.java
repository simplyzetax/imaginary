package com.github.simplyzetax.imaginary.elements.misc;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;

public class RunInNewThread extends Element {

    public RunInNewThread(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public ScriptInstance scriptInstance;

    @Override
    public String getName() {
        return "Run in New Thread";
    }

    @Override
    public String getInternalName() {
        return "run-in-new-thread";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.OXIDIZED_CHISELED_COPPER;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Runs the following actions in a new thread.",
                "This allows the actions to run in parallel with the main thread.",
                "You can optionally specify a delay before the actions are run."
        };
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("delay", "Delay", DataType.NUMBER, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{};
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{
                new Child(elementInfo, "right") {
                    @Override
                    public String getName() {
                        return "Thread actions";
                    }

                    @Override
                    public String[] getDescription() {
                        return new String[]{"The actions to run in a new thread."};
                    }

                    @Override
                    public XMaterial getIcon() {
                        return XMaterial.PINK_STAINED_GLASS_PANE;
                    }
                },
                new Child(elementInfo, "left") {
                    @Override
                    public String getName() {
                        return "Main thread actions";
                    }

                    @Override
                    public String[] getDescription() {
                        return new String[]{"The actions to run in the main thread."};
                    }

                    @Override
                    public XMaterial getIcon() {
                        return XMaterial.BLUE_STAINED_GLASS_PANE;
                    }
                }
        };
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        // Run the "left" connector in the main thread
        getConnectors(elementInfo)[1].run(scriptInstance);

        // Run the "right" connector in a new thread
        new Thread(() -> {
            try {
                // Retrieve and parse the delay argument
                Object delayObj = getArguments(elementInfo)[0].getValue(scriptInstance);
                long delay = delayObj != null ? Long.parseLong(delayObj.toString()) : 0;

                // Apply the delay if specified
                if (delay > 0) {
                    Thread.sleep(delay);
                }

                // Run the next connector
                getConnectors(elementInfo)[0].run(scriptInstance);
            } catch (NumberFormatException | InterruptedException e) {
                UltraCustomizer.getInstance().log("Invalid delay value. Please ensure it's a valid number.");
            }
        }).start();
    }
}