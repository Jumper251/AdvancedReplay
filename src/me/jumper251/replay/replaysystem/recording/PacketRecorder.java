package me.jumper251.replay.replaysystem.recording;

import java.util.ArrayList;


import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import com.comphenix.packetwrapper.WrapperPlayClientBlockDig;
import com.comphenix.packetwrapper.WrapperPlayClientEntityAction;
import com.comphenix.packetwrapper.WrapperPlayClientLook;
import com.comphenix.packetwrapper.WrapperPlayClientPosition;
import com.comphenix.packetwrapper.WrapperPlayClientPositionLook;

import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerAction;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerDigType;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;


import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.listener.AbstractListener;
import me.jumper251.replay.replaysystem.data.types.AnimationData;
import me.jumper251.replay.replaysystem.data.types.EntityActionData;
import me.jumper251.replay.replaysystem.data.types.MetadataUpdate;
import me.jumper251.replay.replaysystem.data.types.MovingData;
import me.jumper251.replay.replaysystem.data.types.PacketData;
import me.jumper251.replay.utils.VersionUtil;
import me.jumper251.replay.utils.VersionUtil.VersionEnum;



public class PacketRecorder extends AbstractListener{

	private PacketAdapter packetAdapter;
	
	private HashMap<String, List<PacketData>> packetData;
	
	private Recorder recorder;
	
	private AbstractListener compListener, listener;
	
	public PacketRecorder(Recorder recorder) {
		super();
		this.packetData = new HashMap<String, List<PacketData>>();
		this.recorder = recorder;
		
	}
	
	@Override
	public void register() {
		super.register();
		
		
		this.packetAdapter = new PacketAdapter(ReplaySystem.getInstance(), ListenerPriority.HIGHEST,
				PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION_LOOK, PacketType.Play.Client.LOOK, PacketType.Play.Client.ENTITY_ACTION, PacketType.Play.Client.ARM_ANIMATION, 
				PacketType.Play.Client.BLOCK_DIG, PacketType.Play.Client.USE_ITEM, PacketType.Play.Server.ENTITY_METADATA) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
            		//Bukkit.broadcastMessage(event.getPacketType().toString());
            		
            		if (event.getPlayer() != null && recorder.getPlayers().contains(event.getPlayer().getName())) {
            			Player p = event.getPlayer();
            			
            			PacketData data = null;
            			if (event.getPacketType() == PacketType.Play.Client.POSITION) {
            				WrapperPlayClientPosition packet = new WrapperPlayClientPosition(event.getPacket());
            				data = new MovingData(packet.getX(), packet.getY(), packet.getZ(), p.getLocation().getPitch(), p.getLocation().getYaw());
            				
            				if (recorder.getData().getWatcher(p.getName()).isBurning() && p.getFireTicks() <= 20) {
            					recorder.getData().getWatcher(p.getName()).setBurning(false);
            					addData(p.getName(), new MetadataUpdate(false, recorder.getData().getWatcher(p.getName()).isBlocking()));
            				}
            		
            			} 
            			
            			if (event.getPacketType() == PacketType.Play.Client.LOOK) {
            				WrapperPlayClientLook packet = new WrapperPlayClientLook(event.getPacket());
            				data = new MovingData(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), packet.getPitch(), packet.getYaw());
            			}
            			
               		if (event.getPacketType() == PacketType.Play.Client.POSITION_LOOK) {
            				WrapperPlayClientPositionLook packet = new WrapperPlayClientPositionLook(event.getPacket());
            				data = new MovingData(packet.getX(), packet.getY(), packet.getZ(), packet.getPitch(), packet.getYaw());
            			}
               			
               		if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {
               			WrapperPlayClientEntityAction packet = new WrapperPlayClientEntityAction(event.getPacket());
               			if (packet.getAction() == PlayerAction.START_SNEAKING || packet.getAction() == PlayerAction.STOP_SNEAKING) {
               				data = new EntityActionData(packet.getAction());
               			}
               		}
               		if (event.getPacketType() == PacketType.Play.Client.ARM_ANIMATION) {
               			data = new AnimationData(0);
               		}
               		

               		
               		if (event.getPacketType() == PacketType.Play.Client.BLOCK_DIG) {
               			WrapperPlayClientBlockDig packet = new WrapperPlayClientBlockDig(event.getPacket());  
               			
               			if(packet.getStatus() == PlayerDigType.RELEASE_USE_ITEM) {
               				if (recorder.getData().getWatcher(p.getName()).isBlocking()) {
               					recorder.getData().getWatcher(p.getName()).setBlocking(false);
               					addData(p.getName(), new MetadataUpdate(recorder.getData().getWatcher(p.getName()).isBurning(), false));
               				}
               			}
               		}
               		
            
  
            			
            			addData(event.getPlayer().getName(), data);
            		}
            }
            
            @Override
            public void onPacketSending(PacketEvent event) {
        			if (event.getPlayer() != null && recorder.getPlayers().contains(event.getPlayer().getName())) {
            			Player p = event.getPlayer();

            			if (event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA) {
            				WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(event.getPacket());
            				WrappedDataWatcher w = new WrappedDataWatcher(packet.getMetadata());
            				//Bukkit.broadcastMessage("value: " + w.getObject(5));
            			
            			}
        			}
            }
		};
		
	    ProtocolLibrary.getProtocolManager().addPacketListener(this.packetAdapter);
	    this.registerExternalListeners();
	}
	
	@Override
	public void unregister() {
		super.unregister();
		
		ProtocolLibrary.getProtocolManager().removePacketListener(this.packetAdapter);
		
		if(this.compListener != null) {
			this.compListener.unregister();
		}
		this.listener.unregister();
	}
	
	
	private void registerExternalListeners() {
		if (!VersionUtil.isCompatible(VersionEnum.V1_8)) {
			this.compListener = new CompListener(this);
			this.compListener.register();
		}
		
		this.listener = new RecordingListener(this);
		this.listener.register();
	}

	
	
	public void addData(String name, PacketData data) {
		List<PacketData> list = new ArrayList<PacketData>();
		if(this.packetData.containsKey(name)) {
			list = this.packetData.get(name);
		}
		
		list.add(data);
		this.packetData.put(name, list);
	}
	
	public HashMap<String, List<PacketData>> getPacketData() {
		return packetData;
	}
	
	public Recorder getRecorder() {
		return recorder;
	}
	
}
