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
package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperPlayClientEntityNbtQuery extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Client.ENTITY_NBT_QUERY;
    
    public WrapperPlayClientEntityNbtQuery() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientEntityNbtQuery(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve Transaction ID.
     * <p>
     * Notes: an incremental ID so that the client can verify that the response matches.
     * @return The current Transaction ID
     */
    public int getTransactionId() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set Transaction ID.
     * @param value - new value.
     */
    public void setTransactionId(int value) {
        handle.getIntegers().write(0, value);
    }
    
    /**
     * Retrieve Entity ID.
     * <p>
     * Notes: the ID of the entity to query.
     * @return The current Entity ID
     */
    public int getEntityID() {
        return handle.getIntegers().read(1);
    }
    
    /**
     * Retrieve the entity involved in this event.
     * @param world - the current world of the entity.
     * @return The involved entity.
     */
    public Entity getEntity(World world) {
        return handle.getEntityModifier(world).read(1);
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
     * Set Entity ID.
     * @param value - new value.
     */
    public void setEntityID(int value) {
        handle.getIntegers().write(1, value);
    }
    
}
