package com.github.simplyzetax.imaginary.elements.entities;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;

public class CreateProjectile extends Element {

    public CreateProjectile(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
    }

    @Override
    public String getName() {
        return "Create Projectile";
    }

    @Override
    public String getInternalName() {
        return "create-projectile";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.ARROW;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Creates a projectile", "and launches it from the player", "Possible types: ARROW, SNOWBALL, INVISIBLE"};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("type", "Type", DataType.STRING, elementInfo),
                new Argument("velocity", "Velocity", DataType.DOUBLE, elementInfo),
                new Argument("player", "Player", DataType.PLAYER, elementInfo),
                new Argument("data", "Data", DataType.STRING, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("uid", "UID", DataType.STRING, elementInfo)
        };
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        try {
            String type = (String) getArguments(elementInfo)[0].getValue(scriptInstance);
            Number velocity = (Number) getArguments(elementInfo)[1].getValue(scriptInstance);
            Player player = (Player) getArguments(elementInfo)[2].getValue(scriptInstance);
            String data = (String) getArguments(elementInfo)[3].getValue(scriptInstance);

            switch (type) {
                case "ARROW":
                    launchProjectile(player, Arrow.class, velocity, data, elementInfo, scriptInstance);
                    break;
                case "SNOWBALL":
                    launchProjectile(player, Snowball.class, velocity, data, elementInfo, scriptInstance);
                    break;
                case "INVISIBLE":
                    Arrow invisible = player.launchProjectile(Arrow.class);
                    invisible.setVelocity(player.getLocation().getDirection().multiply(velocity.doubleValue()));
                    invisible.setCustomName(data);
                    invisible.setCustomNameVisible(false);
                    invisible.setSilent(true);
                    registerProjectileUID(elementInfo, scriptInstance, invisible);
                    break;
                default:
                    player.sendMessage(ChatColor.RED + "Invalid projectile type!");
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error: " + e.getMessage());
        }
        getConnectors(elementInfo)[0].run(scriptInstance);
    }

    private <T extends org.bukkit.entity.Projectile> void launchProjectile(Player player, Class<T> projectileClass, Number velocity, String data, ElementInfo elementInfo, ScriptInstance scriptInstance) {
        T projectile = player.launchProjectile(projectileClass);
        projectile.setVelocity(player.getLocation().getDirection().multiply(velocity.doubleValue()));
        projectile.setCustomName(data);
        registerProjectileUID(elementInfo, scriptInstance, projectile);
    }

    private void registerProjectileUID(ElementInfo elementInfo, ScriptInstance scriptInstance, org.bukkit.entity.Projectile projectile) {
        getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            public Object request() {
                return projectile.getUniqueId().toString();
            }
        });
    }
}