package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayClientTradeSelect extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Client.TR_SEL;

	public WrapperPlayClientTradeSelect() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public int getSlot() {
		return handle.getIntegers().read(0);
	}

	public void setSlot(int value) {
		handle.getIntegers().write(0, value);
	}
}
