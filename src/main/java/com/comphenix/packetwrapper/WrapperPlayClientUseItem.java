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

import com.comphenix.packetwrapper.util.Removed;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.AutoWrapper;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.BukkitConverters;
import com.comphenix.protocol.wrappers.EnumWrappers;
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
	@Removed
	public BlockPosition getLocation() {
		return handle.getBlockPositionModifier().read(0);
	}

	/**
	 * Set Location.
	 * 
	 * @param value - new value.
	 */
	@Removed
	public void setLocation(BlockPosition value) {
		handle.getBlockPositionModifier().write(0, value);
	}

	@Removed
	public Direction getFace() {
		return handle.getDirections().read(0);
	}

	@Removed
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
	@Removed
	public float getCursorPositionX() {
		return handle.getFloat().read(0);
	}

	/**
	 * Set Cursor Position X.
	 * 
	 * @param value - new value.
	 */
	@Removed
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
	@Removed
	public float getCursorPositionY() {
		return handle.getFloat().read(1);
	}

	/**
	 * Set Cursor Position Y.
	 * 
	 * @param value - new value.
	 */
	@Removed
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
	@Removed
	public float getCursorPositionZ() {
		return handle.getFloat().read(2);
	}

	/**
	 * Set Cursor Position Z.
	 * 
	 * @param value - new value.
	 */
	@Removed
	public void setCursorPositionZ(float value) {
		handle.getFloat().write(2, value);
	}

	public static class MovingObjectPosition {
		public Direction direction;
		public BlockPosition position;
		public boolean insideBlock;
	}

	private static final Class<?> POSITION_CLASS = MinecraftReflection.getMinecraftClass("MovingObjectPositionBlock");

	private static final AutoWrapper<MovingObjectPosition> AUTO_WRAPPER = AutoWrapper.wrap(MovingObjectPosition.class, POSITION_CLASS)
			.field(0, EnumWrappers.getDirectionConverter())
			.field(1, BlockPosition.getConverter());

	public MovingObjectPosition getPosition() {
		return handle.getModifier().withType(POSITION_CLASS, AUTO_WRAPPER).read(0);
	}

	public void setPosition(MovingObjectPosition position) {
		handle.getModifier().withType(POSITION_CLASS, AUTO_WRAPPER).write(0, position);
	}
}
