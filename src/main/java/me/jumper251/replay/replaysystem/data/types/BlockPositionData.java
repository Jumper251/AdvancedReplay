package me.jumper251.replay.replaysystem.data.types;

import com.comphenix.protocol.wrappers.BlockPosition;

import java.io.Serializable;

public class BlockPositionData implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int x;
    private final int y;
    private final int z;

    public BlockPositionData(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public static BlockPositionData fromBlockPosition(BlockPosition blockPosition) {
        return new BlockPositionData(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    }

    public BlockPosition toBlockPosition() {
        return new BlockPosition(x, y, z);
    }

    @Override
    public String toString() {
        return "BlockPositionData{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}