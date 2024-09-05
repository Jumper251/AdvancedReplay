package me.jumper251.replay.utils.fetcher;

import com.google.gson.annotations.SerializedName;

import java.net.MalformedURLException;
import java.net.URL;

public class TextureInfo {
    private Textures textures;


    public Textures getTextures() {
        return textures;
    }

    public static class Skin {
        private String url;

        public URL getUrl() {
            try {
                return new URL(url);
            } catch (MalformedURLException e) {
                return null;
            }
        }
    }

    public static class Textures {
        @SerializedName("SKIN")
        private Skin skin;


        public Skin getSkin() {
            return skin;
        }
    }
}
