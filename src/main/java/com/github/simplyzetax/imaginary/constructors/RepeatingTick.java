package com.github.simplyzetax.imaginary.constructors;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.Argument;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.Constructor;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.ElementInfo;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.OutcomingVariable;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.ScriptInstance;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;

public class RepeatingTick extends Constructor {
    public RepeatingTick(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
        this.plugin.getScheduler().runTaskTimer(() -> call((elementInfo) -> {
            ScriptInstance instance = new ScriptInstance();
            Object obj = elementInfo.getElement().getArguments(elementInfo)[0].getValue(instance);
            if (obj == null) {
                return null;
            } else {
                long ticks = (Long) obj;
                if (System.currentTimeMillis() % (ticks * 50) == 0L) {
                    getConnectors(elementInfo)[0].run(instance);
                }
                return instance;
            }
        }), 0L, 1L);
    }

    public String getName() {
        return "On Tick";
    }

    public String getInternalName() {
        return "on-tick";
    }

    public XMaterial getMaterial() {
        return XMaterial.MUSIC_DISC_CREATOR;
    }

    public String[] getDescription() {
        return new String[] { "Will be executed every x ticks" };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[0];
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[] { new Argument("delay", "Delay", DataType.TICKS, elementInfo) };
    }

    public boolean isUnlisted() {
        return false;
    }
}