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

public class WrapperPlayServerResourcePackSend extends AbstractPacket {
	public static final PacketType TYPE =
			PacketType.Play.Server.RESOURCE_PACK_SEND;

	public WrapperPlayServerResourcePackSend() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerResourcePackSend(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve URL.
	 * <p>
	 * Notes: the URL to the resource pack.
	 * 
	 * @return The current URL
	 */
	public String getUrl() {
		return handle.getStrings().read(0);
	}

	/**
	 * Set URL.
	 * 
	 * @param value - new value.
	 */
	public void setUrl(String value) {
		handle.getStrings().write(0, value);
	}

	/**
	 * Retrieve Hash.
	 * <p>
	 * Notes: a 40 character hexadecimal and lower-case SHA-1 hash of the
	 * resource pack file. (must be lower case in order to work) If it's not a
	 * 40 character hexadecimal string, the client will not use it for hash
	 * verification and likely waste bandwidth - but it will still treat it as a
	 * unique id
	 * 
	 * @return The current Hash
	 */
	public String getHash() {
		return handle.getStrings().read(1);
	}

	/**
	 * Set Hash.
	 * 
	 * @param value - new value.
	 */
	public void setHash(String value) {
		handle.getStrings().write(1, value);
	}

}
