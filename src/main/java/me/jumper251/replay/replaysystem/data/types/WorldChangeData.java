package me.jumper251.replay.replaysystem.data.types;

public class WorldChangeData extends PacketData {

    /**
     *
     */
    private static final long serialVersionUID = -7360847147915116994L;

    private LocationData location;

    public WorldChangeData(LocationData location) {
        this.location = location;
    }

    public LocationData getLocation() {
        return location;
    }

}
