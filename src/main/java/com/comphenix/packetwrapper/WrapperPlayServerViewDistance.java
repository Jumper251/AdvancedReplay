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

public class WrapperPlayServerViewDistance extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Server.VIEW_DISTANCE;
    
    public WrapperPlayServerViewDistance() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerViewDistance(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve View Distance.
     * <p>
     * Notes: render distance (2-32)
     * @return The current View Distance
     */
    public int getViewDistance() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set View Distance.
     * @param value - new value.
     */
    public void setViewDistance(int value) {
        handle.getIntegers().write(0, value);
    }
    
}
