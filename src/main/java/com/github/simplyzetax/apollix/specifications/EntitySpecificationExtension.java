package com.github.simplyzetax.apollix.specifications;

import me.TechsCode.UltraCustomizer.Folder;
import me.TechsCode.UltraCustomizer.UltraCustomizer;
import me.TechsCode.UltraCustomizer.base.misc.Callback;
import me.TechsCode.UltraCustomizer.base.translations.Phrase;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.OutcomingVariable;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.ScriptInstance;
import me.TechsCode.UltraCustomizer.scriptSystem.objects.datatypes.DataTypeSpecification;
import org.bukkit.entity.Player;

public class EntitySpecificationExtension extends DataTypeSpecification {
    private static final Phrase GET_PHRASE = Phrase.create("EntitySpecification.title", "Select Entity");

    @Override
    public String getCreatePhrase() {
        return GET_PHRASE.get();
    }

    private static final Phrase GET_DESCRIPTION = Phrase.create("EntitySpecification.description", "Get the selected entity. (description)");

    @Override
    public String[] getCreateDescription() {
        return new String[]{
                GET_DESCRIPTION.get()
        };
    }

    private static final Phrase GET_DISPLAY = Phrase.create("EntitySpecification.selectedEntity", "Select Entity");

    @Override
    public String getDisplay(Object object, OutcomingVariable[] variables) {
        return GET_DISPLAY.get();
    }

    @Override
    public Object getAsValue(Object object, ScriptInstance instance, OutcomingVariable[] variables) {
        return object;
    }

    @Override
    public String serialize(Object object) {
        return ((EntitySpecification) object).serialize();
    }

    @Override
    public Object deserialize(String data, Folder folder) {
        return EntitySpecification.deserialize(data);
    }


    @Override
    public void open(Player p, UltraCustomizer plugin, String name, String description, OutcomingVariable[] variables, Folder folder, Callback<Object> callback) {

    }
}