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

public class WrapperPlayClientTransaction extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Client.TRANSACTION;

	public WrapperPlayClientTransaction() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayClientTransaction(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Window ID.
	 * <p>
	 * Notes: the id of the window that the action occurred in.
	 * 
	 * @return The current Window ID
	 */
	public int getWindowId() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Set Window ID.
	 * 
	 * @param value - new value.
	 */
	public void setWindowId(byte value) {
		handle.getIntegers().write(0, (int) value);
	}

	/**
	 * Retrieve Action number.
	 * <p>
	 * Notes: every action that is to be accepted has a unique number. This
	 * field corresponds to that number.
	 * 
	 * @return The current Action number
	 */
	public short getActionNumber() {
		return handle.getShorts().read(0);
	}

	/**
	 * Set Action number.
	 * 
	 * @param value - new value.
	 */
	public void setActionNumber(short value) {
		handle.getShorts().write(0, value);
	}

	/**
	 * Retrieve Accepted.
	 * <p>
	 * Notes: whether the action was accepted.
	 * 
	 * @return The current Accepted
	 */
	public boolean getAccepted() {
		return handle.getBooleans().read(0);
	}

	/**
	 * Set Accepted.
	 * 
	 * @param value - new value.
	 */
	public void setAccepted(boolean value) {
		handle.getBooleans().write(0, value);
	}

}
