package com.github.simplyzetax.imaginary.elements.misc;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;

public class InterruptThread extends Element {

    public InterruptThread(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Interrupt Thread";
    }

    @Override
    public String getInternalName() {
        return "interrupt-thread";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.WAXED_CHISELED_COPPER;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Interrupts the current thread.",
                "No more elements can be run after this action."
        };
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("thread-id", "Thread ID", DataType.NUMBER, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
        };
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{
                new Child(elementInfo, "interrupted") {
                    @Override
                    public String getName() {
                        return "THREAD INTERRUPTED";
                    }

                    @Override
                    public String[] getDescription() {
                        return new String[]{"You \u00A7ccannot\u00A77 run any elements after this connector.",
                                "No more elements will be executed after this connector."
                        };
                    }

                    @Override
                    public XMaterial getIcon() {
                        return XMaterial.TNT;
                    }
                }
        };
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {

        Long threadId = (Long) getArguments(elementInfo)[0].getValue(scriptInstance);

        Thread thread = Thread.getAllStackTraces().keySet().stream().filter(t -> t.getId() == threadId).findFirst().orElse(null);
        if (thread == null) {
            return;
        }

        UltraCustomizer.getInstance().log("Interrupting thread with ID " + threadId + ".");

        thread.interrupt();

        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}