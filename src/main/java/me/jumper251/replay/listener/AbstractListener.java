package me.jumper251.replay.listener;







import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import me.jumper251.replay.ReplaySystem;

public abstract class AbstractListener implements Listener{
	
	protected ReplaySystem plugin;
	
	public AbstractListener(){
		this.plugin = ReplaySystem.instance;
	}
	
	public void register(){
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
	}
	
	public void unregister(){
		HandlerList.unregisterAll(this);
	}
	

}
