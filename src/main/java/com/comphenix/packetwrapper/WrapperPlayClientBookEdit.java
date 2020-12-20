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

import org.bukkit.inventory.ItemStack;

public class WrapperPlayClientBookEdit extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Client.B_EDIT;
    
    public WrapperPlayClientBookEdit() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientBookEdit(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve New book.
     * @return The current New book
     */
    public ItemStack getNewBook() {
        return handle.getItemModifier().read(0);
    }
    
    /**
     * Set New book.
     * @param value - new value.
     */
    public void setNewBook(ItemStack value) {
        handle.getItemModifier().write(0, value);
    }
    
    /**
     * Retrieve Is signing.
     * <p>
     * Notes: true if the player is signing the book; false if the player is saving a draft.
     * @return The current Is signing
     */
    public boolean getIsSigning() {
        return handle.getBooleans().read(0);
    }
    
    /**
     * Set Is signing.
     * @param value - new value.
     */
    public void setIsSigning(boolean value) {
        handle.getBooleans().write(0, value);
    }
}
