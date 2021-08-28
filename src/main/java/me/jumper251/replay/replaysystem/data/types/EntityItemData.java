package me.jumper251.replay.replaysystem.data.types;

public class EntityItemData extends PacketData {

    /**
     *
     */
    private static final long serialVersionUID = 2309497657075148314L;


    private int action, id;

    private ItemData itemData;

    private LocationData location, velocity;

    public EntityItemData(int action, int id, ItemData itemData, LocationData location, LocationData velocity) {
        this.action = action;
        this.itemData = itemData;
        this.location = location;
        this.id = id;
        this.velocity = velocity;
    }


    public int getAction() {
        return action;
    }

    public ItemData getItemData() {
        return itemData;
    }

    public LocationData getLocation() {
        return location;
    }

    public int getId() {
        return id;
    }

    public LocationData getVelocity() {
        return velocity;
    }

}
