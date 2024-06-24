package me.jumper251.replay.replaysystem.recording;

import java.util.*;


import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.comphenix.protocol.PacketTypeEnum;
import me.jumper251.replay.replaysystem.utils.entities.EntityMappings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import com.comphenix.packetwrapper.WrapperPlayClientBlockDig;
import com.comphenix.packetwrapper.WrapperPlayClientEntityAction;
import com.comphenix.packetwrapper.WrapperPlayClientLook;
import com.comphenix.packetwrapper.WrapperPlayClientPosition;
import com.comphenix.packetwrapper.WrapperPlayClientPositionLook;
import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerEntityTeleport;
import com.comphenix.packetwrapper.WrapperPlayServerEntityVelocity;
import com.comphenix.packetwrapper.WrapperPlayServerRelEntityMove;
import com.comphenix.packetwrapper.WrapperPlayServerRelEntityMoveLook;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntity;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerAction;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerDigType;


import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.filesystem.ConfigManager;
import me.jumper251.replay.listener.AbstractListener;
import me.jumper251.replay.replaysystem.data.types.AnimationData;
import me.jumper251.replay.replaysystem.data.types.EntityActionData;
import me.jumper251.replay.replaysystem.data.types.EntityData;
import me.jumper251.replay.replaysystem.data.types.EntityItemData;
import me.jumper251.replay.replaysystem.data.types.EntityMovingData;
import me.jumper251.replay.replaysystem.data.types.FishingData;
import me.jumper251.replay.replaysystem.data.types.LocationData;
import me.jumper251.replay.replaysystem.data.types.MetadataUpdate;
import me.jumper251.replay.replaysystem.data.types.MovingData;
import me.jumper251.replay.replaysystem.data.types.PacketData;
import me.jumper251.replay.replaysystem.data.types.VelocityData;
import me.jumper251.replay.replaysystem.recording.optimization.ReplayOptimizer;
import me.jumper251.replay.replaysystem.utils.NPCManager;
import me.jumper251.replay.utils.VersionUtil;
import me.jumper251.replay.utils.VersionUtil.VersionEnum;



public class PacketRecorder extends AbstractListener{

	private static final List<PacketType> RECORDED_PACKETS = new ArrayList<>(Arrays.asList(PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION_LOOK, PacketType.Play.Client.LOOK, PacketType.Play.Client.ENTITY_ACTION, PacketType.Play.Client.ARM_ANIMATION,
			PacketType.Play.Client.BLOCK_DIG, PacketType.Play.Server.SPAWN_ENTITY, PacketType.Play.Server.ENTITY_DESTROY, PacketType.Play.Server.ENTITY_VELOCITY,
			PacketType.Play.Server.REL_ENTITY_MOVE, PacketType.Play.Server.REL_ENTITY_MOVE_LOOK, PacketType.Play.Server.ENTITY_LOOK, PacketType.Play.Server.POSITION, PacketType.Play.Server.ENTITY_TELEPORT));

	private PacketAdapter packetAdapter;
	
	private Map<String, List<PacketData>> packetData;
	
	private List<Integer> spawnedItems;
	private HashMap<Integer, EntityData> spawnedEntities;
	private HashMap<Integer, String> entityLookup;
	private HashMap<Integer, Entity> idLookup;
	private List<Integer> spawnedHooks;

	private Recorder recorder;
	
	private ReplayOptimizer optimizer;
	
	private AbstractListener compListener, listener;
	
	public PacketRecorder(Recorder recorder) {
		super();
		this.packetData = new ConcurrentHashMap<>();
		this.spawnedItems = new ArrayList<Integer>();
		this.spawnedEntities = new HashMap<Integer, EntityData>();
		this.entityLookup = new HashMap<Integer, String>();
		this.idLookup = new HashMap<Integer, Entity>();
		this.spawnedHooks = new ArrayList<Integer>();
		this.recorder = recorder;
		this.optimizer = new ReplayOptimizer();
		
	}	

	
	@Override
	public void register() {
		super.register();

		if (VersionUtil.isBelow(VersionEnum.V1_18)) {
			RECORDED_PACKETS.add(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
		}

		this.packetAdapter = new PacketAdapter(ReplaySystem.getInstance(), ListenerPriority.HIGHEST, RECORDED_PACKETS) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
            		
            		if (event.getPlayer() != null && recorder.getPlayers().contains(event.getPlayer().getName())) {
            			Player p = event.getPlayer();
            			
            			PacketData data = null;
            			if (event.getPacketType() == PacketType.Play.Client.POSITION) {
            				WrapperPlayClientPosition packet = new WrapperPlayClientPosition(event.getPacket());
            				data = new MovingData(packet.getX(), packet.getY(), packet.getZ(), p.getLocation().getPitch(), p.getLocation().getYaw());
            			            				
            				if (recorder.getData().getWatcher(p.getName()).isBurning() && p.getFireTicks() <= 20) {
            					recorder.getData().getWatcher(p.getName()).setBurning(false);
            					addData(p.getName(), new MetadataUpdate(false, recorder.getData().getWatcher(p.getName()).isBlocking(), recorder.getData().getWatcher(p.getName()).isElytra()));
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
               					PlayerWatcher watcher = recorder.getData().getWatcher(p.getName());
        						watcher.setBlocking(false);
               					addData(p.getName(), MetadataUpdate.fromWatcher(watcher));
               				}
               			}
               		}
               		
               		addData(event.getPlayer().getName(), data);
            	}
            }
            
