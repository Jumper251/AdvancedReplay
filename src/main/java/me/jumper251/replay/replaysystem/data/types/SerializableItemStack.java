package me.jumper251.replay.replaysystem.data.types;

import me.jumper251.replay.utils.VersionUtil;
import me.jumper251.replay.utils.version.EnchantmentBridge;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class SerializableItemStack implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6849346246817837690L;


    private Map<String, Object> itemStack;

    private boolean hasEnchantment;

    private int color;

    private boolean hasColor;

    private int customModelData;


    public SerializableItemStack() {

    }

    public SerializableItemStack(Map<String, Object> itemStack) {
        this.itemStack = itemStack;
    }

    public SerializableItemStack(Map<String, Object> itemStack, boolean hasEnchantment, int color, boolean hasColor) {
        this(itemStack);

        this.hasEnchantment = hasEnchantment;
        this.color = color;
        this.hasColor = hasColor;
    }

    public static SerializableItemStack fromItemStack(ItemStack stack) {
        return fromItemStack(stack, false);
    }

    public static SerializableItemStack fromMaterial(Material mat) {
        if (mat.isItem() || VersionUtil.isBelow(VersionUtil.VersionEnum.V1_20)) {
            return fromItemStack(new ItemStack(mat), false);
        } else {
            Map<String, Object> serialized = new HashMap<>();
            serialized.put("type", mat.name());


            return new SerializableItemStack(serialized);

        }
    }

    public static SerializableItemStack fromItemStack(ItemStack stack, boolean block) {
        Map<String, Object> serialized = bukkitSerialize(stack);

        if (block && stack.getType() == Material.FLINT_AND_STEEL) {
            serialized.put("type", "FIRE");
        }

        SerializableItemStack serializableItemStack = new SerializableItemStack(serialized);

        if (!block) {
            boolean hasEnchantment = stack.getEnchantments() != null && stack.getEnchantments().size() > 0;
            serializableItemStack.setHasEnchantment(hasEnchantment);

            if (stack.hasItemMeta() && stack.getItemMeta() instanceof LeatherArmorMeta) {
                LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
                serializableItemStack.setHasColor(true);
                serializableItemStack.setColor(meta.getColor().asRGB());
            }
        }

        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_21)) {
            if (stack.getItemMeta() != null && stack.getItemMeta().hasCustomModelData()) {
                serializableItemStack.setCustomModelData(stack.getItemMeta().getCustomModelData());
            }
        }

        return serializableItemStack;
    }

    private static Map<String, Object> bukkitSerialize(ItemStack stack) {
        if (VersionUtil.isAbove(VersionUtil.VersionEnum.V1_21)) {
            Map<String, Object> serialized = new HashMap<>();
            serialized.put("type", stack.getType().name());
            serialized.put("v", Bukkit.getUnsafe().getDataVersion());

            return serialized;

        } else {
            Map<String, Object> serialized = stack.serialize();

            serialized.entrySet().removeIf(e -> !e.getKey().equalsIgnoreCase("v") && !e.getKey().equalsIgnoreCase("type") && !e.getKey().equalsIgnoreCase("damage"));
            return serialized;
        }

    }

    public Map<String, Object> getItemStack() {
        return itemStack;
    }

    public void setItemStack(Map<String, Object> itemStack) {
        this.itemStack = itemStack;
    }

    public Material getMaterial() {
        return Material.getMaterial((String) itemStack.get("type"));
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean hasEnchantment() {
        return hasEnchantment;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public void setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
    }

    public boolean hasColor() {
        return hasColor;
    }

    public void setHasColor(boolean hasColor) {
        this.hasColor = hasColor;
    }

    public void setHasEnchantment(boolean hasEnchantment) {
        this.hasEnchantment = hasEnchantment;
    }

    public ItemStack toItemStack() {
        ItemStack stack = ItemStack.deserialize(this.itemStack);
        if (this.hasEnchantment) stack.addUnsafeEnchantment(EnchantmentBridge.UNBREAKING.toEnchantment(), 1);

        if (this.hasColor) {
            LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
            meta.setColor(Color.fromRGB(this.color));
            stack.setItemMeta(meta);
        }

        if (this.customModelData > 0) {
            ItemMeta meta = stack.getItemMeta();
            meta.setCustomModelData(this.customModelData);
            stack.setItemMeta(meta);
        }

        return stack;
    }

    @Override
    public String toString() {
        return "SerializableItemStack{" +
                "itemStack=" + itemStack +
                ", hasEnchantment=" + hasEnchantment +
                ", color=" + color +
                ", hasColor=" + hasColor +
                ", customModelData=" + customModelData +
                '}';
    }
}
