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

import net.minecraft.server.v1_15_R1.BlockPropertyStructureMode;
import net.minecraft.server.v1_15_R1.EnumBlockMirror;
import net.minecraft.server.v1_15_R1.TileEntityStructure;

public class WrapperPlayClientStruct extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Client.STRUCT;
    
    public WrapperPlayClientStruct() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientStruct(PacketContainer packet) {
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

    public enum UpdateType {
        UPDATE_DATA,
        SAVE_AREA,
        LOAD_AREA,
        SCAN_AREA;
    }
    
    /**
     * Retrieve Action.
     * <p>
     * Notes: an additional action to perform beyond simply saving the given data; see below
     * @return The current Action
     */
    public UpdateType getAction() {
        return handle.getEnumModifier(UpdateType.class, 1).read(0);
    }
    
    /**
     * Set Action.
     * @param value - new value.
     */
    public void setAction(UpdateType value) {
        handle.getEnumModifier(UpdateType.class, 1).write(0, value);
    }

    public enum BlockPropertyStructureMode {
        SAVE,
        LOAD,
        CORNER,
        DATA;
    }

    /**
     * Retrieve Mode.
     * <p>
     * Notes: one of SAVE (0), LOAD (1), CORNER (2), DATA (3).
     * @return The current Mode
     */
    public BlockPropertyStructureMode getMode() {
        return handle.getEnumModifier(BlockPropertyStructureMode.class, 2).read(0);
    }
    
    /**
     * Set Mode.
     * @param value - new value.
     */
    public void setMode(BlockPropertyStructureMode value) {
        handle.getEnumModifier(BlockPropertyStructureMode.class, 2).write(0, value);
    }
    
    /**
     * Retrieve Name.
     * @return The current Name
     */
    public String getName() {
        return handle.getStrings().read(0);
    }
    
    /**
     * Set Name.
     * @param value - new value.
     */
    public void setName(String value) {
        handle.getStrings().write(0, value);
    }
    
    /**
     * Retrieve Offset X, Y, and Z
     * <p>
     * Notes: between -32 and 32
     * @return The current Offset X, Y, Z
     */
    public BlockPosition getOffsets() {
        return handle.getBlockPositionModifier().read(1);
    }
    
    /**
     * Set Offset X, Y, and Z
     * @param value - new value.
     */
    public void setOffsets(BlockPosition value) {
        handle.getBlockPositionModifier().write(1, value);
    }
    
    /**
     * Retrieve Size X, Y, and Z
     * <p>
     * Notes: between -32 and 32
     * @return The current Size X, Y, and Z
     */
    public BlockPosition getSizes() {
        return handle.getBlockPositionModifier().read(2);
    }
    
    /**
     * Set Size X, Y, and Z
     * @param value - new value.
     */
    public void setSizes(BlockPosition value) {
        handle.getBlockPositionModifier().write(2, value);
    }

    public enum BlockMirror {
        NONE,
        LEFT_RIGHT,
        FRONT_BACK;
    }
    
    /**
     * Retrieve Mirror.
     * <p>
     * Notes: one of NONE (0), LEFT_RIGHT (1), FRONT_BACK (2).
     * @return The current Mirror
     */
    public BlockMirror getMirror() {
        return handle.getEnumModifier(BlockMirror.class, 6).read(0);
    }
    
    /**
     * Set Mirror.
     * @param value - new value.
     */
    public void setMirror(BlockMirror value) {
        handle.getEnumModifier(BlockMirror.class, 6).write(0, value);
    }

    public enum BlockRotation {
        NONE,
        CLOCKWISE_90,
        CLOCKWISE_180,
        COUNTERCLOCKWISE_90
    }
    
    /**
     * Retrieve Rotation.
     * <p>
     * Notes: one of NONE (0), CLOCKWISE_90 (1), CLOCKWISE_180 (2), COUNTERCLOCKWISE_90 (3).
     * @return The current Rotation
     */
    public BlockRotation getRotation() {
        return handle.getEnumModifier(BlockRotation.class, 7).read(0);
    }
    
    /**
     * Set Rotation.
     * @param value - new value.
     */
    public void setRotation(BlockRotation value) {
        handle.getEnumModifier(BlockRotation.class, 7).write(1, value);
    }
    
    /**
     * Retrieve Metadata.
     * @return The current Metadata
     */
    public String getMetadata() {
        return handle.getStrings().read(0);
    }
    
    /**
     * Set Metadata.
     * @param value - new value.
     */
    public void setMetadata(String value) {
        handle.getStrings().write(0, value);
    }
    
    /**
     * Retrieve Integrity.
     * <p>
     * Notes: between 0 and 1
     * @return The current Integrity
     */
    public float getIntegrity() {
        return handle.getFloat().read(0);
    }
    
    /**
     * Set Integrity.
     * @param value - new value.
     */
    public void setIntegrity(float value) {
        handle.getFloat().write(0, value);
    }
    
    /**
     * Retrieve Seed.
     * @return The current Seed
     */
    public long getSeed() {
        return handle.getLongs().read(0);
    }
    
    /**
     * Set Seed.
     * @param value - new value.
     */
    public void setSeed(long value) {
        handle.getLongs().write(0, value);
    }
    
    public boolean getIgnoreEntities() {
        return handle.getBooleans().read(0);
    }

    public void setIgnoreEntities(boolean value) {
        handle.getBooleans().write(0, value);
    }

    public boolean getShowAir() {
        return handle.getBooleans().read(1);
    }

    public void setShowAir(boolean value) {
        handle.getBooleans().write(1, value);
    }

    public boolean getShowBoundingBox() {
        return handle.getBooleans().read(2);
    }

    public void setShowBoundingBox(boolean value) {
        handle.getBooleans().write(2, value);
    }
}
