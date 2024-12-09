package com.github.simplyzetax.apollix.elements.entities;

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
        return new String[]{"Creates a projectile"};
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
                    handleArrowProjectile(elementInfo, scriptInstance, player, velocity, data);
                    break;
                case "SNOWBALL":
                    handleSnowballProjectile(elementInfo, scriptInstance, player, velocity, data);
                    break;
                case "INVISIBLE":
                    handleInvisibleProjectile(elementInfo, scriptInstance, player, velocity, data);
                    break;
                default:
                    player.sendMessage(ChatColor.RED + "Invalid projectile type!");
                    break;
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error: " + e.getMessage());
        }

        // Run next connector
        getConnectors(elementInfo)[0].run(scriptInstance);
    }

    private void handleArrowProjectile(ElementInfo elementInfo, ScriptInstance scriptInstance, Player player, Number velocity, String data) {
        Arrow arrow = player.launchProjectile(Arrow.class);
        arrow.setVelocity(player.getLocation().getDirection().multiply(velocity.doubleValue()));
        arrow.setCustomName(data);
        registerProjectileUID(elementInfo, scriptInstance, arrow);
    }

    private void handleSnowballProjectile(ElementInfo elementInfo, ScriptInstance scriptInstance, Player player, Number velocity, String data) {
        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setVelocity(player.getLocation().getDirection().multiply(velocity.doubleValue()));
        snowball.setCustomName(data);
        registerProjectileUID(elementInfo, scriptInstance, snowball);
    }

    private void handleInvisibleProjectile(ElementInfo elementInfo, ScriptInstance scriptInstance, Player player, Number velocity, String data) {
        Arrow invisible = player.launchProjectile(Arrow.class);
        invisible.setVelocity(player.getLocation().getDirection().multiply(velocity.doubleValue()));
        invisible.setCustomName(data);
        invisible.setCustomNameVisible(false);
        invisible.setSilent(true);
        registerProjectileUID(elementInfo, scriptInstance, invisible);
    }

    private void registerProjectileUID(ElementInfo elementInfo, ScriptInstance scriptInstance, org.bukkit.entity.Projectile projectile) {
        getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            @Override
            public Object request() {
                return projectile.getUniqueId().toString();
            }
        });
    }
}