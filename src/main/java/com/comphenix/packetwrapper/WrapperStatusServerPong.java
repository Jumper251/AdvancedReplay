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

public class WrapperStatusServerPong extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Status.Server.PONG;

	public WrapperStatusServerPong() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperStatusServerPong(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Time.
	 * <p>
	 * Notes: should be the same as sent by the client
	 * 
	 * @return The current Time
	 */
	public long getTime() {
		return handle.getLongs().read(0);
	}

	/**
	 * Set Time.
	 * 
	 * @param value - new value.
	 */
	public void setTime(long value) {
		handle.getLongs().write(0, value);
	}

}
