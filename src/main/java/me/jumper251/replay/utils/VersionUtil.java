package me.jumper251.replay.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Locale;

public class VersionUtil {

    public static String VERSION;

    public static String CLEAN_VERSION;

    static {
        String bpName = Bukkit.getServer().getClass().getPackage().getName();
        VERSION = bpName.substring(bpName.lastIndexOf(".") + 1, bpName.length());

        CLEAN_VERSION = VERSION.substring(0, VERSION.length() - 3);
    }

    public static boolean isCompatible(VersionEnum ve) {
        return VERSION.toLowerCase().contains(ve.toString().toLowerCase());
    }

    public static boolean isAbove(VersionEnum ve) {
        return VersionEnum.valueOf(CLEAN_VERSION.toUpperCase()).getOrder() >= ve.getOrder();
    }

    public static boolean isBelow(VersionEnum ve) {
        return VersionEnum.valueOf(CLEAN_VERSION.toUpperCase()).getOrder() <= ve.getOrder();
    }

    public static boolean isEqual(VersionEnum ve) {
        System.out.println("Current version:" + VersionEnum.valueOf(CLEAN_VERSION.toUpperCase(Locale.ROOT)).name());
        return VersionEnum.valueOf(CLEAN_VERSION.toUpperCase(Locale.ROOT)) == ve;
    }

    public static boolean isBetween(VersionEnum ve1, VersionEnum ve2) {
        return isAbove(ve1) && isBelow(ve2);
    }

    public static Class<?> getNmsClass(String nmsClassName) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + nmsClassName);
    }

    public static void sendPacket(Player p, Object packet) {
        try {
            Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
            Field playerConnectionField = nmsPlayer.getClass().getField("playerConnection");
            Object pConnection = playerConnectionField.get(nmsPlayer);
            pConnection.getClass().getMethod("sendPacket", Class.forName("net.minecraft.server." + VersionUtil.VERSION + ".Packet")).invoke(pConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public enum VersionEnum {

        V1_8(1),
        V1_9(2),
        V1_10(3),
        V1_11(4),
        V1_12(5),
        V1_13(6),
        V1_14(7),
        V1_15(8),
        V1_16(9);

        private int order;

        private VersionEnum(int order) {
            this.order = order;
        }

        public int getOrder() {
            return order;
        }

    }
}


