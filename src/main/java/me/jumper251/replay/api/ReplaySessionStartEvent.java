package me.jumper251.replay.api;


import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.replaying.Replayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReplaySessionStartEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;
    private boolean handleButtons = false;

    private Replayer replayer;

    private Player player;

    public ReplaySessionStartEvent(Replayer replayer, Player player) {
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

    public boolean isHandlingButtons(){
        return handleButtons;
    }

    public void willHandleButtons(boolean b){
        this.handleButtons = b;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
