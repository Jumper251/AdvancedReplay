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

import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerSetSlot extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Server.SET_SLOT;

	public WrapperPlayServerSetSlot() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerSetSlot(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Window ID.
	 * <p>
	 * Notes: the window which is being updated. 0 for player inventory. Note
	 * that all known window types include the player inventory. This packet
	 * will only be sent for the currently opened window while the player is
	 * performing actions, even if it affects the player inventory. After the
	 * window is closed, a number of these packets are sent to update the
	 * player's inventory window (0).
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
	 * Retrieve Slot.
	 * <p>
	 * Notes: the slot that should be updated
	 * 
	 * @return The current Slot
	 */
	public int getSlot() {
		return handle.getIntegers().read(1);
	}

	/**
	 * Set Slot.
	 * 
	 * @param value - new value.
	 */
	public void setSlot(int value) {
		handle.getIntegers().write(1, value);
	}

	/**
	 * Retrieve Slot data.
	 * 
	 * @return The current Slot data
	 */
	public ItemStack getSlotData() {
		return handle.getItemModifier().read(0);
	}

	/**
	 * Set Slot data.
	 * 
	 * @param value - new value.
	 */
	public void setSlotData(ItemStack value) {
		handle.getItemModifier().write(0, value);
	}

}
