package me.jumper251.replay.replaysystem.data.types;

public class ItemData extends PacketData{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3882181315164039909L;
	
	
	private int id, subId;
	
	public ItemData(int id, int subId) {
		this.id = id;
		this.subId = subId;
	}
	
	public int getId() {
		return id;
	}
	
	public int getSubId() {
		return subId;
	}

}
