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

import java.util.UUID;

import org.bukkit.boss.BarColor;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class WrapperPlayServerBoss extends AbstractPacket {

	public static final PacketType TYPE = PacketType.Play.Server.BOSS;

	public WrapperPlayServerBoss() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerBoss(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve UUID.
	 * <p>
	 * Notes: unique ID for this bar
	 * 
	 * @return The current UUID
	 */
	public UUID getUniqueId() {
		return handle.getUUIDs().read(0);
	}

	/**
	 * Set UUID.
	 * 
	 * @param value - new value.
	 */
	public void setUniqueId(UUID value) {
		handle.getUUIDs().write(0, value);
	}

	public Action getAction() {
		return handle.getEnumModifier(Action.class, 1).read(0);
	}

	public void setAction(Action value) {
		handle.getEnumModifier(Action.class, 1).write(0, value);
	}

	public WrappedChatComponent getTitle() {
		return handle.getChatComponents().read(0);
	}

	public void setTitle(WrappedChatComponent value) {
		handle.getChatComponents().write(0, value);
	}

	public float getHealth() {
		return handle.getFloat().read(0);
	}

	public void setHealth(float value) {
		handle.getFloat().write(0, value);
	}

	public BarColor getColor() {
		return handle.getEnumModifier(BarColor.class, 4).read(0);
	}

	public void setColor(BarColor value) {
		handle.getEnumModifier(BarColor.class, 4).write(0, value);
	}

	public BarStyle getStyle() {
		return handle.getEnumModifier(BarStyle.class, 5).read(0);
	}

	public void setStyle(BarStyle value) {
		handle.getEnumModifier(BarStyle.class, 5).write(0, value);
	}

	public boolean isDarkenSky() {
		return handle.getBooleans().read(0);
	}

	public void setDarkenSky(boolean value) {
		handle.getBooleans().write(0, value);
	}

	public boolean isPlayMusic() {
		return handle.getBooleans().read(1);
	}

	public void setPlayMusic(boolean value) {
		handle.getBooleans().write(1, value);
	}

	public boolean isCreateFog() {
		return handle.getBooleans().read(2);
	}

	public void setCreateFog(boolean value) {
		handle.getBooleans().write(2, value);
	}

	public static enum Action {
        ADD, REMOVE, UPDATE_PCT, UPDATE_NAME, UPDATE_STYLE, UPDATE_PROPERTIES;
	}

	public static enum BarStyle {
		PROGRESS, NOTCHED_6, NOTCHED_10, NOTCHED_12, NOTCHED_20;
	}
}
