/**
 * This file is part of PacketWrapper.
 * Copyright (C) 2012-2015 Kristian S. Strangeland
 * Copyright (C) 2015 dmulloy2
 *
 * PacketWrapper is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PacketWrapper is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with PacketWrapper.  If not, see <http://www.gnu.org/licenses/>.
 */
package replaylib.com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;

import me.jumper251.replay.utils.VersionUtil;
import me.jumper251.replay.utils.VersionUtil.VersionEnum;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class WrapperPlayClientUseEntity extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Client.USE_ENTITY;

	public WrapperPlayClientUseEntity() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayClientUseEntity(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve entity ID of the target.
	 * @return The current entity ID
	 */
	public int getTargetID() {
		return handle.getIntegers().read(0);
	}

	/**
	 * Retrieve the entity that was targeted.
	 * @param world - the current world of the entity.
	 * @return The targeted entity.
	 */
	public Entity getTarget(World world) {
		return handle.getEntityModifier(world).read(0);
	}

	/**
	 * Retrieve the entity that was targeted.
	 * @param event - the packet event.
	 * @return The targeted entity.
	 */
	public Entity getTarget(PacketEvent event) {
		return getTarget(event.getPlayer().getWorld());
	}

	/**
	 * Set entity ID of the target.
	 * @param value - new value.
	 */
	public void setTargetID(int value) {
		handle.getIntegers().write(0, value);
	}

	/**
	 * Retrieve Type.
	 * @return The current Type
	 */
	public EntityUseAction getType() {
		if (VersionUtil.isAbove(VersionEnum.V1_17)) 
			return handle.getEnumEntityUseActions().read(0).getAction();
		
		return handle.getEntityUseActions().read(0);
	}

	/**
	 * Set Type.
	 * @param value - new value.
	 */
	public void setType(EntityUseAction value) {
		handle.getEntityUseActions().write(0, value);
	}

	/**
	 * Retrieve the target vector.
	 * <p>
	 * Notes: Only if {@link #getType()} is {@link EntityUseAction#INTERACT_AT}.
	 * 
	 * @return The target vector or null
	 */
	public Vector getTargetVector() {
		return handle.getVectors().read(0);
	}

	/**
	 * Set the target vector.
	 * @param value - new value.
	 */
	public void setTargetVector(Vector value) {
		handle.getVectors().write(0, value);
	}
}