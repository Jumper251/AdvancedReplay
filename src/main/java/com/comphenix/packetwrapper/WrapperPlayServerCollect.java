/**
 * PacketWrapper - ProtocolLib wrappers for Minecraft packets
 * Copyright (C) dmulloy2 <http://dmulloy2.net>
 * Copyright (C) Kristian S. Strangeland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerCollect extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Server.COLLECT;

	public WrapperPlayServerCollect() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerCollect(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Collected Entity ID.
	 * 
	 * @return The current Collected Entity ID
	 */
	public int getCollectedEntityId() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Set Collected Entity ID.
	 * 
	 * @param value - new value.
	 */
	public void setCollectedEntityId(int value) {
		handle.getIntegers().write(0, value);
	}

	/**
	 * Retrieve Collector Entity ID.
	 * 
	 * @return The current Collector Entity ID
	 */
	public int getCollectorEntityId() {
		return handle.getIntegers().read(1);
	}

	/**
	 * Set Collector Entity ID.
	 * 
	 * @param value - new value.
	 */
	public void setCollectorEntityId(int value) {
		handle.getIntegers().write(1, value);
	}

}
