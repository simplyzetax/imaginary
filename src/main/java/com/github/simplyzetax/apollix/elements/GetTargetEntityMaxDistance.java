package com.github.simplyzetax.apollix.elements;

import com.github.simplyzetax.apollix.data.EntityStore;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.List;

public class GetTargetEntityMaxDistance extends Element {
    public GetTargetEntityMaxDistance(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Get Target Entity NEW";
    }

    public String getInternalName() {
        return "get-target-entity";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.SPYGLASS;
    }

    public String[] getDescription() {
        return new String[] { "Gets the target entity" };
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[] { new Argument("player", "Player", DataType.PLAYER, elementInfo) };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[] {
                new OutcomingVariable("x", "X", DataType.NUMBER, elementInfo),
                new OutcomingVariable("y", "Y", DataType.NUMBER, elementInfo),
                new OutcomingVariable("z", "Z", DataType.NUMBER, elementInfo),
                new OutcomingVariable("distance", "Distance", DataType.NUMBER, elementInfo),
                new OutcomingVariable("found", "Found entity", DataType.BOOLEAN, elementInfo),
                new OutcomingVariable("entity_uuid", "Entity UUID", DataType.STRING, elementInfo)
        };
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[] { new DefaultChild(elementInfo, "next") };
    }

    public LivingEntity getTarget(Player player) {
        int range = 60;
        List<Entity> nearbyE = player.getNearbyEntities(range, range, range);
        ArrayList<LivingEntity> livingE = new ArrayList<>();

        for (Entity e : nearbyE) {
            if (e instanceof LivingEntity) {
                livingE.add((LivingEntity)e);
            }
        }

        LivingEntity target = null;
        BlockIterator bItr = new BlockIterator(player, range);

        while (bItr.hasNext()) {
            Block block = bItr.next();
            int bx = block.getX();
            int by = block.getY();
            int bz = block.getZ();

            for (LivingEntity e : livingE) {
                Location loc = e.getLocation();
                double ex = loc.getX();
                double ey = loc.getY();
                double ez = loc.getZ();
                if (bx - 0.75D <= ex && ex <= bx + 1.75D &&
                        bz - 0.75D <= ez && ez <= bz + 1.75D &&
                        (by - 1) <= ey && ey <= by + 2.5D) {
                    target = e;
                }
            }
        }

        return target;
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

            EntityStore.entities.put(target.getUniqueId(), target);

            final Location entityLoc = target.getLocation();
            final double distance = player.getLocation().distance(entityLoc);

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
                    return target.getUniqueId().toString();
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
                    return "";
                }
            });

        }
        getConnectors(elementInfo)[0].run(scriptInstance);
    }
}