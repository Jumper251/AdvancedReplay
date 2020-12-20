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
import com.comphenix.protocol.wrappers.MinecraftKey;

public class WrapperPlayClientAdvancements extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Client.ADVANCEMENTS;
    
    public WrapperPlayClientAdvancements() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientAdvancements(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve Action.
     * <p>
     * Notes: 0: Opened tab, 1: Closed screen
     * @return The current Action
     */
    public Status getAction() {
        return handle.getEnumModifier(Status.class, 0).readSafely(0);
    }
    
    /**
     * Set Action.
     * @param value - new value.
     */
    public void setAction(Status value) {
        handle.getEnumModifier(Status.class, 0).writeSafely(0, value);
    }
    /**
     * Retrieve Tab ID.
     * <p>
     * Notes: only present if action is Opened tab
     * @return The current Tab ID
     */
    public MinecraftKey getTabId() {
        return handle.getMinecraftKeys().readSafely(0);
    }
    
    /**
     * Set Tab ID.
     * @param value - new value.
     */
    public void setTabId(MinecraftKey value) {
        handle.getMinecraftKeys().writeSafely(0, value);
    }

    public enum Status {
        OPENED_TAB,
        CLOSED_SCREEN;
    }
}
