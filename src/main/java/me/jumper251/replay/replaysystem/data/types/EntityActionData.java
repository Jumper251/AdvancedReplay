package me.jumper251.replay.replaysystem.data.types;

import com.comphenix.protocol.wrappers.EnumWrappers.PlayerAction;

public class EntityActionData extends PacketData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7841723539864388570L;

	private PlayerAction action;

	public EntityActionData (PlayerAction action) {
		this.action = action;
	}

	public PlayerAction getAction () {
		return action;
	}
}
