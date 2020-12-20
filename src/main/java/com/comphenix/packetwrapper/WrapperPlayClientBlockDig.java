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
import com.comphenix.protocol.wrappers.EnumWrappers.Direction;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerDigType;

public class WrapperPlayClientBlockDig extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Client.BLOCK_DIG;

	public WrapperPlayClientBlockDig() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayClientBlockDig(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Location.
	 * <p>
	 * Notes: block position
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

	public Direction getDirection() {
		return handle.getDirections().read(0);
	}

	public void setDirection(Direction value) {
		handle.getDirections().write(0, value);
	}

	/**
	 * Retrieve Status.
	 * <p>
	 * Notes: the action the player is taking against the block (see below)
	 * 
	 * @return The current Status
	 */
	public PlayerDigType getStatus() {
		return handle.getPlayerDigTypes().read(0);
	}

	/**
	 * Set Status.
	 * 
	 * @param value - new value.
	 */
	public void setStatus(PlayerDigType value) {
		handle.getPlayerDigTypes().write(0, value);
	}
}
