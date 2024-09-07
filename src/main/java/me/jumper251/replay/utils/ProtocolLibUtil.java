package me.jumper251.replay.utils;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.jumper251.replay.replaysystem.utils.entities.PacketNPC;

public class ProtocolLibUtil {

    /**
     * Initialize some ProtocolLib wrappers to avoid class loading issues
     * in the first replay after a server start.
     */
    public static void prepare() {
        PacketNPC npc = new PacketNPC();
        npc.setData(new WrappedDataWatcher());
        npc.getInfoAddPacket();
        npc.look(0, 0);
    }
}
