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

public class WrapperPlayServerWindowData extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Server.WINDOW_DATA;

	public WrapperPlayServerWindowData() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerWindowData(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Window ID.
	 * <p>
	 * Notes: the id of the window.
	 * 
	 * @return The current Window ID
	 */
	public int getWindowId() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Set Window ID.
	 * 
	 * @param value - new value.
	 */
	public void setWindowId(int value) {
		handle.getIntegers().write(0, value);
	}

	/**
	 * Retrieve Property.
	 * <p>
	 * Notes: which property should be updated.
	 * 
	 * @return The current Property
	 */
	public int getProperty() {
		return handle.getIntegers().read(1);
	}

	/**
	 * Set Property.
	 * 
	 * @param value - new value.
	 */
	public void setProperty(int value) {
		handle.getIntegers().write(1, value);
	}

	/**
	 * Retrieve Value.
	 * <p>
	 * Notes: the new value for the property.
	 * 
	 * @return The current Value
	 */
	public int getValue() {
		return handle.getIntegers().read(2);
	}

	/**
	 * Set Value.
	 * 
	 * @param value - new value.
	 */
	public void setValue(int value) {
		handle.getIntegers().write(2, value);
	}

}
