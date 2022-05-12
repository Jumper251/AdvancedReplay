package me.jumper251.replay.api;

import me.jumper251.replay.replaysystem.replaying.Replayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReplaySessionFinishEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private Replayer replayer;
    
    private Player player;
    
    public ReplaySessionFinishEvent(Replayer replayer, Player player) {
    	super(!Bukkit.isPrimaryThread());
    	
    	this.replayer = replayer;
    	this.player = player;
    }
    
	
    public Player getPlayer() {
		return player;
	}
    
    public Replayer getReplayer() {
		return replayer;
	}
    
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
	    return HANDLERS;
	}

}
