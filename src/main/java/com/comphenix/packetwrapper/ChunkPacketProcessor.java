/**
 * PacketWrapper - ProtocolLib wrappers for Minecraft packets
 * Copyright (C) dmulloy2 <http://dmulloy2.net>
 * Copyright (C) Kristian S. Strangeland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.comphenix.packetwrapper;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;

/**
 * Used to process a chunk.
 * 
 * @author Kristian
 */
public class ChunkPacketProcessor {
	/**
	 * Contains the offset of the different block data in a chunk packet.
	 * 
	 * @author Kristian
	 */
	public static class ChunkOffsets {
		private int blockIdOffset;
		private int dataOffset;
		private int lightOffset;
		private int skylightOffset;
		private int extraOffset;

		private ChunkOffsets(int blockIdOffset, int dataOffset,
				int lightOffset, int skylightOffset, int extraOffset) {
			this.blockIdOffset = blockIdOffset;
			this.dataOffset = dataOffset;
			this.lightOffset = lightOffset;
			this.skylightOffset = skylightOffset;
			this.extraOffset = extraOffset;
		}

		private void incrementIdIndex() {
			blockIdOffset += ChunkPacketProcessor.BLOCK_ID_LENGTH;
			dataOffset += ChunkPacketProcessor.BYTES_PER_NIBBLE_PART;
			dataOffset += ChunkPacketProcessor.BYTES_PER_NIBBLE_PART;

			if (skylightOffset >= 0) {
				skylightOffset += ChunkPacketProcessor.BYTES_PER_NIBBLE_PART;
			}
		}

		private void incrementExtraIndex() {
			if (extraOffset >= 0) {
				extraOffset += ChunkPacketProcessor.BYTES_PER_NIBBLE_PART;
			}
		}

		/**
		 * Retrieve the starting index of the block ID data.
		 * <p>
		 * This will be 4096 bytes in length, one byte for each block in the
		 * 16x16x16 chunklet.
		 * 
		 * @return The starting location of the block ID data.
		 */
		public int getBlockIdOffset() {
			return blockIdOffset;
		}

		/**
		 * Retrieve the starting index of the meta data (4 bit per block).
		 * <p>
		 * This will be 2048 bytes in length, one nibblet for each block in the
		 * 16x16x16 chunklet.
		 * 
		 * @return The starting location of the block meta data.
		 */
		public int getDataOffset() {
			return dataOffset;
		}

		/**
		 * Retrieve the starting index of the torch light data (4 bit per
		 * block).
		 * <p>
		 * This will be 2048 bytes in length, one nibblet for each block in the
		 * 16x16x16 chunklet.
		 * 
		 * @return The starting location of the torch light data.
		 */
		public int getLightOffset() {
			return lightOffset;
		}

		/**
		 * Retrieve the starting index of the skylight data (4 bit per block).
		 * <p>
		 * This will be 2048 bytes in length if the skylight data exists (see
		 * {@link #hasSkylightOffset()}), no bytes if not.
		 * 
		 * @return The starting location of the skylight data.
		 */
		public int getSkylightOffset() {
			return skylightOffset;
		}

		/**
		 * Determine if the current chunklet contains skylight data.
		 * 
		 * @return TRUE if it does, FALSE otherwise.
		 */
		public boolean hasSkylightOffset() {
			return skylightOffset >= 0;
		}

		/**
		 * Retrieve the extra 4 bits in each block ID, if necessary.
		 * <p>
		 * This will be 2048 bytes in length if the extra data exists, no bytes
		 * if not.
		 * 
		 * @return The starting location of the extra data.
		 */
		public int getExtraOffset() {
			return extraOffset;
		}

		/**
		 * Determine if the current chunklet contains any extra block ID data.
		 * 
		 * @return TRUE if it does, FALSE otherwise.
		 */
		public boolean hasExtraOffset() {
			return extraOffset > 0;
		}
	}

	/**
	 * Process the content of a single 16x16x16 chunklet in a 16x256x16 chunk.
	 * 
	 * @author Kristian
	 */
	public interface ChunkletProcessor {
		/**
		 * Process a given chunklet (16x16x16).
		 * 
		 * @param origin - the block with the lowest x, y and z coordinate in
		 *        the chunklet.
		 * @param data - the data array.
		 * @param offsets - the offsets with the data for the given chunklet.
		 */
		public void processChunklet(Location origin, byte[] data,
				ChunkOffsets offsets);

