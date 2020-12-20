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

public class WrapperLoginClientEncryptionBegin extends AbstractPacket {
	public static final PacketType TYPE =
			PacketType.Login.Client.ENCRYPTION_BEGIN;

	public WrapperLoginClientEncryptionBegin() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperLoginClientEncryptionBegin(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Shared Secret.
	 * 
	 * @return The current Shared Secret
	 */
	public byte[] getSharedSecret() {
		return handle.getByteArrays().read(0);
	}

	/**
	 * Set Shared Secret.
	 * 
	 * @param value - new value.
	 */
	public void setSharedSecret(byte[] value) {
		handle.getByteArrays().write(0, value);
	}

	/**
	 * Retrieve Verify Token.
	 * 
	 * @return The current Verify Token
	 */
	public byte[] getVerifyToken() {
		return handle.getByteArrays().read(1);
	}

	/**
	 * Set Verify Token.
	 * 
	 * @param value - new value.
	 */
	public void setVerifyToken(byte[] value) {
		handle.getByteArrays().write(1, value);
	}
}
