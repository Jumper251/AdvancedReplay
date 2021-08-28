package me.jumper251.replay.utils.fetcher;


import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class SkinInfo extends JsonClass {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("properties")
    private List<Map<String, String>> properties;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Map<String, String>> getProperties() {
        return properties;
    }

}
