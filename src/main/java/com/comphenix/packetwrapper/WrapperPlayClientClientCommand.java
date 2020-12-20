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
import com.comphenix.protocol.wrappers.EnumWrappers.ClientCommand;

public class WrapperPlayClientClientCommand extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Client.CLIENT_COMMAND;

	public WrapperPlayClientClientCommand() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayClientClientCommand(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Action ID.
	 * <p>
	 * Notes: see below
	 * 
	 * @return The current Action ID
	 */
	public ClientCommand getAction() {
		return handle.getClientCommands().read(0);
	}

	/**
	 * Set Action ID.
	 * 
	 * @param value - new value.
	 */
	public void setAction(ClientCommand value) {
		handle.getClientCommands().write(0, value);
	}

}
