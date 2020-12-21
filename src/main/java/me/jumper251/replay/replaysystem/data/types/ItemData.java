package me.jumper251.replay.replaysystem.data.types;

import org.bukkit.Material;

public class ItemData extends PacketData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3882181315164039909L;

	private Material id;
	private int subId;

	public ItemData (Material id, int subId) {
		this.id    = id;
		this.subId = subId;
	}

	public Material getId () {
		return id;
	}

	public int getSubId () {
		return subId;
	}
}
