/**
 * This file is part of PacketWrapper.
 * Copyright (C) 2012-2015 Kristian S. Strangeland
 * Copyright (C) 2015 dmulloy2
 * <p>
 * PacketWrapper is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * PacketWrapper is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with PacketWrapper.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.utility.MinecraftReflection;

import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperPlayServerLookAt extends AbstractPacket {

	public static final PacketType TYPE = PacketType.Play.Server.LOOK_AT;

	public WrapperPlayServerLookAt() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerLookAt(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Feet/eyes.
	 * <p>
	 * Notes: values are feet=0, eyes=1. If set to eyes, aims using the head position; otherwise aims using the feet position.
	 * @return The current Feet/eyes
	 */
	public Anchor getAnchor() {
		return handle.getEnumModifier(Anchor.class, MinecraftReflection.getMinecraftClass("ArgumentAnchor$Anchor"))
		             .readSafely(0);
	}

	/**
	 * Set Feet/eyes.
	 * @param value - new value.
	 */
	public void setAnchor(Anchor value) {
		handle.getEnumModifier(Anchor.class, MinecraftReflection.getMinecraftClass("ArgumentAnchor$Anchor"))
		      .writeSafely(0, value);
	}

	/**
	 * Retrieve Target x.
	 * <p>
	 * Notes: x coordinate of the point to face towards
	 * @return The current Target x
	 */
	public double getTargetX() {
		return handle.getDoubles().read(0);
	}

	/**
	 * Set Target x.
	 * @param value - new value.
	 */
	public void setTargetX(double value) {
		handle.getDoubles().write(0, value);
	}

	/**
	 * Retrieve Target y.
	 * <p>
	 * Notes: y coordinate of the point to face towards
	 * @return The current Target y
	 */
	public double getTargetY() {
		return handle.getDoubles().read(1);
	}

	/**
	 * Set Target y.
	 * @param value - new value.
	 */
	public void setTargetY(double value) {
		handle.getDoubles().write(1, value);
	}

	/**
	 * Retrieve Target z.
	 * <p>
	 * Notes: z coordinate of the point to face towards
	 * @return The current Target z
	 */
	public double getTargetZ() {
		return handle.getDoubles().read(2);
	}

	/**
	 * Set Target z.
	 * @param value - new value.
	 */
	public void setTargetZ(double value) {
		handle.getDoubles().write(2, value);
	}

	/**
	 * Retrieve Is entity.
	 * <p>
	 * Notes: if true, additional information about an entity is provided.
	 * @return The current Is entity
	 */
	public boolean getIsEntity() {
		return handle.getBooleans().read(0);
	}

	/**
	 * Set Is entity.
	 * @param value - new value.
	 */
	public void setIsEntity(boolean value) {
		handle.getBooleans().write(0, value);
	}

	/**
	 * Retrieve Entity ID.
	 * <p>
	 * Notes: only if is entity is true — the entity to face towards
	 * @return The current Entity ID
	 */
	public int getEntityID() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Set Entity ID.
	 * @param value - new value.
	 */
	public void setEntityID(int value) {
		handle.getIntegers().write(0, value);
	}

	/**
	 * Retrieve the entity involved in this event.
	 * @param world - the current world of the entity.
	 * @return The involved entity.
	 */
	public Entity getEntity(World world) {
		return handle.getEntityModifier(world).read(6);
	}

	/**
	 * Retrieve the entity involved in this event.
	 * @param event - the packet event.
	 * @return The involved entity.
	 */
	public Entity getEntity(PacketEvent event) {
		return getEntity(event.getPlayer().getWorld());
	}

	/**
	 * Retrieve Entity feet/eyes.
	 * <p>
	 * Notes: whether to look at the entity's eyes or feet. Same values and meanings as before, just for the entity's head/feet.
	 * @return The current Entity feet/eyes
	 */
	public Anchor getEntityAnchor() {
		return handle.getEnumModifier(Anchor.class, MinecraftReflection.getMinecraftClass("ArgumentAnchor$Anchor"))
		             .readSafely(1);
	}

	/**
	 * Set Entity feet/eyes.
	 * @param value - new value.
	 */
	public void setEntityAnchor(Anchor value) {
		handle.getEnumModifier(Anchor.class, MinecraftReflection.getMinecraftClass("ArgumentAnchor$Anchor"))
		      .writeSafely(1, value);
	}

	public enum Anchor {
		FEET,
		EYES
	}
}