			@Override
            public void onPacketSending(PacketEvent event) {
            		Player p = event.getPlayer();
            		if (!recorder.getPlayers().contains(p.getName())) return;
            		
            		if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY) {
            			WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity(event.getPacket());
            			
            			com.comphenix.packetwrapper.old.WrapperPlayServerSpawnEntity oldPacket = new com.comphenix.packetwrapper.old.WrapperPlayServerSpawnEntity(event.getPacket());
            			int type = VersionUtil.isCompatible(VersionEnum.V1_8) ? oldPacket.getType() : packet.getType(); 
            			
    					LocationData location = null;

    					if (VersionUtil.isCompatible(VersionEnum.V1_8)) {
        					location = new LocationData(oldPacket.getX(), oldPacket.getY(), oldPacket.getZ(), p.getWorld().getName());
        				} else {
        					location = new LocationData(packet.getX(), packet.getY(), packet.getZ(), p.getWorld().getName());
        				}
            			
            			if ((type == 2 || (VersionUtil.isAbove(VersionEnum.V1_14) && event.getPacket().getEntityTypeModifier().read(0) == EntityType.DROPPED_ITEM)) && !spawnedItems.contains(packet.getEntityID())) {
            				Entity en = packet.getEntity(p.getWorld());
            				if (en != null && en instanceof Item) {
            					Item item = (Item) en;
            					LocationData velocity = LocationData.fromLocation(item.getVelocity().toLocation(p.getWorld()));

            					addData(p.getName(), new EntityItemData(0, packet.getEntityID(), NPCManager.fromItemStack(item.getItemStack()), location, velocity));
            					
            					spawnedItems.add(packet.getEntityID());
            				}
            				
            	
            
            			}
        				if ((type == 90 || (VersionUtil.isAbove(VersionEnum.V1_14) && event.getPacket().getEntityTypeModifier().read(0) == EntityType.FISHING_HOOK))  && !spawnedHooks.contains(packet.getEntityID())) {
							int throwerId = VersionUtil.isCompatible(VersionEnum.V1_8) ? oldPacket.getObjectData() : packet.getObjectData();

							String ownerName = Bukkit.getOnlinePlayers().stream()
									.filter(player -> (player.getWorld().getName().equals(p.getWorld().getName()) && player.getEntityId() == throwerId))
									.findFirst()
									.map(Player::getName)
									.orElse(null);

        					if (VersionUtil.isCompatible(VersionEnum.V1_8)) {
        						addData(p.getName(), new FishingData(oldPacket.getEntityID(), location, oldPacket.getOptionalSpeedX(), oldPacket.getOptionalSpeedY(), oldPacket.getOptionalSpeedZ(), ownerName));
        					} else {
        						addData(p.getName(), new FishingData(packet.getEntityID(), location, packet.getOptionalSpeedX(), packet.getOptionalSpeedY(), packet.getOptionalSpeedZ(), ownerName));
        					}
        					spawnedHooks.add(packet.getEntityID());
        				}

						if (VersionUtil.isAbove(VersionEnum.V1_19) && ConfigManager.RECORD_ENTITIES) {
							EntityType livingType = event.getPacket().getEntityTypeModifier().read(0);

							if (EntityMappings.getInstance().getTypeId(livingType.toString()) != 0) {
								LocationData locationData = new LocationData(packet.getX(), packet.getY(), packet.getZ(), p.getWorld().getName());

								if (!spawnedEntities.containsKey(packet.getEntityID())) {
									EntityData entData = new EntityData(0, packet.getEntityID(), locationData, livingType.toString());
									addData(p.getName(), entData);

									spawnedEntities.put(packet.getEntityID(), entData);
									entityLookup.put(packet.getEntityID(), p.getName());
									idLookup.put(packet.getEntityID(), packet.getEntity(p.getWorld()));
								}
							}
						}

            		}
            		
