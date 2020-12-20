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
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.nbt.NbtBase;

public class WrapperPlayServerTileEntityData extends AbstractPacket {
	public static final PacketType TYPE =
			PacketType.Play.Server.TILE_ENTITY_DATA;

	public WrapperPlayServerTileEntityData() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerTileEntityData(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Location.
	 * 
	 * @return The current Location
	 */
	public BlockPosition getLocation() {
		return handle.getBlockPositionModifier().read(0);
	}

	/**
	 * Set Location.
	 * 
	 * @param value - new value.
	 */
	public void setLocation(BlockPosition value) {
		handle.getBlockPositionModifier().write(0, value);
	}

	/**
	 * Retrieve Action.
	 * <p>
	 * Notes: the type of update to perform
	 * 
	 * @return The current Action
	 */
	public int getAction() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Set Action.
	 * 
	 * @param value - new value.
	 */
	public void setAction(int value) {
		handle.getIntegers().write(0, value);
	}

	/**
	 * Retrieve NBT Data.
	 * <p>
	 * Notes: if not present then its TAG_END (0)
	 * 
	 * @return The current NBT Data
	 */
	public NbtBase<?> getNbtData() {
		return handle.getNbtModifier().read(0);
	}

	/**
	 * Set NBT Data.
	 * 
	 * @param value - new value.
	 */
	public void setNbtData(NbtBase<?> value) {
		handle.getNbtModifier().write(0, value);
	}

}
