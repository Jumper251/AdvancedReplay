package me.jumper251.replay.utils.fetcher;

import com.google.gson.annotations.SerializedName;

public class PlayerInfo extends JsonClass {
	@SerializedName ("id")
	private String id;

	@SerializedName ("name")
	private String name;

	@SerializedName ("legacy")
	private boolean legacy;

	public String getId () {
		return id;
	}

	public String getName () {
		return name;
	}
	public boolean getLegacy () {
		return this.legacy;
	}
}
