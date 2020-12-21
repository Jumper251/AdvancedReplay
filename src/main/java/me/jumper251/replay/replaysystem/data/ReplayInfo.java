package me.jumper251.replay.replaysystem.data;

public class ReplayInfo {

	private String id;

	private String creator;

	private Long time;

	private int duration;

	public ReplayInfo (String id, String creator, Long time, int duration) {
		this.id       = id;
		this.creator  = creator;
		this.time     = time;
		this.duration = duration;
	}

	public int getDuration () {
		return duration;
	}

	public String getCreator () {
		return creator;
	}

	public String getID () {
		return id;
	}

	public Long getTime () {
		return time;
	}
}
