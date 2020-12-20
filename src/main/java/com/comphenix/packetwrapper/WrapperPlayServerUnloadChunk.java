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

public class WrapperPlayServerUnloadChunk extends AbstractPacket {

	public static final PacketType TYPE = PacketType.Play.Server.UNLOAD_CHUNK;

	public WrapperPlayServerUnloadChunk() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerUnloadChunk(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Chunk X.
	 * <p>
	 * Notes: block coordinate divided by 16, rounded down
	 * 
	 * @return The current Chunk X
	 */
	public int getChunkX() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Set Chunk X.
	 * 
	 * @param value - new value.
	 */
	public void setChunkX(int value) {
		handle.getIntegers().write(0, value);
	}

	/**
	 * Retrieve Chunk Z.
	 * <p>
	 * Notes: block coordinate divided by 16, rounded down
	 * 
	 * @return The current Chunk Z
	 */
	public int getChunkZ() {
		return handle.getIntegers().read(1);
	}

	/**
	 * Set Chunk Z.
	 * 
	 * @param value - new value.
	 */
	public void setChunkZ(int value) {
		handle.getIntegers().write(1, value);
	}

}
