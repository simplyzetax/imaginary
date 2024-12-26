package com.github.simplyzetax.imaginary.elements.entities;

import com.github.simplyzetax.imaginary.specifications.entity.Specification;
import com.github.simplyzetax.imaginary.specifications.entity.Extension;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.List;

public class GetTargetEntity extends Element {
    public GetTargetEntity(UltraCustomizer ultraCustomizer) {

        super(ultraCustomizer);
        DataType.registerCustomDataType("entityspecification", new Extension());
    }

    public String getName() {
        return "Get Target Entity";
    }

    public String getInternalName() {
        return "get-target-entity";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.EYE_ARMOR_TRIM_SMITHING_TEMPLATE;
    }

    public String[] getDescription() {
        return new String[]{"Gets the target entity", "Max range based on server view distance", "Returns entity location, distance, and UUID", "Returns false if no entity found", "Current max range: " + (Bukkit.getViewDistance() * 16) + " blocks"};
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{new Argument("player", "Player", DataType.PLAYER, elementInfo)};
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("x", "X", DataType.DOUBLE, elementInfo),
                new OutcomingVariable("y", "Y", DataType.DOUBLE, elementInfo),
                new OutcomingVariable("z", "Z", DataType.DOUBLE, elementInfo),
                new OutcomingVariable("distance", "Distance", DataType.DOUBLE, elementInfo),
                new OutcomingVariable("found", "Found entity", DataType.BOOLEAN, elementInfo),
                new OutcomingVariable("entity-new", "Entity NEW", DataType.getCustomDataType("entityspecification"), elementInfo)
        };
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    public LivingEntity getTarget(Player player) {
        int distance = Bukkit.getViewDistance() * 16;
        List<Entity> nearbyEntities = player.getNearbyEntities(distance, distance, distance);
        List<LivingEntity> livingEntities = new ArrayList<>();

        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity) {
                livingEntities.add((LivingEntity) entity);
            }
        }

        BlockIterator blockIterator = new BlockIterator(player, distance);
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            int bx = block.getX();
            int by = block.getY();
            int bz = block.getZ();

            for (LivingEntity livingEntity : livingEntities) {
                Location loc = livingEntity.getLocation();
                double ex = loc.getX();
                double ey = loc.getY();
                double ez = loc.getZ();

                if (bx - 0.75D <= ex && ex <= bx + 1.75D &&
                        bz - 0.75D <= ez && ez <= bz + 1.75D &&
                        (by - 1) <= ey && ey <= by + 2.5D &&
                        player.hasLineOfSight(livingEntity)) {
                    return livingEntity;
                }
            }
        }

        return null;
    }

    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        Argument playerArgument = getArguments(elementInfo)[0];
        if (playerArgument == null || playerArgument.getValue(scriptInstance) == null) {
            getOutcomingVariables(elementInfo)[4].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return false;
                }
            });
            getConnectors(elementInfo)[0].run(scriptInstance);
            return;
        }

        Player player = (Player) playerArgument.getValue(scriptInstance);

        try {
            LivingEntity target = getTarget(player);
            if (target == null) {
                throw new Exception("No target found");
            }

            final Location entityLoc = target.getLocation();
            final long distance = (long) player.getLocation().distance(entityLoc);

            getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return entityLoc.getX();
                }
            });
            getOutcomingVariables(elementInfo)[1].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return entityLoc.getY();
                }
            });
            getOutcomingVariables(elementInfo)[2].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return entityLoc.getZ();
                }
            });
            getOutcomingVariables(elementInfo)[3].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return distance;
                }
            });
            getOutcomingVariables(elementInfo)[4].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return true;
                }
            });
            getOutcomingVariables(elementInfo)[5].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return new Specification(target).serialize();
                }
            });
        } catch (Exception e) {

            getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return 0;
                }
            });

            getOutcomingVariables(elementInfo)[1].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return 0;
                }
            });

            getOutcomingVariables(elementInfo)[2].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return 0;
                }
            });

            getOutcomingVariables(elementInfo)[3].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return 0;
                }
            });

            getOutcomingVariables(elementInfo)[4].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return false;
                }
            });

            getOutcomingVariables(elementInfo)[5].register(scriptInstance, new DataRequester() {
                public Object request() {
                    return new Extension().serialize(new Specification(null));
                }
            });

        }
        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}