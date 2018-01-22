package me.jumper251.replay.replaysystem.data.types;


public class InvData extends PacketData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5055461506908394060L;

	
	private ItemData head, chest, leg, boots, mainHand, offHand;
	
	public InvData (ItemData head, ItemData chest, ItemData leg, ItemData boots, ItemData mainHand, ItemData offHand) {
		this.head = head;
		this.chest = chest;
		this.leg = leg;
		this.boots = boots;
		this.mainHand = mainHand;
		this.offHand = offHand;
	}
	
	public InvData() {
		
	}
	
	public ItemData getBoots() {
		return boots;
	}
	
	public ItemData getChest() {
		return chest;
	}
	
	public ItemData getHead() {
		return head;
	}
	
	public ItemData getLeg() {
		return leg;
	}
	
	public ItemData getMainHand() {
		return mainHand;
	}
	
	public ItemData getOffHand() {
		return offHand;
	}
	
	public void setBoots(ItemData boots) {
		this.boots = boots;
	}
	
	public void setChest(ItemData chest) {
		this.chest = chest;
	}
	
	public void setHead(ItemData head) {
		this.head = head;
	}
	
	public void setLeg(ItemData leg) {
		this.leg = leg;
	}
	
	public void setMainHand(ItemData mainHand) {
		this.mainHand = mainHand;
	}
	
	public void setOffHand(ItemData offHand) {
		this.offHand = offHand;
	}
	
	
}
