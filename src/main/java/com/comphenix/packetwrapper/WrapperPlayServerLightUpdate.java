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

import java.util.List;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;

public class WrapperPlayServerLightUpdate extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Server.LIGHT_UPDATE;
    
    public WrapperPlayServerLightUpdate() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerLightUpdate(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve Chunk X.
     * <p>
     * Notes: chunk coordinate (block coordinate divided by 16, rounded down)
     * @return The current Chunk X
     */
    public int getChunkX() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set Chunk X.
     * @param value - new value.
     */
    public void setChunkX(int value) {
        handle.getIntegers().write(0, value);
    }
    
    /**
     * Retrieve Chunk Z.
     * <p>
     * Notes: chunk coordinate (block coordinate divided by 16, rounded down)
     * @return The current Chunk Z
     */
    public int getChunkZ() {
        return handle.getIntegers().read(1);
    }
    
    /**
     * Set Chunk Z.
     * @param value - new value.
     */
    public void setChunkZ(int value) {
        handle.getIntegers().write(1, value);
    }
    
    /**
     * Retrieve Sky Light Mask.
     * <p>
     * Notes: mask containing 18 bits, with the lowest bit corresponding to chunk section -1 (in the void, y=-16 to y=-1) and the highest bit for chunk section 16 (above the world, y=256 to y=271)
     * @return The current Sky Light Mask
     */
    public int getSkyLightMask() {
        return handle.getIntegers().read(2);
    }
    
    /**
     * Set Sky Light Mask.
     * @param value - new value.
     */
    public void setSkyLightMask(int value) {
        handle.getIntegers().write(2, value);
    }
    
    /**
     * Retrieve Block Light Mask.
     * <p>
     * Notes: mask containing 18 bits, with the same order as sky light
     * @return The current Block Light Mask
     */
    public int getBlockLightMask() {
        return handle.getIntegers().read(3);
    }
    
    /**
     * Set Block Light Mask.
     * @param value - new value.
     */
    public void setBlockLightMask(int value) {
        handle.getIntegers().write(3, value);
    }
    
    /**
     * Retrieve Empty Sky Light Mask.
     * <p>
     * Notes: mask containing 18 bits, which indicates sections that have 0 for all their sky light values. If a section is set in both this mask and the main sky light mask, it is ignored for this mask and it is included in the sky light arrays (the notchian server does not create such masks). If it is only set in this mask, it is not included in the sky light arrays.
     * @return The current Empty Sky Light Mask
     */
    public int getEmptySkyLightMask() {
        return handle.getIntegers().read(4);
    }
    
    /**
     * Set Empty Sky Light Mask.
     * @param value - new value.
     */
    public void setEmptySkyLightMask(int value) {
        handle.getIntegers().write(4, value);
    }
    
    /**
     * Retrieve Empty Block Light Mask.
     * <p>
     * Notes: mask containing 18 bits, which indicates sections that have 0 for all their block light values. If a section is set in both this mask and the main block light mask, it is ignored for this mask and it is included in the block light arrays (the notchian server does not create such masks). If it is only set in this mask, it is not included in the block light arrays.
     * @return The current Empty Block Light Mask
     */
    public int getEmptyBlockLightMask() {
        return handle.getIntegers().read(5);
    }
    
    /**
     * Set Empty Block Light Mask.
     * @param value - new value.
     */
    public void setEmptyBlockLightMask(int value) {
        handle.getIntegers().write(5, value);
    }
    
    /**
     * Retrieve Sky Light arrays.
     * <p>
     * Notes: array
     * @return The current Sky Light arrays
     */
    public List<BlockPosition> getSkyLightArrays() {
        return handle.getBlockPositionCollectionModifier().read(0);
    }
    
    /**
     * Set Sky Light arrays.
     * @param value - new value.
     */
    public void setSkyLightArrays(List<BlockPosition> value) {
        handle.getBlockPositionCollectionModifier().write(0, value);
    }
    
    /**
     * Retrieve Sky Light array.
     * <p>
     * Notes: there is 1 array for each bit set to true in the sky light mask, starting with the lowest value. Half a byte per light value.
     * @return The current Sky Light array
     */
    public List<BlockPosition> getSkyLightArray() {
        return handle.getBlockPositionCollectionModifier().read(1);
    }
    
    /**
     * Set Sky Light array.
     * @param value - new value.
     */
    public void setSkyLightArray(List<BlockPosition> value) {
        handle.getBlockPositionCollectionModifier().write(1, value);
    }
    
    // Cannot generate field Block Light arrays
    // Cannot generate field Block Light array
}
