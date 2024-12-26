package com.github.simplyzetax.apollix.elements.entities;

import com.github.simplyzetax.apollix.AddonMain;
import com.github.simplyzetax.apollix.specifications.EntitySpecification;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.elements.ExecuteForAllPlayers;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class ForAllNearbyEntities extends Element {

    public ForAllNearbyEntities(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "For all nearby entities";
    }

    public String getInternalName() {
        return "for-all-nearby-entities";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.NETHERITE_UPGRADE_SMITHING_TEMPLATE;
    }

    public String[] getDescription() {
        return new String[]{"Executes the child elements for all nearby entities"};
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("player", "Player", DataType.PLAYER, elementInfo),
                new Argument("range", "Range", DataType.NUMBER, elementInfo),
        };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[] {
                new OutcomingVariable("entity-sepc", "Entity",DataType.getCustomDataType("entityspecification"), elementInfo)
        };
    }

    public Child[] getConnectors(ElementInfo var1) {
        return new Child[]{new Child(var1, "next") {
            public String getName() {
                return"Next Elements";
            }

            public String[] getDescription() {
                return new String[]{"All of the next Elements", "will be executed for every entity in the specified range"};
            }

            public XMaterial getIcon() {
                return XMaterial.YELLOW_STAINED_GLASS_PANE;
            }
        }};
    }

    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
    try {
        // Retrieve and parse the range argument
        int range = Integer.parseInt(getArguments(elementInfo)[1].getValue(scriptInstance).toString());

        // Retrieve the player argument and get the player entity
        Player player = (Player) getArguments(elementInfo)[0].getValue(scriptInstance);

        // Find all nearby entities within the specified range
        List<LivingEntity> nearbyEntities = player.getNearbyEntities(range, range, range).stream()
                .filter(entity -> entity instanceof LivingEntity)
                .map(entity -> (LivingEntity) entity)
                .toList();

        // Execute the child elements for each nearby entity
        for (LivingEntity entity : nearbyEntities) {

            AddonMain.plugin.getLogger().info("Running for entity: " + entity.getName());

            EntitySpecification entitySpec = new EntitySpecification(entity);
            getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return entitySpec.serialize();
                }
            });

            getConnectors(elementInfo)[0].run(scriptInstance);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}