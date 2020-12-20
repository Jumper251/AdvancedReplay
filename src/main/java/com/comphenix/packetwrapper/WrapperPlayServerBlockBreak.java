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
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerDigType;
import com.comphenix.protocol.wrappers.WrappedBlockData;

public class WrapperPlayServerBlockBreak extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Server.BLOCK_BREAK;
    
    public WrapperPlayServerBlockBreak() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerBlockBreak(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve Location.
     * <p>
     * Notes: position where the digging was happening
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
     * Retrieve Block.
     * <p>
     * Notes: block state ID of the block that should be at that position now.
     * @return The current Block
     */
    public WrappedBlockData getBlock() {
        return handle.getBlockData().read(0);
    }
    
    /**
     * Set Block.
     * @param value - new value.
     */
    public void setBlock(WrappedBlockData value) {
        handle.getBlockData().write(0, value);
    }
    
    /**
     * Retrieve Status.
     * <p>
     * Notes: same as Player Digging. Only Started digging (0), Cancelled digging (1), and Finished digging (2) are used.
     * @return The current Status
     */
    public PlayerDigType getStatus() {
        return handle.getPlayerDigTypes().read(0);
    }
    
    /**
     * Set Status.
     * @param value - new value.
     */
    public void setStatus(PlayerDigType value) {
        handle.getPlayerDigTypes().write(0, value);
    }
    
    /**
     * Retrieve Successful.
     * <p>
     * Notes: true if the digging succeeded; false if the client should undo any changes it made locally. (How does this work?)
     * @return The current Successful
     */
    public boolean getSuccessful() {
        return handle.getBooleans().read(0);
    }
    
    /**
     * Set Successful.
     * @param value - new value.
     */
    public void setSuccessful(boolean value) {
        handle.getBooleans().write(0, value);
    }
    
}
