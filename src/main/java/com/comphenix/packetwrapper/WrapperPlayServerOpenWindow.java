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

import com.comphenix.packetwrapper.util.Removed;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class WrapperPlayServerOpenWindow extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Server.OPEN_WINDOW;

	public WrapperPlayServerOpenWindow() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerOpenWindow(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Window id.
	 * <p>
	 * Notes: a unique id number for the window to be displayed. Notchian server
	 * implementation is a counter, starting at 1.
	 * 
	 * @return The current Window id
	 */
	public int getWindowID() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Set Window id.
	 * 
	 * @param value - new value.
	 */
	public void setWindowID(int value) {
		handle.getIntegers().write(0, value);
	}

	/**
	 * Retrieve Inventory Type.
	 * <p>
	 * Notes: the window type to use for display. Check below
	 * 
	 * @return The current Inventory Type
	 */
	@Removed
	public String getInventoryType() {
		return handle.getStrings().read(0);
	}

	/**
	 * Set Inventory Type.
	 * 
	 * @param value - new value.
	 */
	@Removed
	public void setInventoryType(String value) {
		handle.getStrings().write(0, value);
	}

	/**
	 * Retrieve Window title.
	 * <p>
	 * Notes: the title of the window.
	 * 
	 * @return The current Window title
	 */
	public WrappedChatComponent getWindowTitle() {
		return handle.getChatComponents().read(0);
	}

	/**
	 * Set Window title.
	 * 
	 * @param value - new value.
	 */
	public void setWindowTitle(WrappedChatComponent value) {
		handle.getChatComponents().write(0, value);
	}

	/**
	 * Retrieve Number of Slots.
	 * <p>
	 * Notes: number of slots in the window (excluding the number of slots in
	 * the player inventory).
	 * 
	 * @return The current Number of Slots
	 */
	public int getNumberOfSlots() {
		return handle.getIntegers().read(1);
	}

	/**
	 * Set Number of Slots.
	 * 
	 * @param value - new value.
	 */
	public void setNumberOfSlots(int value) {
		handle.getIntegers().write(1, value);
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
}
