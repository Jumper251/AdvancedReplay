package me.jumper251.replay.replaysystem.replaying;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.comphenix.packetwrapper.WrapperPlayServerTitle;
import com.comphenix.protocol.wrappers.EnumWrappers.TitleAction;
import com.comphenix.protocol.wrappers.WrappedChatComponent;


public class ReplayHelper {

	public static HashMap<String, Replayer> replaySessions = new HashMap<String, Replayer>();
	
	public static ItemStack creatItem(Material material, String name) {
		ItemStack stack = new ItemStack(material);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		
		return stack;
	}
	
	public static ItemStack getPauseItem() {
		ItemStack pause = new ItemStack(Material.SKULL_ITEM,1,(short)3);

		SkullMeta pauseMeta = (SkullMeta) pause.getItemMeta();
		pauseMeta.setDisplayName("¤cPause");
		pauseMeta.setOwner("Push_red_button");
		pause.setItemMeta(pauseMeta);
		
		return pause;
	}
	
	public static ItemStack getResumeItem() {
		return creatItem(Material.SLIME_BLOCK, "¤aResume");
	}
	
	public static void createTeleporter(Player player, Replayer replayer) {
		Inventory inv = Bukkit.createInventory(null, ((int)replayer.getNPCList().size() / 9) > 0 ? ((int)Math.floor(replayer.getNPCList().size() / 9)) * 9 : 9 , "¤7Teleporter");
		
		int index = 0;
		
		for (String name : replayer.getNPCList().keySet()) {
			ItemStack stack = new ItemStack(Material.SKULL_ITEM,1,(short)3);
			SkullMeta meta = (SkullMeta) stack.getItemMeta();
			meta.setDisplayName("¤6" + name);
			meta.setOwner(name);
			stack.setItemMeta(meta);
			
			inv.setItem(index, stack);

			index++;
		}
		
		player.openInventory(inv);
	}
	
	
	public static void sendTitle(Player player, String title, String subTitle, int stay) {
		WrapperPlayServerTitle packet = new WrapperPlayServerTitle();
		packet.setAction(TitleAction.TIMES);
		packet.setStay(stay);
		packet.setFadeIn(0);
		packet.setFadeOut(20);
		
		packet.sendPacket(player);
		
		if (subTitle != null) {
			WrapperPlayServerTitle sub = new WrapperPlayServerTitle();
			sub.setAction(TitleAction.SUBTITLE);
			sub.setTitle(WrappedChatComponent.fromText(subTitle));
			
			sub.sendPacket(player);
		}
		
		WrapperPlayServerTitle titlePacket = new WrapperPlayServerTitle();
		titlePacket.setAction(TitleAction.TITLE);
		titlePacket.setTitle(title != null ? WrappedChatComponent.fromText(title) : WrappedChatComponent.fromText(""));
		
		titlePacket.sendPacket(player);
		
		
	}
	
	public static boolean isInRange(Location loc1, Location loc2) {
		return loc1.getWorld().getName().equals(loc2.getWorld().getName()) && (loc1.distance(loc2) <= 48D);
	}
	
}
