package me.jumper251.replay.replaysystem.data.types;



public class ItemData extends PacketData{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3882181315164039909L;
	
	
	private int id, subId;
	
	private SerializableItemStack itemStack;
	
	public ItemData(int id, int subId) {
		this.id = id;
		this.subId = subId;
	}
	
	public ItemData(SerializableItemStack itemStack) {
		this.itemStack = itemStack;
	}
	
	public int getId() {
		return id;
	}
	
	public int getSubId() {
		return subId;
	}
	
	public SerializableItemStack getItemStack() {
		return itemStack;
	}

}
