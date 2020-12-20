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

public class WrapperPlayServerEntityEffect extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Server.ENTITY_EFFECT;

	public WrapperPlayServerEntityEffect() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerEntityEffect(PacketContainer packet) {
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
	 * Retrieve Effect ID.
	 * <p>
	 * Notes: see [[1]]
	 * 
	 * @return The current Effect ID
	 */
	public byte getEffectID() {
		return handle.getBytes().read(0);
	}

	/**
	 * Set Effect ID.
	 * 
	 * @param value - new value.
	 */
	public void setEffectID(byte value) {
		handle.getBytes().write(0, (byte) (value & 255));
	}

	/**
	 * Retrieve Amplifier.
	 * 
	 * @return The current Amplifier
	 */
	public byte getAmplifier() {
		return handle.getBytes().read(1);
	}

	/**
	 * Set Amplifier.
	 * 
	 * @param value - new value.
	 */
	public void setAmplifier(byte value) {
		handle.getBytes().write(1, (byte) (value & 255));
	}

	/**
	 * Retrieve Duration.
	 * 
	 * @return The current Duration
	 */
	public int getDuration() {
		return handle.getIntegers().read(1);
	}

	/**
	 * Set Duration.
	 * 
	 * @param value - new value.
	 */
	public void setDuration(int value) {
		handle.getIntegers().write(1, value);
	}

	/**
	 * Retrieve Hide Particles.
	 * 
	 * @return The current Hide Particles
	 */
	public boolean getHideParticles() {
		return handle.getBytes().read(2) == 0;
	}

	/**
	 * Set Hide Particles.
	 * 
	 * @param value - new value.
	 */
	public void setHideParticles(boolean value) {
		handle.getBytes().write(2, (byte) (value ? 0 : 1));
	}

}
