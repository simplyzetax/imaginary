package com.github.simplyzetax.apollix.elements;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CreateParticleStream extends Element {

    public CreateParticleStream(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Create Particle Stream";
    }

    @Override
    public String getInternalName() {
        return "particle-stream";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.FIREWORK_STAR;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Creates a particle stream"};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("player", "Player", DataType.PLAYER, elementInfo),
                new Argument("particle", "Particle Type", DataType.STRING, elementInfo),
                new Argument("count", "Particle Count", DataType.NUMBER, elementInfo),
                new Argument("range", "Max Range", DataType.NUMBER, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[0];
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    @Override
public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
try {
    Player player = (Player) getArguments(elementInfo)[0].getValue(scriptInstance);
    String particleName = (String) getArguments(elementInfo)[1].getValue(scriptInstance);
    Number particleCount = (Number) getArguments(elementInfo)[2].getValue(scriptInstance);
    Number maxRange = (Number) getArguments(elementInfo)[3].getValue(scriptInstance);

    // Validate and parse particle type
    Particle particle;
    try {
        particle = Particle.valueOf(particleName.toUpperCase());
    } catch (IllegalArgumentException e) {
        player.sendMessage(ChatColor.RED + "Invalid particle type: " + particleName);
        return;
    }

    Location loc = player.getEyeLocation(); // Use player's eye location
    Location target = player.getTargetBlock(null, maxRange.intValue()).getLocation();

    // Calculate direction vector
    Vector direction = target.toVector().subtract(loc.toVector()).normalize();
    double distance = loc.distance(target);
    double stepSize = 0.1; // Smaller step size for more accuracy
    int steps = (int) (distance / stepSize);

    // Spawn particles along the path
    for (int i = 0; i < steps; i++) {
        player.spawnParticle(particle, loc, particleCount.intValue(), 0, 0, 0, 0);
        loc.add(direction.clone().multiply(stepSize));
    }

    // Optional: spawn end particles
    player.spawnParticle(Particle.END_ROD, target, 5);

} catch (Exception e) {
    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error in Particle Stream: " + e.getMessage());
    e.printStackTrace();
}

    // Run next connector
    getConnectors(elementInfo)[0].run(scriptInstance);
}
}