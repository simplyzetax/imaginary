package com.github.simplyzetax.imaginary.constructors;

import com.github.simplyzetax.imaginary.AddonMain;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.Argument;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.Constructor;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.ElementInfo;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.OutcomingVariable;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.ScriptInstance;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.scheduler.BukkitRunnable;

public class RepeatingTick extends Constructor {
    public RepeatingTick(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public void onEnable(ElementInfo elementInfo) {
        Argument delayArgument = elementInfo.getElement().getArguments(elementInfo)[0];
        Object obj = delayArgument.getValue(new ScriptInstance());
        if (obj instanceof Long) {
            long ticks = (Long) obj;

            // Schedule the task based on the tick argument
            new BukkitRunnable() {
                @Override
                public void run() {
                    getConnectors(elementInfo)[0].run(new ScriptInstance());
                }
            }.runTaskTimer(AddonMain.plugin, 0L, ticks); // Use `ticks` as the interval
        }
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
