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

public class WrapperPlayServerRecipes extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Server.RECIPES;
    
    public WrapperPlayServerRecipes() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerRecipes(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieve Action.
     * <p>
     * Notes: 0: init, 1: add, 2: remove
     * @return The current Action
     */
    public Action getAction() {
        return handle.getEnumModifier(Action.class, 0).readSafely(0);
    }

    /**
     * Set Action.
     * @param value - new value.
     */
    public void setAction(Action value) {
        handle.getEnumModifier(Action.class, 0).writeSafely(0, value);
    }
    
    /**
     * Retrieve Crafting Book Open.
     * <p>
     * Notes: if true, then the crafting book will be open when the player opens its inventory.
     * @return The current Crafting Book Open
     */
    public boolean getCraftingBookOpen() {
        return handle.getBooleans().read(0);
    }
    
    /**
     * Set Crafting Book Open.
     * @param value - new value.
     */
    public void setCraftingBookOpen(boolean value) {
        handle.getBooleans().write(0, value);
    }
    
    /**
     * Retrieve Filtering Craftable.
     * <p>
     * Notes: if true, then the filtering option is active when the players opens its inventory.
     * @return The current Filtering Craftable
     */
    public boolean getFilteringCraftable() {
        return handle.getBooleans().read(0);
    }
    
    /**
     * Set Filtering Craftable.
     * @param value - new value.
     */
    public void setFilteringCraftable(boolean value) {
        handle.getBooleans().write(0, value);
    }

    // recipe ids

    public enum Action {
        INIT,
        ADD,
        REMOVE;
    }
}
