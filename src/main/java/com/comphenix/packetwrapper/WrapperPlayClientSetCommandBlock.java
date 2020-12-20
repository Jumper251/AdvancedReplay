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
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.BlockPosition;

public class WrapperPlayClientSetCommandBlock extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Client.SET_COMMAND_BLOCK;
    
    public WrapperPlayClientSetCommandBlock() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientSetCommandBlock(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieve Location.
     * @return The current Location
     */
    public BlockPosition getLocation() {
        return handle.getBlockPositionModifier().readSafely(0);
    }
    
    /**
     * Set Location.
     * @param value - new value.
     */
    public void setLocation(BlockPosition value) {
        handle.getBlockPositionModifier().writeSafely(0,  value);
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
	 * if false, the output of the previous command will not be stored within the command block
	 */
	public boolean isTrackOutput() {
    	return handle.getBooleans().read(0);
    }

    public void setTrackOutput(boolean value) {
		handle.getBooleans().write(0, value);
    }

    public boolean isConditional() {
	    return handle.getBooleans().read(1);
    }

    public void setConditional(boolean value) {
	    handle.getBooleans().write(1, value);
    }

    public boolean isAutomatic() {
	    return handle.getBooleans().read(2);
    }

    public void setAutomatic(boolean value) {
	    handle.getBooleans().write(2, value);
    }

    public Mode getMode() {
		return handle.getEnumModifier(Mode.class, MinecraftReflection.getMinecraftClass("TileEntityCommand$Type")).readSafely(0);
    }

    public void setMode(Mode mode) {
	    handle.getEnumModifier(Mode.class, MinecraftReflection.getMinecraftClass("TileEntityCommand$Type")).writeSafely(0, mode);
    }

    public enum Mode {
        SEQUENCE,
        AUTO,
        REDSTONE
    }
}
