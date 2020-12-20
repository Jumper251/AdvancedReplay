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
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.AutoWrapper;
import com.comphenix.protocol.wrappers.BukkitConverters;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class WrapperPlayServerMap extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Server.MAP;

	public WrapperPlayServerMap() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerMap(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Item Damage.
	 * <p>
	 * Notes: the damage value of the map being modified
	 * 
	 * @return The current Item Damage
	 */
	public int getItemDamage() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Set Item Damage.
	 * 
	 * @param value - new value.
	 */
	public void setItemDamage(int value) {
		handle.getIntegers().write(0, value);
	}

	/**
	 * Retrieve Scale.
	 * 
	 * @return The current Scale
	 */
	public byte getScale() {
		return handle.getBytes().read(0);
	}

	public enum MapIconType {
		PLAYER,
		FRAME,
		RED_MARKER,
		BLUE_MARKER,
		TARGET_X,
		TARGET_POINT,
		PLAYER_OFF_MAP,
		PLAYER_OFF_LIMITS,
		MANSION,
		MONUMENT,
		BANNER_WHITE,
		BANNER_ORANGE,
		BANNER_MAGENTA,
		BANNER_LIGHT_BLUE,
		BANNER_YELLOW,
		BANNER_LIME,
		BANNER_PINK,
		BANNER_GRAY,
		BANNER_LIGHT_GRAY,
		BANNER_CYAN,
		BANNER_PURPLE,
		BANNER_BLUE,
		BANNER_BROWN,
		BANNER_GREEN,
		BANNER_RED,
		BANNER_BLACK,
		RED_X;
	}

	public static class MapIcon {
		public MapIconType type;
		public byte x;
		public byte y;
		public byte rotation;
		public WrappedChatComponent name;
	}

	private static final AutoWrapper<MapIcon> ICON_WRAPPER = AutoWrapper.wrap(MapIcon.class, "MapIcon")
			.field(0, EnumWrappers.getGenericConverter(MinecraftReflection.getMinecraftClass("MapIcon$Type"), MapIconType.class))
			.field(4, BukkitConverters.getWrappedChatComponentConverter());

	/**
	 * Set Scale.
	 * 
	 * @param value - new value.
	 */
	public void setScale(byte value) {
		handle.getBytes().write(0, value);
	}

	public boolean getTrackingPosition() {
		return handle.getBooleans().read(0);
	}

	public void setTrackingPosition(boolean value) {
		handle.getBooleans().write(0, value);
	}

	public boolean isLocked() {
		return handle.getBooleans().read(1);
	}

	public void setLocked(boolean value) {
		handle.getBooleans().write(1, value);
	}

	public MapIcon[] getMapIcons() {
		Object[] iconHandles = (Object[]) handle.getModifier().read(4);
		MapIcon[] wrappers = new MapIcon[iconHandles.length];

		for (int i = 0; i < iconHandles.length; i++) {
			wrappers[i] = ICON_WRAPPER.getSpecific(iconHandles[i]);
		}

		return wrappers;
	}

	public void setMapIcons(MapIcon[] value) {
		Object[] iconHandles = new Object[value.length];

		for (int i = 0; i < value.length; i++) {
			iconHandles[i] = ICON_WRAPPER.getGeneric(value[i]);
		}

		handle.getModifier().write(4, iconHandles);
	}

	public int getColumns() {
		return handle.getIntegers().read(1);
	}

	public void setColumns(int value) {
		handle.getIntegers().write(1, value);
	}

	public int getRows() {
		return handle.getIntegers().read(2);
	}

	public void setRows(int value) {
		handle.getIntegers().write(2, value);
	}

	public int getX() {
		return handle.getIntegers().read(3);
	}

	public void setX(int value) {
		handle.getIntegers().write(3, value);
	}

	public int getZ() {
		return handle.getIntegers().read(4);
	}

	public void setZ(int value) {
		handle.getIntegers().write(4, value);
	}

	public byte[] getData() {
		return handle.getByteArrays().read(0);
	}

	public void setData(byte[] value) {
		handle.getByteArrays().write(0, value);
	}
}
