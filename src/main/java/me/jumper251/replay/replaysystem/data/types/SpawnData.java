package me.jumper251.replay.replaysystem.data.types;

import java.util.UUID;





public class SpawnData extends PacketData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7896939862437693109L;
	
	private UUID uuid;
	
	private LocationData location;

	private String worldHashCode;
	
	private SignatureData signature;
	
	public SpawnData(UUID uuid, LocationData location, SignatureData signature, String worldHashCode) {
		this.uuid = uuid;
		this.location = location;
		this.signature = signature;
		this.worldHashCode = worldHashCode;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public LocationData getLocation() {
		return location;
	}
	
	public SignatureData getSignature() {
		return signature;
	}
	
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getWorldHashCode(){
		return this.worldHashCode;
	}
}
