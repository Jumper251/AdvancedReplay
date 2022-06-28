package me.jumper251.replay.dev.mrflyn.extended.worldmanagers;

public class RawWorld {

    public byte[] data;
    public String hashcode;
    public String name;
    public String type;

    public RawWorld(String name, String hashcode, byte[] data, String type){
        this.data = data;
        this.hashcode = hashcode;
        this.name = name;
        this.type = type;
    }


}
