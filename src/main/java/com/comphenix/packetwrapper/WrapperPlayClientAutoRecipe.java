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

public class WrapperPlayClientAutoRecipe extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Client.AUTO_RECIPE;
    
    public WrapperPlayClientAutoRecipe() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientAutoRecipe(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve Window ID.
     * <p>
     * Notes: the window id.
     * @return The current Window ID
     */
    public int getWindowId() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set Window ID.
     * @param value - new value.
     */
    public void setWindowId(int value) {
        handle.getIntegers().write(0, value);
    }

    // Modifier for recipe can be created upon request

    public boolean isMakeAll() {
        return handle.getBooleans().read(0);
    }

    public void setMakeAll(boolean value) {
        handle.getBooleans().write(0, value);
    }
}
