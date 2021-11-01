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

import org.bukkit.Location;
import org.bukkit.World;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;

public class WrapperPlayServerBlockChange extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Server.BLOCK_CHANGE;

	public WrapperPlayServerBlockChange() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerBlockChange(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Location.
	 * <p>
	 * Notes: block Coordinates
	 * 
	 * @return The current Location
	 */
	public BlockPosition getLocation() {
		return handle.getBlockPositionModifier().read(0);
	}

	/**
	 * Set Location.
	 * 
	 * @param value - new value.
	 */
	public void setLocation(BlockPosition value) {
		handle.getBlockPositionModifier().write(0, value);
	}

	/**
	 * Retrieve the Bukkit Location.
	 * 
	 * @param world World for the location
	 * @return Bukkit Location
	 */
	public Location getBukkitLocation(World world) {
		return getLocation().toVector().toLocation(world);
	}

	/**
	 * Retrieve Block Data.
	 * 
	 * @return The current Block Data
	 */
	public WrappedBlockData getBlockData() {
		return handle.getBlockData().read(0);
	}

	/**
	 * Set Block Data.
	 * 
	 * @param value - new value.
	 */
	public void setBlockData(WrappedBlockData value) {
		handle.getBlockData().write(0, value);
	}
}
