package com.github.simplyzetax.apollix.events;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.Argument;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.Constructor;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.DataRequester;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.ElementInfo;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.OutcomingVariable;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.ScriptInstance;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class RightClickPlayer extends Constructor {
    public RightClickPlayer(UltraCustomizer UC) {
        super(UC);
    }

    public String getName() {
        return "Right Click Player";
    }

    public String getInternalName() {
        return "right-click-player";
    }

    public XMaterial getMaterial() {
        return XMaterial.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE;
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[0];
    }

    public String[] getDescription() {
        return new String[] { "Will be executed when a player right clicks another player." };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[] {
            new OutcomingVariable("event", "Event", DataType.CANCELLABLE_EVENT, elementInfo),
            new OutcomingVariable("player", "Player", DataType.PLAYER, elementInfo),
            new OutcomingVariable("target", "Target", DataType.PLAYER, elementInfo)
        };
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Player) {
            call(e);
        }
    }

    public void call(PlayerInteractEntityEvent e) {
        call(elementInfo -> {
            ScriptInstance instance = new ScriptInstance();

            getOutcomingVariables(elementInfo)[0].register(instance, new DataRequester() {
                public Object request() {
                    return e;
                }
            });

            getOutcomingVariables(elementInfo)[1].register(instance, new DataRequester() {
                public Object request() {
                    return e.getPlayer();
                }
            });

            if (e.getRightClicked() instanceof Player) {
                UltraCustomizer.getInstance().log("Right clicked player");
                getOutcomingVariables(elementInfo)[2].register(instance, new DataRequester() {
                    public Object request() {
                        return (Player) e.getRightClicked();
                    }
                });
                getConnectors(elementInfo)[0].run(instance);
            } else {
                getOutcomingVariables(elementInfo)[2].register(instance, new DataRequester() {
                    public Object request() {
                        return null;
                    }
                });
            }
            return instance;
        });
    }

    public boolean isUnlisted() {
        return false;
    }
}