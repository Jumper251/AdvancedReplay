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
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.SoundCategory;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperPlayServerEntitySound extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_SOUND;
    
    public WrapperPlayServerEntitySound() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerEntitySound(PacketContainer packet) {
        super(packet, TYPE);
    }
    
    /**
     * Retrieve Sound ID.
     * <p>
     * Notes: iD of hardcoded sound event (events as of 1.15.2)
     * @return The current Sound ID
     */
    public Sound getSound() {
        return handle.getSoundEffects().read(0);
    }
    
    /**
     * Set Sound ID.
     * @param value - new value.
     */
    public void setSound(Sound value) {
        handle.getSoundEffects().write(0, value);
    }
    
    /**
     * Retrieve Sound Category.
     * <p>
     * Notes: the category that this sound will be played from (current categories)
     * @return The current Sound Category
     */
    public SoundCategory getSoundCategory() {
        return handle.getSoundCategories().read(0);
    }
    
    /**
     * Set Sound Category.
     * @param value - new value.
     */
    public void setSoundCategory(SoundCategory value) {
        handle.getSoundCategories().write(0, value);
    }
    
    /**
     * Retrieve Entity ID.
     * @return The current Entity ID
     */
    public int getEntityID() {
        return handle.getIntegers().read(0);
    }
    
    /**
     * Retrieve the entity involved in this event.
     * @param world - the current world of the entity.
     * @return The involved entity.
     */
    public Entity getEntity(World world) {
        return handle.getEntityModifier(world).read(2);
    }
    
    /**
     * Retrieve the entity involved in this event.
     * @param event - the packet event.
     * @return The involved entity.
     */
    public Entity getEntity(PacketEvent event) {
        return getEntity(event.getPlayer().getWorld());
    }
    
    /**
     * Set Entity ID.
     * @param value - new value.
     */
    public void setEntityID(int value) {
        handle.getIntegers().write(0, value);
    }
    
    /**
     * Retrieve Volume.
     * <p>
     * Notes: 1.0 is 100%, capped between 0.0 and 1.0 by Notchian clients
     * @return The current Volume
     */
    public float getVolume() {
        return handle.getFloat().read(0);
    }
    
    /**
     * Set Volume.
     * @param value - new value.
     */
    public void setVolume(float value) {
        handle.getFloat().write(0, value);
    }
    
    /**
     * Retrieve Pitch.
     * <p>
     * Notes: float between 0.5 and 2.0 by Notchian clients
     * @return The current Pitch
     */
    public float getPitch() {
        return handle.getFloat().read(1);
    }
    
    /**
     * Set Pitch.
     * @param value - new value.
     */
    public void setPitch(float value) {
        handle.getFloat().write(1, value);
    }
    
}
