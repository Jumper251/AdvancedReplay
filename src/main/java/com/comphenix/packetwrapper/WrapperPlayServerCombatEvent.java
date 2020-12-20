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
import com.comphenix.protocol.wrappers.EnumWrappers.CombatEventType;

public class WrapperPlayServerCombatEvent extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Server.COMBAT_EVENT;

	public WrapperPlayServerCombatEvent() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerCombatEvent(PacketContainer packet) {
		super(packet, TYPE);
	}

	// ---- All

	/**
	 * Retrieve Event.
	 * <p>
	 * Notes: 0 ENTER_COMBAT, 1 END_COMBAT, 2 ENTITY_DEAD
	 * 
	 * @return The current Event
	 */
	public CombatEventType getEvent() {
		return handle.getCombatEvents().read(0);
	}

	/**
	 * Set Event.
	 * 
	 * @param value - new value.
	 */
	public void setEvent(CombatEventType value) {
		handle.getCombatEvents().write(0, value);
	}

	// ---- END_COMBAT

	public int getDuration() {
		if (getEvent() != CombatEventType.END_COMBAT)
			throw new IllegalStateException(
					"Duration only exists for END_COMBAT");

		return handle.getIntegers().read(0);
	}

	public void setDuration(int value) {
		if (getEvent() != CombatEventType.END_COMBAT)
			throw new IllegalStateException(
					"Duration only exists for END_COMBAT");

		handle.getIntegers().write(0, value);
	}

	// ---- ENTITY_DIED

	public int getPlayerID() {
		if (getEvent() != CombatEventType.ENTITY_DIED)
			throw new IllegalStateException(
					"Player ID only exists for ENTITY_DEAD");

		return handle.getIntegers().read(0);
	}

	public void setPlayerId(int value) {
		if (getEvent() != CombatEventType.ENTITY_DIED)
			throw new IllegalStateException(
					"Player ID only exists for ENTITY_DEAD");

		handle.getIntegers().write(0, value);
	}

	public int getEntityID() {
		CombatEventType event = getEvent();
		switch (event) {
			case END_COMBAT:
			case ENTITY_DIED:
				return handle.getIntegers().read(1);
			default:
				throw new IllegalStateException("Entity ID does not exist for "
						+ event);

		}
	}

	public void setEntityId(int value) {
		CombatEventType event = getEvent();
		switch (event) {
			case END_COMBAT:
			case ENTITY_DIED:
				handle.getIntegers().write(1, value);
			default:
				throw new IllegalStateException("Entity ID does not exist for "
						+ event);

		}
	}

	public String getMessage() {
		if (getEvent() != CombatEventType.ENTITY_DIED)
			throw new IllegalStateException(
					"Message only exists for ENTITY_DEAD");

		return handle.getStrings().read(0);
	}

	public void setMessage(String value) {
		if (getEvent() != CombatEventType.ENTITY_DIED)
			throw new IllegalStateException(
					"Message only exists for ENTITY_DEAD");

		handle.getStrings().write(0, value);
	}
}
