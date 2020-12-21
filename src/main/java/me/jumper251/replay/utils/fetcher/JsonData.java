package me.jumper251.replay.utils.fetcher;

import com.google.gson.Gson;

public class JsonData {

	private boolean enabled;
	private JsonClass jsonClass;
	private Gson gson = new Gson ();
	private String data;
	public JsonData (boolean enabled) {
		this.enabled = enabled;
	}
	public JsonData (boolean enabled, JsonClass jsonClass) {
		this.enabled   = enabled;
		this.jsonClass = jsonClass;
	}
	public void setData (String data) {
		this.data = data;
	}
	public void convertData () {
		this.jsonClass = gson.fromJson (data, this.jsonClass.getClass ());
	}
	public JsonClass getJsonClass () {
		return jsonClass;
	}

	public boolean isEnabled () {
		return this.enabled;
	}
}