            		if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY_LIVING && ConfigManager.RECORD_ENTITIES) {
            			WrapperPlayServerSpawnEntityLiving packet = new WrapperPlayServerSpawnEntityLiving(event.getPacket());
            			
            			EntityType type = packet.getType();
            			if (type == null) type = packet.getEntity(p.getWorld()).getType();
            			            			
            			if (!spawnedEntities.containsKey(packet.getEntityID())) {
            				LocationData location = null;
            				
            				if (VersionUtil.isCompatible(VersionEnum.V1_8)) {
            					com.comphenix.packetwrapper.old.WrapperPlayServerSpawnEntityLiving oldPacket = new com.comphenix.packetwrapper.old.WrapperPlayServerSpawnEntityLiving(event.getPacket());
            					location = new LocationData(oldPacket.getX(), oldPacket.getY(), oldPacket.getZ(), p.getWorld().getName());
            				} else {
            					location = new LocationData(packet.getX(), packet.getY(), packet.getZ(), p.getWorld().getName());
            				}

            				EntityData entData = new EntityData(0, packet.getEntityID(), location, type.toString());
            				addData(p.getName(), entData);

            				spawnedEntities.put(packet.getEntityID(), entData);
            				entityLookup.put(packet.getEntityID(), p.getName());
            				idLookup.put(packet.getEntityID(), packet.getEntity(p.getWorld()));

            			}
            			
            		}
            		
            		if (event.getPacketType() == PacketType.Play.Server.ENTITY_DESTROY) {
            			WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy(event.getPacket());
            			
            			List<Integer> entityIds;
            			if (VersionUtil.isAbove(VersionEnum.V1_17)) {
            				entityIds = packet.getHandle().getIntLists().read(0);
            				
            			} else {
            				entityIds = IntStream.of(packet.getEntityIDs()).boxed().collect(Collectors.toList());
            			}
            			

            			for (Integer id : entityIds) {

            				if (spawnedItems.contains(id)) {
            					addData(p.getName(), new EntityItemData(1, id, null, null, null));
            					
            					spawnedItems.remove(id);
            				}
            				
            				if (spawnedEntities.containsKey(id) && (idLookup.get(id) == null || (idLookup.get(id) != null && idLookup.get(id).isDead()))) {
            		
            					addData(p.getName(), new EntityData(1, id, spawnedEntities.get(id).getLocation(), spawnedEntities.get(id).getType()));
            					
            					spawnedEntities.remove(id);
            					entityLookup.remove(id);
            					idLookup.remove(id);
            				}
            				
            				if (spawnedHooks.contains(id)) {
            					addData(p.getName(), new EntityItemData(2, id, null, null, null));
            					
            					spawnedHooks.remove(id);
            				}
            			}
            		}
            	
            		if (event.getPacketType() == PacketType.Play.Server.ENTITY_VELOCITY) {
            			WrapperPlayServerEntityVelocity packet = new WrapperPlayServerEntityVelocity(event.getPacket());
            			
            			if (spawnedHooks.contains(packet.getEntityID()) || (entityLookup.containsKey(packet.getEntityID()) && entityLookup.get(packet.getEntityID()).equalsIgnoreCase(p.getName()))) {
            				
            				addData(p.getName(), new VelocityData(packet.getEntityID(), packet.getVelocityX(), packet.getVelocityY(), packet.getVelocityZ()));
            			}
            			
            		}
            		if ( event.getPacketType() == PacketType.Play.Server.REL_ENTITY_MOVE) {
            			WrapperPlayServerRelEntityMove packet = new WrapperPlayServerRelEntityMove(event.getPacket());
            			
            			if (entityLookup.containsKey(packet.getEntityID()) && entityLookup.get(packet.getEntityID()).equalsIgnoreCase(p.getName())) {
            				Location loc = checkEntityLocation(packet.getEntity(p.getWorld()));
            			
            				if (loc != null) {
            					addData(p.getName(), new EntityMovingData(packet.getEntityID(), loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw()));
            				}
            				
            				
            			}
            		}
            		if (event.getPacketType() == PacketType.Play.Server.REL_ENTITY_MOVE_LOOK) {
            			WrapperPlayServerRelEntityMoveLook packet = new WrapperPlayServerRelEntityMoveLook(event.getPacket());
            			
            			if (entityLookup.containsKey(packet.getEntityID()) && entityLookup.get(packet.getEntityID()).equalsIgnoreCase(p.getName())) {
            				Location loc = checkEntityLocation(packet.getEntity(p.getWorld()));
            			
            				if (loc != null) {
            					addData(p.getName(), new EntityMovingData(packet.getEntityID(), loc.getX(), loc.getY(), loc.getZ(), packet.getPitch(), packet.getYaw()));
            				}
            					
            			}
            		}
            		if (event.getPacketType() == PacketType.Play.Server.ENTITY_TELEPORT) {
            			WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport(event.getPacket());

            			if (entityLookup.containsKey(packet.getEntityID()) && entityLookup.get(packet.getEntityID()).equalsIgnoreCase(p.getName())) {
            				Location loc = checkEntityLocation(packet.getEntity(p.getWorld()));

            				if (loc != null) {
            					addData(p.getName(), new EntityMovingData(packet.getEntityID(), loc.getX(), loc.getY(), loc.getZ(), packet.getPitch(), packet.getYaw()));
            				}
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
	
	private Location checkEntityLocation(Entity en) {
		if (en == null) return null;
		
		return en.getLocation();
	}

	
	
	public void addData(String name, PacketData data) {
		if (!optimizer.shouldRecord(data)) return;
	
		List<PacketData> list = new ArrayList<PacketData>();
		if(this.packetData.containsKey(name)) {
			list = this.packetData.getOrDefault(name, new ArrayList<>());
		}
		
		list.add(data);
		this.packetData.put(name, list);
	}
	
	public Map<String, List<PacketData>> getPacketData() {
		return packetData;
	}
	
	public HashMap<Integer, String> getEntityLookup() {
		return entityLookup;
	}
	
	public Recorder getRecorder() {
		return recorder;
	}
	
}
