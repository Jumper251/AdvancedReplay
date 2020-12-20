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

import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerWindowItems extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Server.WINDOW_ITEMS;

	public WrapperPlayServerWindowItems() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerWindowItems(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Window ID.
	 * <p>
	 * Notes: the id of window which items are being sent for. 0 for player
	 * inventory.
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
	 * Retrieve Slot data.
	 * 
	 * @return The current Slot data
	 */
	public List<ItemStack> getSlotData() {
		return handle.getItemListModifier().read(0);
	}

	/**
	 * Set Slot data.
	 * 
	 * @param value - new value.
	 */
	public void setSlotData(List<ItemStack> value) {
		handle.getItemListModifier().write(0, value);
	}

}
