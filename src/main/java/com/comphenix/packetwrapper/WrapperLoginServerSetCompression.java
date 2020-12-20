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

public class WrapperLoginServerSetCompression extends AbstractPacket {
	public static final PacketType TYPE =
			PacketType.Login.Server.SET_COMPRESSION;

	public WrapperLoginServerSetCompression() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperLoginServerSetCompression(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Threshold.
	 * <p>
	 * Notes: threshold is the max size of a packet before its compressed
	 * 
	 * @return The current Threshold
	 */
	public int getThreshold() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Set Threshold.
	 * 
	 * @param value - new value.
	 */
	public void setThreshold(int value) {
		handle.getIntegers().write(0, value);
	}

}
