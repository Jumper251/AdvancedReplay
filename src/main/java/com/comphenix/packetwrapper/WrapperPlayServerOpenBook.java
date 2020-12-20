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
import com.comphenix.protocol.wrappers.EnumWrappers.Hand;

public class WrapperPlayServerOpenBook extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Server.OPEN_BOOK;
    
    public WrapperPlayServerOpenBook() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerOpenBook(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve Hand.
     * <p>
     * Notes: 0: Main hand, 1: Off hand
     * @return The current Hand
     */
    public Hand getHand() {
        return handle.getHands().read(0);
    }
    
    /**
     * Set Hand.
     * @param value - new value.
     */
    public void setHand(Hand value) {
        handle.getHands().write(0, value);
    }
    
}
