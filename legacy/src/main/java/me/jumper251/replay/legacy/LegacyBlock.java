package me.jumper251.replay.legacy;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class LegacyBlock {

    public static void setTypeIdAndData(Block block, int typeId, byte data, boolean applyPhysics) {
        block.setTypeIdAndData(typeId, data, applyPhysics);
    }

    public static void sendBlockChange(Player player, Location loc, int typeId, byte data) {
        player.sendBlockChange(loc, typeId, data);
    }
}
