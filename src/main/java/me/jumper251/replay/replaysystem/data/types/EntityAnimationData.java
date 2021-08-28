package me.jumper251.replay.replaysystem.data.types;

public class EntityAnimationData extends PacketData {


    /**
     *
     */
    private static final long serialVersionUID = 3334893591520224930L;

    private int entId, id;

    public EntityAnimationData(int entId, int id) {
        this.id = id;
        this.entId = entId;
    }

    public int getId() {
        return id;
    }

    public int getEntId() {
        return entId;
    }
}
