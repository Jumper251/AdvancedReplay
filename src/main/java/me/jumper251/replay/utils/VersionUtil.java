package me.jumper251.replay.utils;

import java.lang.reflect.Field;
import java.util.Arrays;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VersionUtil {

	public static VersionEnum VERSION;

	static {
		VERSION = VersionEnum.parseVersion();
	}

	public static boolean isCompatible(VersionEnum ve){
		return VERSION.equals(ve);
	}
	
	public static boolean isAbove(VersionEnum ve) {		
		return VERSION.getOrder() >= ve.getOrder();
	}
	
	public static boolean isBelow(VersionEnum ve) {
		return VERSION.getOrder() <= ve.getOrder();
	}
	
	public static boolean isBetween(VersionEnum ve1, VersionEnum ve2) {
		return isAbove(ve1) && isBelow(ve2);
	}
	
	public static Class<?> getNmsClass(String nmsClassName) throws ClassNotFoundException {
	    return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + nmsClassName);
	}
	
	public static void sendPacket(Player p, Object packet){
		try{
			Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
			Field playerConnectionField = nmsPlayer.getClass().getField("playerConnection");
			Object pConnection = playerConnectionField.get(nmsPlayer);
			pConnection.getClass().getMethod("sendPacket", Class.forName("net.minecraft.server." + VersionUtil.VERSION + ".Packet")).invoke(pConnection, packet);
		}catch(Exception e){
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
		V1_16(9),
		V1_17(10),
		V1_18(11),
		V1_19(12),
		V1_20(13),
		V1_21(14),
        V1_21_10(15);

		
		private int order;

		VersionEnum(int order) {
			this.order = order;
		}
		
		public int getOrder() {
			return order;
		}

        public int getPatch() {
            String[] parts = this.name().split("_");
            if (parts.length == 3) {
                return Integer.parseInt(parts[2]);
            }
            return 0;
        }

		public static VersionEnum parseVersion() {
			String version = Bukkit.getBukkitVersion().split("-")[0];
            String[] parts = version.split("\\.");
			String majorMinor = parts[0] + "_" + parts[1];
			String majorMinorPatch = parts[0] + "_" + parts[1] + "_" + parts[2];

            return Arrays.stream(VersionEnum.values())
                    .filter(v -> v.toString().equals("V" + majorMinorPatch)
                            || (v.getPatch() != 0 && v.getPatch() < Integer.parseInt(parts[2])))
                    .findAny()
                    .orElse(VersionEnum.valueOf("V" + majorMinor));
		}

	}
}


