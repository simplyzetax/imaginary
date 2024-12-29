package com.github.simplyzetax.imaginary.constructors;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.ChunkLoadEvent;

public class OnChunkLoad extends Constructor {

    public OnChunkLoad(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "On Chunk Load";
    }

    public String getInternalName() {
        return "on-chunk-load";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public boolean isUnlisted() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.SEAGRASS;
    }

    public String[] getDescription() {
        return new String[]{"Executes the following actions when a chunk is loaded."};
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("world", "World", DataType.STRING, elementInfo)
        };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("event", "Event", DataType.CANCELLABLE_EVENT, elementInfo),
                new OutcomingVariable("isNewChunk", "Is New Chunk", DataType.BOOLEAN, elementInfo),
                new OutcomingVariable("x", "X", DataType.NUMBER, elementInfo),
                new OutcomingVariable("z", "Z", DataType.NUMBER, elementInfo)
        };
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChunkLoad(ChunkLoadEvent e) {
        call(e);
    }

    public void call(ChunkLoadEvent e) {
    call(elementInfo -> {
        ScriptInstance instance = new ScriptInstance();

        getOutcomingVariables(elementInfo)[0].register(instance, new DataRequester() {
            public Object request() {
                return e;
            }
        });

        getOutcomingVariables(elementInfo)[1].register(instance, new DataRequester() {
            public Object request() {
                return e.isNewChunk();
            }
        });

        getOutcomingVariables(elementInfo)[2].register(instance, new DataRequester() {
            public Object request() {
                return e.getChunk().getX();
            }
        });

        getOutcomingVariables(elementInfo)[3].register(instance, new DataRequester() {
            public Object request() {
                return e.getChunk().getZ();
            }
        });

        getConnectors(elementInfo)[0].run(instance);
        return instance;
    });
}
}