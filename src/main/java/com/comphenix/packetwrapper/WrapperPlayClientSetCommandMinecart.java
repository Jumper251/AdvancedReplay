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

public class WrapperPlayClientSetCommandMinecart extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Client.SET_COMMAND_MINECART;
    
    public WrapperPlayClientSetCommandMinecart() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientSetCommandMinecart(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve Entity ID.
     * @return The current Entity ID
     */
    public int getEntityID() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Retrieve the entity involved in this event.
     * @param world - the current world of the entity.
     * @return The involved entity.
     */
    public Entity getEntity(World world) {
        return handle.getEntityModifier(world).read(0);
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
        handle.getIntegers().write(0, value);
    }
    
    /**
     * Retrieve Command.
     * @return The current Command
     */
    public String getCommand() {
        return handle.getStrings().read(0);
    }
    
    /**
     * Set Command.
     * @param value - new value.
     */
    public void setCommand(String value) {
        handle.getStrings().write(0, value);
    }
    
    /**
     * Retrieve Track Output.
     * <p>
     * Notes: if false, the output of the previous command will not be stored within the command block.
     * @return The current Track Output
     */
    public boolean getTrackOutput() {
        return handle.getBooleans().read(0);
    }
    
    /**
     * Set Track Output.
     * @param value - new value.
     */
    public void setTrackOutput(boolean value) {
        handle.getBooleans().write(0, value);
    }
    
}
