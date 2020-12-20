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

import java.security.PublicKey;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperLoginServerEncryptionBegin extends AbstractPacket {
	public static final PacketType TYPE =
			PacketType.Login.Server.ENCRYPTION_BEGIN;

	public WrapperLoginServerEncryptionBegin() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperLoginServerEncryptionBegin(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Server ID.
	 * <p>
	 * Notes: appears to be empty as of 1.7.x
	 * 
	 * @return The current Server ID
	 */
	public String getServerId() {
		return handle.getStrings().read(0);
	}

	/**
	 * Set Server ID.
	 * 
	 * @param value - new value.
	 */
	public void setServerId(String value) {
		handle.getStrings().write(0, value);
	}

	/**
	 * Retrieve Public Key.
	 * 
	 * @return The current Public Key
	 */
	public PublicKey getPublicKey() {
		return handle.getSpecificModifier(PublicKey.class).read(0);
	}

	/**
	 * Set Public Key.
	 * 
	 * @param value - new value.
	 */
	public void setPublicKey(PublicKey value) {
		handle.getSpecificModifier(PublicKey.class).write(0, value);
	}

	/**
	 * Retrieve Verify Token.
	 * 
	 * @return The current Verify Token
	 */
	public byte[] getVerifyToken() {
		return handle.getByteArrays().read(0);
	}

	/**
	 * Set Verify Token.
	 * 
	 * @param value - new value.
	 */
	public void setVerifyToken(byte[] value) {
		handle.getByteArrays().write(0, value);
	}
}
