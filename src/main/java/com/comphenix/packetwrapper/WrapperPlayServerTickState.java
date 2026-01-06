package com.comphenix.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerTickState extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.TICKING_STATE;

    public WrapperPlayServerTickState() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerTickState(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieve Tick Rate.
     *
     * @return The current Tick Rate
     */
    public float getTickRate() {
        return handle.getFloat().read(0);
    }

    /**
     * Set Tick Rate.
     *
     * @param value - new value.
     */
    public void setTickRate(float value) {
        handle.getFloat().write(0, value);
    }

    /**
     * Retrieve if the ticking state is frozen.
     *
     * @return The current frozen state
     */
    public boolean isFrozen() {
        return handle.getBooleans().read(0);
    }


    /**
     * Set if the ticking state is frozen.
     *
     * @param value - new value.
     */
    public void setFrozen(boolean value) {
        handle.getBooleans().write(0, value);
    }

}