package me.jumper251.replay.filesystem;

import org.bukkit.Material;


public class ItemConfigOption {

    private Material material;

    private String name;

    private int slot;

    private String owner;

    private int data;

    private boolean enabled;

    public ItemConfigOption(Material material, String name, int slot) {
        this.material = material;
        this.name = name;
        this.slot = slot;
        this.enabled = true;
    }

    public ItemConfigOption(Material material, String name, int slot, String owner, int data) {
        this(material, name, slot);
        this.data = data;
        this.owner = owner;
    }


    public Material getMaterial() {
        return material;
    }

    public int getSlot() {
        return slot;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public ItemConfigOption enable(boolean enabled) {
        this.enabled = enabled;

        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

}
