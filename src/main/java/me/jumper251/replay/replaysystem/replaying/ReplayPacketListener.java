package me.jumper251.replay.replaysystem.replaying;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import replaylib.com.comphenix.packetwrapper.WrapperPlayClientUseEntity;
import replaylib.com.comphenix.packetwrapper.WrapperPlayServerCamera;
import replaylib.com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import replaylib.com.comphenix.packetwrapper.WrapperPlayServerGameStateChange;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.listener.AbstractListener;
import me.jumper251.replay.utils.VersionUtil;
import me.jumper251.replay.utils.VersionUtil.VersionEnum;

public class ReplayPacketListener extends AbstractListener {
	
	private PacketAdapter packetAdapter;
	
	private Replayer replayer;
	
	private int previous;
	
	private HashMap<Player, Integer> spectating;
	
	public ReplayPacketListener(Replayer replayer) {
		this.replayer = replayer;
		this.spectating = new HashMap<Player, Integer>();
		this.previous = -1;
		
		if (!isRegistered()) register();
	}

	@Override
	public void register() {
		this.packetAdapter = new PacketAdapter(ReplaySystem.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY, PacketType.Play.Server.ENTITY_DESTROY) {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onPacketReceiving(PacketEvent event) {
				WrapperPlayClientUseEntity packet = new WrapperPlayClientUseEntity(event.getPacket());
				Player p = event.getPlayer();
				
				if (packet.getType() == EntityUseAction.ATTACK && ReplayHelper.replaySessions.containsKey(p.getName()) && replayer.getNPCList().values().stream().anyMatch(ent -> packet.getTargetID() == ent.getId())) {
					if (p.getGameMode() != GameMode.SPECTATOR) previous = p.getGameMode().getValue();
					setCamera(p, packet.getTargetID(), 3F);
					
				}
			}
			
			@Override
			public void onPacketSending(PacketEvent event) {
				WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy(event.getPacket());
				Player p = event.getPlayer();
				
				if (ReplayHelper.replaySessions.containsKey(p.getName()) && isSpectating(p)) {
					
					List<Integer> entityIds;
        			if (VersionUtil.isAbove(VersionEnum.V1_17)) {
        				entityIds = packet.getHandle().getIntLists().read(0);
        				
        			} else {
        				entityIds = IntStream.of(packet.getEntityIDs()).boxed().collect(Collectors.toList());
        			}
					
					for (int id : entityIds) {
						if (id == spectating.get(p)) {
							setCamera(p, p.getEntityId(), previous);
						}
					}
				}
			}
			
		};
		
	    ProtocolLibrary.getProtocolManager().addPacketListener(this.packetAdapter);
	}
	
	@Override
	public void unregister() {
		ProtocolLibrary.getProtocolManager().removePacketListener(this.packetAdapter);
	}
	
	public boolean isRegistered() {
		return this.packetAdapter != null;
	}
	
	public int getPrevious() {
		return previous;
	}
	
	public boolean isSpectating(Player p) {
		return this.spectating.containsKey(p);
	}
	
	public void setCamera(Player p, int entityID, float gamemode) {
		WrapperPlayServerCamera camera = new WrapperPlayServerCamera();
		camera.setCameraId(entityID);
		
		WrapperPlayServerGameStateChange state = new WrapperPlayServerGameStateChange();
		
		if (VersionUtil.isAbove(VersionEnum.V1_16)) {
			state.getHandle().getGameStateIDs().write(0, 3);
		} else {
			state.setReason(3);
		}
		
		state.setValue(gamemode < 0 ? 0 : gamemode);
		
		state.sendPacket(p);
		camera.sendPacket(p);
		
		if (gamemode == 3F) {
			this.spectating.put(p, entityID);
		} else if (this.spectating.containsKey(p)){
			this.spectating.remove(p);
		}
	}
}