		/**
		 * Process the biome array for a chunk (16x256x16).
		 * <p>
		 * This method will not be called if the chunk is missing biome
		 * information.
		 * 
		 * @param origin - the block with the lowest x, y and z coordinate in
		 *        the chunk.
		 * @param data - the data array.
		 * @param biomeIndex - the starting index of the biome data (256 bytes
		 *        in lenght).
		 */
		public void processBiomeArray(Location origin, byte[] data,
				int biomeIndex);
	}

	// Useful Minecraft constants
	protected static final int BYTES_PER_NIBBLE_PART = 2048;
	protected static final int CHUNK_SEGMENTS = 16;
	protected static final int NIBBLES_REQUIRED = 4;

	/**Misspelled.
	 * @see #BLOCK_ID_LENGTH
	 */
	@Deprecated
	public static final int BLOCK_ID_LENGHT = 4096;

	public static final int BLOCK_ID_LENGTH = 4096;

    /**Misspelled.
     * @see #DATA_LENGTH
     */
	@Deprecated
	public static final int DATA_LENGHT = 2048;

    public static final int DATA_LENGTH = 2048;
	public static final int BIOME_ARRAY_LENGTH = 256;

	private int chunkX;
	private int chunkZ;
	private int chunkMask;
	private int extraMask;
	private int chunkSectionNumber;
	private int extraSectionNumber;
	private boolean hasContinuous = true;

	private int startIndex;
	private int size;

	private byte[] data;
	private World world;

	private ChunkPacketProcessor() {
		// Use factory methods
	}

	/**
	 * Construct a chunk packet processor from a given MAP_CHUNK packet.
	 * 
	 * @param packet - the map chunk packet.
	 * @return The chunk packet processor.
	 */
	public static ChunkPacketProcessor fromMapPacket(PacketContainer packet,
			World world) {
		if (!packet.getType().equals(PacketType.Play.Server.MAP_CHUNK))
			throw new IllegalArgumentException(packet
					+ " must be a MAP_CHUNK packet.");

		StructureModifier<Integer> ints = packet.getIntegers();
		StructureModifier<byte[]> byteArray = packet.getByteArrays();

		// Create an info objects
		ChunkPacketProcessor processor = new ChunkPacketProcessor();
		processor.world = world;
		processor.chunkX = ints.read(0); 	 // packet.a;
		processor.chunkZ = ints.read(1); 	 // packet.b;
		processor.chunkMask = ints.read(2);  // packet.c;
		processor.extraMask = ints.read(3);  // packet.d;
		processor.data = byteArray.read(1);  // packet.inflatedBuffer;
		processor.startIndex = 0;

		if (packet.getBooleans().size() > 0) {
			processor.hasContinuous = packet.getBooleans().read(0);
		}
		return processor;
	}

	/**
	 * Construct an array of chunk packet processors from a given MAP_CHUNK_BULK
	 * packet.
	 * 
	 * @param packet - the map chunk bulk packet.
	 * @return The chunk packet processors.
	 */
	// The MAP_CHUNK_BULK packet no longer exists
	/*
	 * public static ChunkPacketProcessor[] fromMapBulkPacket(PacketContainer
	 * packet, World world) {
	 * if (!packet.getType().equals(PacketType.Play.Server.MAP_CHUNK_BULK))
	 * throw new IllegalArgumentException(packet +
	 * " must be a MAP_CHUNK_BULK packet.");
	 * StructureModifier<int[]> intArrays = packet.getIntegerArrays();
	 * StructureModifier<byte[]> byteArrays = packet.getByteArrays();
	 * int[] x = intArrays.read(0); // packet.c;
	 * int[] z = intArrays.read(1); // packet.d;
	 * ChunkPacketProcessor[] processors = new ChunkPacketProcessor[x.length];
	 * int[] chunkMask = intArrays.read(2); // packet.a;
	 * int[] extraMask = intArrays.read(3); // packet.b;
	 * int dataStartIndex = 0;
	 * for (int chunkNum = 0; chunkNum < processors.length; chunkNum++) {
	 * // Create an info objects
	 * ChunkPacketProcessor processor = new ChunkPacketProcessor();
	 * processors[chunkNum] = processor;
	 * processor.world = world;
	 * processor.chunkX = x[chunkNum];
	 * processor.chunkZ = z[chunkNum];
	 * processor.chunkMask = chunkMask[chunkNum];
	 * processor.extraMask = extraMask[chunkNum];
	 * processor.hasContinuous = true; // Always true
	 * processor.data = byteArrays.read(1); //packet.buildBuffer;
	 * // Check for Spigot
	 * if (processor.data == null || processor.data.length == 0) {
	 * processor.data =
	 * packet.getSpecificModifier(byte[][].class).read(0)[chunkNum];
	 * } else {
	 * processor.startIndex = dataStartIndex;
	 * }
	 * dataStartIndex += processor.size;
	 * }
	 * return processors;
	 * }
	 */

