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

public class WrapperPlayServerOpenWindowHorse extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Server.OPEN_WINDOW_HORSE;
    
    public WrapperPlayServerOpenWindowHorse() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerOpenWindowHorse(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve Window ID?.
     * @return The current Window ID?
     */
    public int getWindowId() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set Window ID?.
     * @param value - new value.
     */
    public void setWindowId(int value) {
        handle.getIntegers().write(0, value);
    }
    
    /**
     * Retrieve Number of slots?.
     * @return The current Number of slots?
     */
    public int getNumberOfSlots() {
        return handle.getIntegers().read(1);
    }
    
    /**
     * Set Number of slots?.
     * @param value - new value.
     */
    public void setNumberOfSlots(int value) {
        handle.getIntegers().write(1, value);
    }
    
    /**
     * Retrieve Entity ID?.
     * @return The current Entity ID?
     */
    public int getEntityID() {
        return handle.getIntegers().read(2);
    }
    
    /**
     * Retrieve the entity involved in this event.
     * @param world - the current world of the entity.
     * @return The involved entity.
     */
    public Entity getEntity(World world) {
        return handle.getEntityModifier(world).read(2);
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
     * Set Entity ID?.
     * @param value - new value.
     */
    public void setEntityID(int value) {
        handle.getIntegers().write(2, value);
    }
    
}
