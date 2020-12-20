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
import com.comphenix.protocol.PacketType.Protocol;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperHandshakingClientSetProtocol extends AbstractPacket {
	public static final PacketType TYPE =
			PacketType.Handshake.Client.SET_PROTOCOL;

	public WrapperHandshakingClientSetProtocol() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperHandshakingClientSetProtocol(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Protocol Version.
	 * <p>
	 * Notes: (4 as of 1.7.2)
	 * 
	 * @return The current Protocol Version
	 */
	public int getProtocolVersion() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Set Protocol Version.
	 * 
	 * @param value - new value.
	 */
	public void setProtocolVersion(int value) {
		handle.getIntegers().write(0, value);
	}

	/**
	 * Retrieve Server Address (hostname or IP).
	 * <p>
	 * Notes: localhost
	 * 
	 * @return The current Server Address (hostname or IP)
	 */
	public String getServerAddressHostnameOrIp() {
		return handle.getStrings().read(0);
	}

	/**
	 * Set Server Address (hostname or IP).
	 * 
	 * @param value - new value.
	 */
	public void setServerAddressHostnameOrIp(String value) {
		handle.getStrings().write(0, value);
	}

	/**
	 * Retrieve Server Port.
	 * <p>
	 * Notes: 25565
	 * 
	 * @return The current Server Port
	 */
	public int getServerPort() {
		return handle.getIntegers().read(1);
	}

	/**
	 * Set Server Port.
	 * 
	 * @param value - new value.
	 */
	public void setServerPort(int value) {
		handle.getIntegers().write(1, value);
	}

	/**
	 * Retrieve Next state.
	 * <p>
	 * Notes: 1 for status, 2 for login
	 * 
	 * @return The current Next state
	 */
	public Protocol getNextState() {
		return handle.getProtocols().read(0);
	}

	/**
	 * Set Next state.
	 * 
	 * @param value - new value.
	 */
	public void setNextState(Protocol value) {
		handle.getProtocols().write(0, value);
	}

}
