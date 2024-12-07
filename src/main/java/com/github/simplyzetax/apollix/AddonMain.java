package com.github.simplyzetax.apollix;

import me.TechsCode.UltraCustomizer.UltraCustomizer;
import org.bukkit.event.Listener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Used as a main class of addons.
 * For this to work you must call the instance somewhere in elements constructor does not matter what element you call it in.
 */
public class AddonMain implements Listener {

    public static final AddonMain instance = new AddonMain("Apollix", "simplyzetax");
    private String name, author;

    public AddonMain(String name, String author) {
        this.name = name; this.author = author;
        try {
            onLoad();
            UltraCustomizer.getInstance().log(name + " onLoad executed. " + "Author: " + author);
        } catch (Exception e) {
            UltraCustomizer.getInstance().log(name + " onLoad failed please contact author. " + "Author: " + author);
            e.printStackTrace();
        }
    }

    /**
     * You can get the UltraCustomizer instance here and do anything you would do in regular plugin here.
     * You can create fields any anything in this class.
     */
    private void onLoad() {
        
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }
}
