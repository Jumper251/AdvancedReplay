package me.jumper251.replay.replaysystem.data.types;

public class EntityDestroyData extends PacketData {
	
	
	int id;
	
	public EntityDestroyData(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
