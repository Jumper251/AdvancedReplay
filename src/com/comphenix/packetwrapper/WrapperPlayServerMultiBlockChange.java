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
import com.comphenix.protocol.wrappers.ChunkCoordIntPair;
import com.comphenix.protocol.wrappers.MultiBlockChangeInfo;

public class WrapperPlayServerMultiBlockChange extends AbstractPacket {
	public static final PacketType TYPE =
			PacketType.Play.Server.MULTI_BLOCK_CHANGE;

	public WrapperPlayServerMultiBlockChange() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerMultiBlockChange(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve the chunk that has been altered.
	 * 
	 * @return The current chunk
	 */
	public ChunkCoordIntPair getChunk() {
		return handle.getChunkCoordIntPairs().read(0);
	}

	/**
	 * Set the chunk that has been altered.
	 * 
	 * @param value - new value
	 */
	public void setChunk(ChunkCoordIntPair value) {
		handle.getChunkCoordIntPairs().write(0, value);
	}

	/**
	 * Retrieve a copy of the record data as a block change array.
	 * 
	 * @return The copied block change array.
	 */
	public MultiBlockChangeInfo[] getRecords() {
		return handle.getMultiBlockChangeInfoArrays().read(0);
	}

	/**
	 * Set the record data using the given helper array.
	 * 
	 * @param value - new value
	 */
	public void setRecords(MultiBlockChangeInfo[] value) {
		handle.getMultiBlockChangeInfoArrays().write(0, value);
	}
}
