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
import com.comphenix.protocol.wrappers.EnumWrappers.Hand;

public class WrapperPlayClientUseItem extends AbstractPacket {

	public static final PacketType TYPE = PacketType.Play.Client.USE_ITEM;

	public WrapperPlayClientUseItem() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayClientUseItem(PacketContainer packet) {
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

	public Direction getFace() {
		return handle.getDirections().read(0);
	}

	public void setFace(Direction value) {
		handle.getDirections().write(0, value);
	}

	public Hand getHand() {
		return handle.getHands().read(0);
	}

	public void setHand(Hand value) {
		handle.getHands().write(0, value);
	}

	/**
	 * Retrieve Cursor Position X.
	 * <p>
	 * Notes: the position of the crosshair on the block, from 0 to 15
	 * increasing from west to east
	 * 
	 * @return The current Cursor Position X
	 */
	public float getCursorPositionX() {
		return handle.getFloat().read(0);
	}

	/**
	 * Set Cursor Position X.
	 * 
	 * @param value - new value.
	 */
	public void setCursorPositionX(float value) {
		handle.getFloat().write(0, value);
	}

	/**
	 * Retrieve Cursor Position Y.
	 * <p>
	 * Notes: the position of the crosshair on the block, from 0 to 15
	 * increasing from bottom to top
	 * 
	 * @return The current Cursor Position Y
	 */
	public float getCursorPositionY() {
		return handle.getFloat().read(1);
	}

	/**
	 * Set Cursor Position Y.
	 * 
	 * @param value - new value.
	 */
	public void setCursorPositionY(float value) {
		handle.getFloat().write(1, value);
	}

	/**
	 * Retrieve Cursor Position Z.
	 * <p>
	 * Notes: the position of the crosshair on the block, from 0 to 15
	 * increasing from north to south
	 * 
	 * @return The current Cursor Position Z
	 */
	public float getCursorPositionZ() {
		return handle.getFloat().read(2);
	}

	/**
	 * Set Cursor Position Z.
	 * 
	 * @param value - new value.
	 */
	public void setCursorPositionZ(float value) {
		handle.getFloat().write(2, value);
	}
}