package me.jumper251.replay.utils;

import java.lang.reflect.Field;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VersionUtil {

	public static String VERSION;
	
	static {
        String bpName = Bukkit.getServer().getClass().getPackage().getName();
         VERSION = bpName.substring(bpName.lastIndexOf(".") + 1, bpName.length());
	}
	
	public static boolean isCompatible(VersionEnum ve){
		return VERSION.toLowerCase().contains(ve.toString().toLowerCase());
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
		V1_8,
		V1_9,
		V1_10,
		V1_11,
		V1_12,
		V1_13,
		V1_14;

	}
}


