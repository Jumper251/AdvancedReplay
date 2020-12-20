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

import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerAction;

public class WrapperPlayClientEntityAction extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Client.ENTITY_ACTION;

	public WrapperPlayClientEntityAction() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayClientEntityAction(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Entity ID.
	 * <p>
	 * Notes: entity's ID
	 * 
	 * @return The current Entity ID
	 */
	public int getEntityID() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Set Entity ID.
	 * 
	 * @param value - new value.
	 */
	public void setEntityID(int value) {
		handle.getIntegers().write(0, value);
	}

	/**
	 * Retrieve the entity of the painting that will be spawned.
	 * 
	 * @param world - the current world of the entity.
	 * @return The spawned entity.
	 */
	public Entity getEntity(World world) {
		return handle.getEntityModifier(world).read(0);
	}

	/**
	 * Retrieve the entity of the painting that will be spawned.
	 * 
	 * @param event - the packet event.
	 * @return The spawned entity.
	 */
	public Entity getEntity(PacketEvent event) {
		return getEntity(event.getPlayer().getWorld());
	}

	/**
	 * Retrieve Action ID.
	 * <p>
	 * Notes: the ID of the action, see below.
	 * 
	 * @return The current Action ID
	 */
	public PlayerAction getAction() {
		return handle.getPlayerActions().read(0);
	}

	/**
	 * Set Action ID.
	 * 
	 * @param value - new value.
	 */
	public void setAction(PlayerAction value) {
		handle.getPlayerActions().write(0, value);
	}

	/**
	 * Retrieve Jump Boost.
	 * <p>
	 * Notes: horse jump boost. Ranged from 0 -> 100.
	 * 
	 * @return The current Jump Boost
	 */
	public int getJumpBoost() {
		return handle.getIntegers().read(1);
	}

	/**
	 * Set Jump Boost.
	 * 
	 * @param value - new value.
	 */
	public void setJumpBoost(int value) {
		handle.getIntegers().write(1, value);
	}

}
