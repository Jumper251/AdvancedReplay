package me.jumper251.replay.replaysystem.data.types;

public class MetadataUpdate extends PacketData {

    /**
     *
     */
    private static final long serialVersionUID = -8969498588009941633L;

    private boolean burning, blocking, gliding;

    public MetadataUpdate(boolean burning, boolean blocking) {
        this.burning = burning;
        this.blocking = blocking;
    }

    public MetadataUpdate(boolean burning, boolean blocking, boolean gliding) {
        this(burning, blocking);

        this.gliding = gliding;
    }

    public boolean isBurning() {
        return burning;
    }

    public boolean isBlocking() {
        return blocking;
    }

    public boolean isGliding() {
        return gliding;
    }


}
