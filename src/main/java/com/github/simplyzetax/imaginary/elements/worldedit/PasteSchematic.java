package com.github.simplyzetax.imaginary.elements.worldedit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.simplyzetax.imaginary.AddonMain;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PasteSchematic extends Element {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public PasteSchematic(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    public String getName() {
        return "Paste Schematic";
    }

    public String getInternalName() {
        return "paste-schematic";
    }

    public boolean isHidingIfNotCompatible() {
        return false;
    }

    public XMaterial getMaterial() {
        return XMaterial.OXIDIZED_COPPER_BULB;
    }

    public String[] getDescription() {
        return new String[]{"Pastes a schematic at the specified location."};
    }

    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("world", "World", DataType.STRING, elementInfo),
                new Argument("x", "X", DataType.DOUBLE, elementInfo),
                new Argument("y", "Y", DataType.DOUBLE, elementInfo),
                new Argument("z", "Z", DataType.DOUBLE, elementInfo),
                new Argument("schematic", "Schematic Name", DataType.STRING, elementInfo),
        };
    }

    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{};
    }

    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        System.out.println("Starting PasteSchematic...");

        String worldName = getArguments(elementInfo)[0].getValue(scriptInstance).toString();
        double x = Double.parseDouble(getArguments(elementInfo)[1].getValue(scriptInstance).toString());
        double y = Double.parseDouble(getArguments(elementInfo)[2].getValue(scriptInstance).toString());
        double z = Double.parseDouble(getArguments(elementInfo)[3].getValue(scriptInstance).toString());
        String schematicName = getArguments(elementInfo)[4].getValue(scriptInstance).toString();

        org.bukkit.World bukkitWorld = Bukkit.getWorld(worldName);
        if (bukkitWorld == null) {
            System.out.println("World '" + worldName + "' not found!");
            return;
        }

        World world = BukkitAdapter.adapt(bukkitWorld);
        Location location = new Location(bukkitWorld, x, y, z);
        BlockVector3 vector = BlockVector3.at(location.getX(), location.getY(), location.getZ());

        File schematicFile = new File(UltraCustomizer.getInstance().getServerFolder(), "plugins/FastAsyncWorldEdit/schematics/" + schematicName + ".schematic");
        if (!schematicFile.exists()) {
            System.out.println("Schematic file not found: " + schematicFile.getAbsolutePath());
            return;
        }

        ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
        if (format == null) {
            System.out.println("Unknown format for file: " + schematicFile.getAbsolutePath());
            return;
        }

        executorService.submit(() -> {
            try (ClipboardReader reader = format.getReader(new FileInputStream(schematicFile))) {
                Clipboard clipboard = reader.read();
                try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
                    Operation operation = new ClipboardHolder(clipboard)
                            .createPaste(editSession)
                            .to(vector)
                            .ignoreAirBlocks(false)
                            .build();
                    System.out.println("Executing paste operation...");
                    Operations.complete(operation);
                    System.out.println("Schematic pasted successfully!");
                }
            } catch (IOException | WorldEditException e) {
                System.err.println("Error while pasting schematic: " + e.getMessage());
                e.printStackTrace();
            }

            // Trigger the next connector after completion
            new BukkitRunnable() {
                @Override
                public void run() {
                    System.out.println("Triggering next connector...");
                    getConnectors(elementInfo)[0].run(scriptInstance);
                }
            }.runTask(AddonMain.plugin);
        });
    }
}
