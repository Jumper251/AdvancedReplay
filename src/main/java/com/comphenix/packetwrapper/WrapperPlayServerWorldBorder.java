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
import com.comphenix.protocol.wrappers.EnumWrappers.WorldBorderAction;

public class WrapperPlayServerWorldBorder extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Server.WORLD_BORDER;

	public WrapperPlayServerWorldBorder() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerWorldBorder(PacketContainer packet) {
		super(packet, TYPE);
	}

	public WorldBorderAction getAction() {
		return handle.getWorldBorderActions().read(0);
	}

	public void setAction(WorldBorderAction value) {
		handle.getWorldBorderActions().write(0, value);
	}

	public int getPortalTeleportBoundary() {
		return handle.getIntegers().read(0);
	}

	public void setPortalTeleportBoundary(int value) {
		handle.getIntegers().write(0, value);
	}

	public double getCenterX() {
		return handle.getDoubles().read(0);
	}

	public void setCenterX(double value) {
		handle.getDoubles().write(0, value);
	}

	public double getCenterZ() {
		return handle.getDoubles().read(1);
	}

	public void setCenterZ(double value) {
		handle.getDoubles().write(1, value);
	}

	public double getOldRadius() {
		return handle.getDoubles().read(2);
	}

	public void setOldRadius(double value) {
		handle.getDoubles().write(2, value);
	}

	public double getRadius() {
		return handle.getDoubles().read(3);
	}

	public void setRadius(double value) {
		handle.getDoubles().write(3, value);
	}

	public long getSpeed() {
		return handle.getLongs().read(0);
	}

	public void setSpeed(long value) {
		handle.getLongs().write(0, value);
	}

	public int getWarningTime() {
		return handle.getIntegers().read(1);
	}

	public void setWarningTime(int value) {
		handle.getIntegers().write(1, value);
	}

	public int getWarningDistance() {
		return handle.getIntegers().read(2);
	}

	public void setWarningDistance(int value) {
		handle.getIntegers().write(2, value);
	}
}