	/**
	 * Begin processing the current chunk with the provided processor.
	 * 
	 * @param processor - the processor that will process the chunk.
	 */
	public void process(ChunkletProcessor processor) {
		// Compute chunk number
		for (int i = 0; i < CHUNK_SEGMENTS; i++) {
			if ((chunkMask & (1 << i)) > 0) {
				chunkSectionNumber++;
			}
			if ((extraMask & (1 << i)) > 0) {
				extraSectionNumber++;
			}
		}

		int skylightCount = getSkylightCount();

		// The total size of a chunk is the number of blocks sent (depends on the number of sections) multiplied by the
		// amount of bytes per block. This last figure can be calculated by adding together all the data parts:
		//   For any block:
		//    * Block ID          -   8 bits per block (byte)
		//    * Block metadata    -   4 bits per block (nibble)
		//    * Block light array -   4 bits per block
		//   If 'worldProvider.skylight' is TRUE
		//    * Sky light array   -   4 bits per block
		//   If the segment has extra data:
		//    * Add array         -   4 bits per block
		//   Biome array - only if the entire chunk (has continous) is sent:
		//    * Biome array       -   256 bytes
		//
		// A section has 16 * 16 * 16 = 4096 blocks.
		size =
				BYTES_PER_NIBBLE_PART
						* ((NIBBLES_REQUIRED + skylightCount)
								* chunkSectionNumber + extraSectionNumber)
						+ (hasContinuous ? BIOME_ARRAY_LENGTH : 0);

		if ((getOffset(2) - startIndex) > data.length) {
			return;
		}

		// Make sure the chunk is loaded
		if (isChunkLoaded(world, chunkX, chunkZ)) {
			translate(processor);
		}
	}

	/**
	 * Retrieve the number of 2048 byte segments per chunklet.
	 * <p<
	 * This is usually one for The Overworld, and zero for both The End and The
	 * Nether.
	 * 
	 * @return Number of skylight byte segments.
	 */
	protected int getSkylightCount() {
		// There's no sun/moon in the end or in the nether, so Minecraft doesn't sent any skylight information
		// This optimization was added in 1.4.6. Note that ideally you should get this from the "f" (skylight) field.
		return world.getEnvironment() == Environment.NORMAL ? 1 : 0;
	}

	private int getOffset(int nibbles) {
		return startIndex
				+ (nibbles * chunkSectionNumber * ChunkPacketProcessor.BYTES_PER_NIBBLE_PART);
	}

	private void translate(ChunkletProcessor processor) {
		// Loop over 16x16x16 chunks in the 16x256x16 column
		int current = 4;
		ChunkOffsets offsets =
				new ChunkOffsets(getOffset(0), getOffset(2), getOffset(3),
						getSkylightCount() > 0 ? getOffset(current++) : -1,
						extraSectionNumber > 0 ? getOffset(current++) : -1);

		for (int i = 0; i < 16; i++) {
			// If the bitmask indicates this chunk is sent
			if ((chunkMask & 1 << i) > 0) {
				// The lowest block (in x, y, z) in this chunklet
				Location origin =
						new Location(world, chunkX << 4, i * 16, chunkZ << 4);

				processor.processChunklet(origin, data, offsets);
				offsets.incrementIdIndex();
			}
			if ((extraMask & 1 << i) > 0) {
				offsets.incrementExtraIndex();
			}
		}

		if (hasContinuous) {
			processor.processBiomeArray(new Location(world, chunkX << 4, 0,
					chunkZ << 4), data, startIndex + size - BIOME_ARRAY_LENGTH);
		}
	}

	private boolean isChunkLoaded(World world, int x, int z) {
		return world.isChunkLoaded(x, z);
	}
}
