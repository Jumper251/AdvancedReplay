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
import com.comphenix.protocol.wrappers.EnumWrappers.ChatVisibility;

public class WrapperPlayClientSettings extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Client.SETTINGS;

	public WrapperPlayClientSettings() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayClientSettings(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Locale.
	 * <p>
	 * Notes: en_GB
	 * 
	 * @return The current Locale
	 */
	public String getLocale() {
		return handle.getStrings().read(0);
	}

	/**
	 * Set Locale.
	 * 
	 * @param value - new value.
	 */
	public void setLocale(String value) {
		handle.getStrings().write(0, value);
	}

	/**
	 * Retrieve View distance.
	 * <p>
	 * Notes: client-side render distance(chunks)
	 * 
	 * @return The current View distance
	 */
	public int getViewDistance() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Set View distance.
	 * 
	 * @param value - new value.
	 */
	public void setViewDistance(byte value) {
		handle.getIntegers().write(0, (int) value);
	}

	/**
	 * Retrieve Chat flags.
	 * <p>
	 * Notes: chat settings. See notes below.
	 * 
	 * @return The current Chat flags
	 */
	public ChatVisibility getChatFlags() {
		return handle.getChatVisibilities().read(0);
	}

	/**
	 * Set Chat flags.
	 * 
	 * @param value - new value.
	 */
	public void setChatFlags(ChatVisibility value) {
		handle.getChatVisibilities().write(0, value);
	}

	/**
	 * Retrieve Chat colours.
	 * <p>
	 * Notes: "Colours" multiplayer setting
	 * 
	 * @return The current Chat colours
	 */
	public boolean getChatColours() {
		return handle.getBooleans().read(0);
	}

	/**
	 * Set Chat colours.
	 * 
	 * @param value - new value.
	 */
	public void setChatColours(boolean value) {
		handle.getBooleans().write(0, value);
	}

	/**
	 * Retrieve Displayed skin parts.
	 * <p>
	 * Notes: skin parts. See note below
	 * 
	 * @return The current Displayed skin parts
	 */
	public int getDisplayedSkinParts() {
		return handle.getIntegers().read(1);
	}

	/**
	 * Set Displayed skin parts.
	 * 
	 * @param value - new value.
	 */
	public void setDisplayedSkinParts(int value) {
		handle.getIntegers().write(1, value);
	}

}
