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
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.MinecraftKey;

public class WrapperPlayClientSetJigsaw extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Client.SET_JIGSAW;
    
    public WrapperPlayClientSetJigsaw() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientSetJigsaw(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve Location.
     * <p>
     * Notes: block entity location
     * @return The current Location
     */
    public BlockPosition getLocation() {
        return handle.getBlockPositionModifier().read(0);
    }
    
    /**
     * Set Location.
     * @param value - new value.
     */
    public void setLocation(BlockPosition value) {
        handle.getBlockPositionModifier().write(0, value);
    }
    
    /**
     * Retrieve Attachment type.
     * @return The current Attachment type
     */
    public MinecraftKey getAttachmentType() {
        return handle.getMinecraftKeys().read(0);
    }
    
    /**
     * Set Attachment type.
     * @param value - new value.
     */
    public void setAttachmentType(MinecraftKey value) {
        handle.getMinecraftKeys().write(0, value);
    }
    
    /**
     * Retrieve Target pool.
     * @return The current Target pool
     */
    public MinecraftKey getTargetPool() {
        return handle.getMinecraftKeys().read(1);
    }
    
    /**
     * Set Target pool.
     * @param value - new value.
     */
    public void setTargetPool(MinecraftKey value) {
        handle.getMinecraftKeys().write(1, value);
    }
    
    /**
     * Retrieve Final state.
     * <p>
     * Notes: "Turns into" on the GUI, final_state in NBT
     * @return The current Final state
     */
    public String getFinalState() {
        return handle.getStrings().read(0);
    }
    
    /**
     * Set Final state.
     * @param value - new value.
     */
    public void setFinalState(String value) {
        handle.getStrings().write(0, value);
    }
    
}
