package me.jumper251.replay.api;

import java.util.ArrayList;
import java.util.List;

public class HookManager {

    private List<IReplayHook> hooks = new ArrayList<IReplayHook>();


    public void registerHook(IReplayHook hook) {
        if (!this.hooks.contains(hook)) {
            this.hooks.add(hook);
        }
    }

    public void unregisterHook(IReplayHook hook) {
        if (this.hooks.contains(hook)) {
            this.hooks.remove(hook);
        }
    }

    public boolean isRegistered() {
        return this.hooks.size() > 0;
    }

    public List<IReplayHook> getHooks() {
        return hooks;
    }
}
