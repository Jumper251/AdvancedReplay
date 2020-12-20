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

public class WrapperPlayServerGameStateChange extends AbstractPacket {
	public static final PacketType TYPE =
			PacketType.Play.Server.GAME_STATE_CHANGE;

	public WrapperPlayServerGameStateChange() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerGameStateChange(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Reason.
	 * 
	 * @return The current Reason
	 */
	public int getReason() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Set Reason.
	 * 
	 * @param value - new value.
	 */
	public void setReason(int value) {
		handle.getIntegers().write(0, value);
	}

	/**
	 * Retrieve Value.
	 * <p>
	 * Notes: depends on reason
	 * 
	 * @return The current Value
	 */
	public float getValue() {
		return handle.getFloat().read(0);
	}

	/**
	 * Set Value.
	 * 
	 * @param value - new value.
	 */
	public void setValue(float value) {
		handle.getFloat().write(0, value);
	}

}
