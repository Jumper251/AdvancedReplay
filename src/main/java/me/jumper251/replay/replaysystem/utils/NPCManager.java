package me.jumper251.replay.replaysystem.utils;

import com.comphenix.packetwrapper.WrapperPlayServerEntityEquipment;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;
import com.comphenix.protocol.wrappers.Pair;
import me.jumper251.replay.replaysystem.data.types.InvData;
import me.jumper251.replay.replaysystem.data.types.ItemData;
import me.jumper251.replay.replaysystem.data.types.SerializableItemStack;
import me.jumper251.replay.utils.version.MaterialBridge;
import me.jumper251.replay.utils.VersionUtil;
import me.jumper251.replay.utils.VersionUtil.VersionEnum;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NPCManager {

    private final static List<Material> ARMOR = new ArrayList<>();

    public static List<String> names = new ArrayList<>();

    static {
        ARMOR.add(Material.DIAMOND_HELMET);
        ARMOR.add(Material.DIAMOND_CHESTPLATE);
        ARMOR.add(Material.DIAMOND_LEGGINGS);
        ARMOR.add(Material.DIAMOND_BOOTS);
        ARMOR.add(Material.IRON_HELMET);
        ARMOR.add(Material.IRON_CHESTPLATE);
        ARMOR.add(Material.IRON_LEGGINGS);
        ARMOR.add(Material.IRON_BOOTS);
        ARMOR.add(Material.CHAINMAIL_HELMET);
        ARMOR.add(Material.CHAINMAIL_CHESTPLATE);
        ARMOR.add(Material.CHAINMAIL_LEGGINGS);
        ARMOR.add(Material.CHAINMAIL_BOOTS);
        ARMOR.add(MaterialBridge.GOLDEN_HELMET.toMaterial());
        ARMOR.add(MaterialBridge.GOLDEN_CHESTPLATE.toMaterial());
        ARMOR.add(MaterialBridge.GOLDEN_LEGGINGS.toMaterial());
        ARMOR.add(MaterialBridge.GOLDEN_BOOTS.toMaterial());
        ARMOR.add(Material.LEATHER_HELMET);
        ARMOR.add(Material.LEATHER_CHESTPLATE);
        ARMOR.add(Material.LEATHER_LEGGINGS);
        ARMOR.add(Material.LEATHER_BOOTS);

        if (VersionUtil.isAbove(VersionEnum.V1_16)) {
            ARMOR.add(Material.NETHERITE_HELMET);
            ARMOR.add(Material.NETHERITE_CHESTPLATE);
            ARMOR.add(Material.NETHERITE_LEGGINGS);
            ARMOR.add(Material.NETHERITE_BOOTS);
        }

    }

    public static List<WrapperPlayServerEntityEquipment> updateEquipmentv16(int id, InvData data) {
        List<Pair<ItemSlot, ItemStack>> items = new ArrayList<>();
        items.add(new Pair<>(ItemSlot.HEAD, fromID(data.getHead())));
        items.add(new Pair<>(ItemSlot.CHEST, fromID(data.getChest())));
        items.add(new Pair<>(ItemSlot.LEGS, fromID(data.getLeg())));
        items.add(new Pair<>(ItemSlot.FEET, fromID(data.getBoots())));
        items.add(new Pair<>(ItemSlot.MAINHAND, fromID(data.getMainHand())));
        items.add(new Pair<>(ItemSlot.OFFHAND, fromID(data.getOffHand())));

        WrapperPlayServerEntityEquipment packet = new WrapperPlayServerEntityEquipment();

        packet.setEntityID(id);
        packet.getHandle().getSlotStackPairLists().write(0, items);

        return Collections.singletonList(packet);
    }


    public static List<WrapperPlayServerEntityEquipment> updateEquipment(int id, InvData data) {
        List<WrapperPlayServerEntityEquipment> list = new ArrayList<>();


        WrapperPlayServerEntityEquipment packet = new WrapperPlayServerEntityEquipment();
        packet.setEntityID(id);
        packet.setSlot(ItemSlot.HEAD);
        packet.setItem(fromID(data.getHead()));
        list.add(packet);


        WrapperPlayServerEntityEquipment packet1 = new WrapperPlayServerEntityEquipment();
        packet1.setEntityID(id);
        packet1.setSlot(ItemSlot.CHEST);
        packet1.setItem(fromID(data.getChest()));
        list.add(packet1);


        WrapperPlayServerEntityEquipment packet2 = new WrapperPlayServerEntityEquipment();
        packet2.setEntityID(id);
        packet2.setSlot(ItemSlot.LEGS);
        packet2.setItem(fromID(data.getLeg()));
        list.add(packet2);


        WrapperPlayServerEntityEquipment packet3 = new WrapperPlayServerEntityEquipment();
        packet3.setEntityID(id);
        packet3.setSlot(ItemSlot.FEET);
        packet3.setItem(fromID(data.getBoots()));
        list.add(packet3);


        WrapperPlayServerEntityEquipment packet4 = new WrapperPlayServerEntityEquipment();
        packet4.setEntityID(id);
        packet4.setSlot(ItemSlot.MAINHAND);
        packet4.setItem(fromID(data.getMainHand()));
        list.add(packet4);


        if (!VersionUtil.isCompatible(VersionEnum.V1_8)) {
            WrapperPlayServerEntityEquipment packet5 = new WrapperPlayServerEntityEquipment();
            packet5.setEntityID(id);
            packet5.setSlot(ItemSlot.OFFHAND);
            packet5.setItem(fromID(data.getOffHand()));
            list.add(packet5);
        }

        return list;
    }

    public static List<com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment> updateEquipmentOld(int id, InvData data) {
        List<com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment> list = new ArrayList<>();

        com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment packet = new com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment();
        packet.setEntityID(id);
        packet.setSlot(4);
        packet.setItem(fromID(data.getHead()));
        list.add(packet);


        com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment packet1 = new com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment();
        packet1.setEntityID(id);
        packet1.setSlot(3);
        packet1.setItem(fromID(data.getChest()));
        list.add(packet1);


        com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment packet2 = new com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment();
        packet2.setEntityID(id);
        packet2.setSlot(2);
        packet2.setItem(fromID(data.getLeg()));
        list.add(packet2);


        com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment packet3 = new com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment();
        packet3.setEntityID(id);
        packet3.setSlot(1);
        packet3.setItem(fromID(data.getBoots()));
        list.add(packet3);


        com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment packet4 = new com.comphenix.packetwrapper.old.WrapperPlayServerEntityEquipment();
        packet4.setEntityID(id);
        packet4.setSlot(0);
        packet4.setItem(fromID(data.getMainHand()));
        list.add(packet4);


        return list;
    }

    public static ItemStack fromID(ItemData data) {
        if (data == null) return new ItemStack(Material.AIR);
        if (data.getItemStack() != null) return data.getItemStack().toItemStack();

        return new ItemStack(MaterialBridge.fromID(data.getId()), 1, (short) data.getSubId());
    }

    public static ItemData fromItemStack(ItemStack stack) {
        if (stack == null) return null;

        return new ItemData(SerializableItemStack.fromItemStack(stack));
    }

    @SuppressWarnings("deprecation")
    public static InvData copyFromPlayer(Player player, boolean armor, boolean off) {
        InvData data = new InvData();

        if (VersionUtil.isCompatible(VersionEnum.V1_8)) {
            data.setMainHand(fromItemStack(player.getItemInHand()));
        } else {
            data.setMainHand(fromItemStack(player.getInventory().getItemInMainHand()));
            if (off) {
                data.setOffHand(fromItemStack(player.getInventory().getItemInOffHand()));
            }
        }

        if (armor) {
            data.setHead(fromItemStack(player.getInventory().getHelmet()));

            data.setChest(fromItemStack(player.getInventory().getChestplate()));

            data.setLeg(fromItemStack(player.getInventory().getLeggings()));

            data.setBoots(fromItemStack(player.getInventory().getBoots()));

        }


        return data;
    }

    public static boolean isArmor(ItemStack stack) {
        if (stack == null) return false;
        return ARMOR.contains(stack.getType());
    }


    public static String getMaterialName(ItemStack stack) {
        return stack.getType().name();
    }

    public static String getArmorType(ItemStack stack) {
        if (stack == null) return null;

        if (getMaterialName(stack).contains("HELMET")) return "head";
        if (getMaterialName(stack).contains("CHESTPLATE")) return "chest";
        if (getMaterialName(stack).contains("LEGGINGS")) return "leg";
        if (getMaterialName(stack).contains("BOOTS")) return "boots";

        return null;

    }

    public static boolean wearsArmor(Player p, String type) {
        PlayerInventory inv = p.getInventory();
        if (type == null) return false;

        if (type.equals("head") && isArmor(inv.getHelmet())) return true;
        if (type.equals("chest") && isArmor(inv.getChestplate())) return true;
        if (type.equals("leg") && isArmor(inv.getLeggings())) return true;
        if (type.equals("boots") && isArmor(inv.getBoots())) return true;

        return false;
    }


}
