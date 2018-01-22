package me.jumper251.replay.replaysystem.recording;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import me.jumper251.replay.listener.AbstractListener;
import me.jumper251.replay.replaysystem.data.types.InvData;
import me.jumper251.replay.replaysystem.utils.NPCManager;

public class CompListener extends AbstractListener{

	private PacketRecorder packetRecorder;
	
	public CompListener(PacketRecorder packetRecorder) {
		super();
		
		this.packetRecorder = packetRecorder;
	}
	
	@EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onSwap(PlayerSwapHandItemsEvent e) {
		Player p = e.getPlayer();
		if (this.packetRecorder.getRecorder().getPlayers().contains(p.getName())) {
			
			InvData data = NPCManager.copyFromPlayer(p, true, true);
			data.setMainHand(NPCManager.fromItemStack(e.getMainHandItem()));
			data.setOffHand(NPCManager.fromItemStack(e.getOffHandItem()));

			this.packetRecorder.addData(p.getName(), data);
		}

	}
}
