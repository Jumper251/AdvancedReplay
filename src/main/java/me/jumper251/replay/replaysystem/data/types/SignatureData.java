package me.jumper251.replay.replaysystem.data.types;

public class SignatureData extends PacketData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5019331850509482609L;

	private String name, value, signature;

	public SignatureData (String name, String value, String signature) {
		this.name      = name;
		this.signature = signature;
		this.value     = value;
	}

	public String getName () {
		return name;
	}

	public String getSignature () {
		return signature;
	}

	public String getValue () {
		return value;
	}
}
