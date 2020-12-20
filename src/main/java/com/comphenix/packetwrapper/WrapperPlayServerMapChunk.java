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

import java.util.List;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;

public class WrapperPlayServerMapChunk extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Server.MAP_CHUNK;

	public WrapperPlayServerMapChunk() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerMapChunk(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Chunk X.
	 * <p>
	 * Notes: chunk X coordinate
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
	 * Notes: chunk Z coordinate
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

	/**
	 * Retrieve Ground-Up continuous.
	 * <p>
	 * Notes: this is True if the packet represents all sections in this
	 * vertical column, where the primary bit map specifies exactly which
	 * sections are included, and which are air
	 * 
	 * @return The current Ground-Up continuous
	 */
	public boolean getGroundUpContinuous() {
		return handle.getBooleans().read(0);
	}

	/**
	 * Set Ground-Up continuous.
	 * 
	 * @param value - new value.
	 */
	public void setGroundUpContinuous(boolean value) {
		handle.getBooleans().write(0, value);
	}

	/**
	 * Bitmask with bits set to 1 for every 16×16×16 chunk section whose data is
	 * included in Data. The least significant bit represents the chunk section at
	 * the bottom of the chunk column (from y=0 to y=15).
	 * @return the bitmask
	 */
	public int getBitmask() {
		return handle.getIntegers().read(2);
	}

	public void setBitmask(int value) {
		handle.getIntegers().write(2, value);
	}

	/**
	 * See <a href="http://wiki.vg/Chunk_Format#Data_structure">the wiki</a>
	 * @return the data array
	 */
	public byte[] getData() {
		return handle.getByteArrays().read(0);
	}

	public void setData(byte[] value) {
		handle.getByteArrays().write(0, value);
	}

	/**
	 * @return all block entities in the chunk
	 */
	public List<NbtBase<?>> getTileEntities() {
		return handle.getListNbtModifier().read(0);
	}

	public void setTileEntities(List<NbtBase<?>> value) {
		handle.getListNbtModifier().write(0, value);
	}
}
