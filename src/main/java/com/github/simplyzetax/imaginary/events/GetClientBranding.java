package com.github.simplyzetax.imaginary.events;

import com.github.simplyzetax.imaginary.AddonMain;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.item.XMaterial;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.*;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

public class GetClientBranding extends Element implements PluginMessageListener {

    private final HashMap<UUID, String> playerBrands = new HashMap<>();

    public GetClientBranding(UltraCustomizer ultraCustomizer) {
        super(ultraCustomizer);
        Bukkit.getMessenger().registerIncomingPluginChannel(AddonMain.plugin, "minecraft:brand", this);
    }

    @Override
    public String getName() {
        return "Get Client Branding";
    }

    @Override
    public String getInternalName() {
        return "get-client-branding";
    }

    @Override
    public boolean isHidingIfNotCompatible() {
        return false;
    }

    @Override
    public XMaterial getMaterial() {
        return XMaterial.SMITHING_TABLE;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Retrieves the client branding of a player"};
    }

    @Override
    public Argument[] getArguments(ElementInfo elementInfo) {
        return new Argument[]{
                new Argument("player", "Player", DataType.PLAYER, elementInfo)
        };
    }

    @Override
    public OutcomingVariable[] getOutcomingVariables(ElementInfo elementInfo) {
        return new OutcomingVariable[]{
                new OutcomingVariable("branding", "Branding", DataType.STRING, elementInfo)
        };
    }

    @Override
    public Child[] getConnectors(ElementInfo elementInfo) {
        return new Child[]{new DefaultChild(elementInfo, "next")};
    }

    @Override
    public void run(ElementInfo elementInfo, ScriptInstance scriptInstance) {
        Player player = (Player) getArguments(elementInfo)[0].getValue(scriptInstance);

        String branding = playerBrands.getOrDefault(player.getUniqueId(), "Unknown");
        getOutcomingVariables(elementInfo)[0].register(scriptInstance, new DataRequester() {
            public Object request() {
                return branding;
            }
        });

        // Run the next connected element
        getConnectors(elementInfo)[0].run(scriptInstance);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        UltraCustomizer.getInstance().log("Received client branding message");
        if (channel.equals("minecraft:brand")) {
            String brand = new String(message, StandardCharsets.UTF_8).substring(1); // Decode and remove padding
            playerBrands.put(player.getUniqueId(), brand);
        } else {
            UltraCustomizer.getInstance().log("Received unknown plugin message: " + channel);
            UltraCustomizer.getInstance().log("Message: " + new String(message, StandardCharsets.UTF_8));
        }
    }
}
