package com.github.simplyzetax.apollix.events;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

public class RightClick extends Constructor {

    public RightClick(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRightClick(PlayerInteractEvent e) {
        try {
            call(e);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void call(final PlayerInteractEvent e) {
        call(elementInfo -> {
            ScriptInstance instance = new ScriptInstance();
            getOutcomingVariables(elementInfo)[0].register(instance, new DataRequester() {
                public Object request() {
                    Player player = (Player)e.getPlayer();
                    return player;
                    }
                });
            return instance;
        });
    }

    @Override
    public String getName() {
        return "On Right Click";
    }

    @Override
    public String getInternalName() {
        return "on-right-click"; // Fixed internal name to match the event
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.MUSIC_DISC_13;
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Will be executed when player",
                "does a right click"
        };
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[0];
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[] { new OutcomingVariable("player", "Player", DataType.PLAYER, elementInfo) };
    }

    @Override
    public boolean isUnlisted() {
        return false;
    }
}