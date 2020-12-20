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
import com.comphenix.protocol.wrappers.nbt.NbtBase;

public class WrapperPlayServerNbtQuery extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Server.NBT_QUERY;
    
    public WrapperPlayServerNbtQuery() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerNbtQuery(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve Transaction ID.
     * <p>
     * Notes: can be compared to the one sent in the original query packet.
     * @return The current Transaction ID
     */
    public int getTransactionId() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Set Transaction ID.
     * @param value - new value.
     */
    public void setTransactionId(int value) {
        handle.getIntegers().write(0, value);
    }
    
    /**
     * Retrieve NBT.
     * <p>
     * Notes: the NBT of the block or entity. May be a TAG_END (0) in which case no NBT is present.
     * @return The current NBT
     */
    public NbtBase<?> getNbt() {
        return handle.getNbtModifier().read(0);
    }
    
    /**
     * Set NBT.
     * @param value - new value.
     */
    public void setNbt(NbtBase<?> value) {
        handle.getNbtModifier().write(0, value);
    }
    
}
