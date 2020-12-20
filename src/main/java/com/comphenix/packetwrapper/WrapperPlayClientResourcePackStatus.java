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
import com.comphenix.protocol.wrappers.EnumWrappers.ResourcePackStatus;

public class WrapperPlayClientResourcePackStatus extends AbstractPacket {
	public static final PacketType TYPE =
			PacketType.Play.Client.RESOURCE_PACK_STATUS;

	public WrapperPlayClientResourcePackStatus() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayClientResourcePackStatus(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Result.
	 * <p>
	 * Notes: successfully loaded: 0, Declined: 1, Failed download: 2, Accepted:
	 * 3
	 * 
	 * @return The current Result
	 */
	public ResourcePackStatus getResult() {
		return handle.getResourcePackStatus().read(0);
	}

	/**
	 * Set Result.
	 * 
	 * @param value - new value.
	 */
	public void setResult(ResourcePackStatus value) {
		handle.getResourcePackStatus().write(0, value);
	}
}
