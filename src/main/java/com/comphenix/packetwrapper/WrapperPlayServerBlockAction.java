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

import org.bukkit.Material;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;

public class WrapperPlayServerBlockAction extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Server.BLOCK_ACTION;

	public WrapperPlayServerBlockAction() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerBlockAction(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Location.
	 * <p>
	 * Notes: block Coordinates
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
	 * Retrieve Byte 1.
	 * <p>
	 * Notes: varies depending on block - see Block_Actions
	 * 
	 * @return The current Byte 1
	 */
	public int getByte1() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Set Byte 1.
	 * 
	 * @param value - new value.
	 */
	public void setByte1(int value) {
		handle.getIntegers().write(0, value);
	}

	/**
	 * Retrieve Byte 2.
	 * <p>
	 * Notes: varies depending on block - see Block_Actions
	 * 
	 * @return The current Byte 2
	 */
	public int getByte2() {
		return handle.getIntegers().read(1);
	}

	/**
	 * Set Byte 2.
	 * 
	 * @param value - new value.
	 */
	public void setByte2(int value) {
		handle.getIntegers().write(1, value);
	}

	/**
	 * Retrieve Block Type.
	 * <p>
	 * Notes: the block type for the block
	 * 
	 * @return The current Block Type
	 */
	public Material getBlockType() {
		return handle.getBlocks().read(0);
	}

	/**
	 * Set Block Type.
	 * 
	 * @param value - new value.
	 */
	public void setBlockType(Material value) {
		handle.getBlocks().write(0, value);
	}

}
