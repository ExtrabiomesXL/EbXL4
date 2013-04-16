
package net.extrabiomes.terraincontrol;

import java.util.List;

import net.minecraft.block.BlockSand;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import com.khorn.terraincontrol.configuration.WorldConfig;
import com.khorn.terraincontrol.generator.ChunkProviderTC;
import com.khorn.terraincontrol.generator.ObjectSpawner;

public class ChunkProvider implements IChunkProvider
{

    private final SingleWorld     world;
    private final World           worldHandle;
    private boolean               TestMode = false;

    private final ChunkProviderTC generator;
    private final ObjectSpawner   spawner;

    public ChunkProvider(final SingleWorld _world) {
        // super(_world.getWorld(), _world.getSeed());

        world = _world;
        worldHandle = _world.getWorld();

        TestMode = world.getSettings().ModeTerrain == WorldConfig.TerrainMode.TerrainTest;

        generator = new ChunkProviderTC(world.getSettings(), world);
        spawner = new ObjectSpawner(world.getSettings(), world);

    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public boolean chunkExists(final int i, final int i1) {
        return true;
    }

    @Override
    public ChunkPosition findClosestStructure(final World world, final String s, final int x,
            final int y, final int z)
    {
        if ("Stronghold".equals(s) && this.world.strongholdGen != null)
            return this.world.strongholdGen.getNearestInstance(world, x, y, z);
        return null;
    }

    @Override
    public int getLoadedChunkCount() {
        return 0;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List getPossibleCreatures(final EnumCreatureType paramaca, final int paramInt1,
            final int paramInt2, final int paramInt3)
    {
        final BiomeGenBase Biome = worldHandle.getBiomeGenForCoords(paramInt1, paramInt3);
        if (Biome == null) return null;
        return Biome.getSpawnableList(paramaca);
    }

    @Override
    public Chunk loadChunk(final int i, final int i1) {
        return provideChunk(i, i1);
    }

    @Override
    public String makeString() {
        return "TerrainControlLevelSource";
    }

    @Override
    public void populate(final IChunkProvider ChunkProvider, final int x, final int z) {
        if (TestMode) return;
        BlockSand.fallInstantly = true;
        world.LoadChunk(x, z);
        spawner.populate(x, z);
        BlockSand.fallInstantly = false;
    }

    @Override
    public Chunk provideChunk(final int chunkX, final int chunkZ) {
        final Chunk chunk = new Chunk(worldHandle, chunkX, chunkZ);

        final byte[] BlockArray = generator.generate(chunkX, chunkZ);
        final ExtendedBlockStorage[] sections = chunk.getBlockStorageArray();

        final int i1 = BlockArray.length / 256;
        for (int blockX = 0; blockX < 16; blockX++)
            for (int blockZ = 0; blockZ < 16; blockZ++)
                for (int blockY = 0; blockY < i1; blockY++) {
                    final int block = BlockArray[blockX << world.getHeightBits() + 4
                            | blockZ << world.getHeightBits() | blockY];
                    if (block != 0) {
                        final int sectionId = blockY >> 4;
                        if (sections[sectionId] == null) // Second
                                                         // argument is
                                                         // skylight
                        	sections[sectionId] = new ExtendedBlockStorage(sectionId << 4, !chunk.worldObj.provider.hasNoSky);
                        sections[sectionId].setExtBlockID(blockX, blockY & 0xF, blockZ, block);
                    }
                }
        world.FillChunkForBiomes(chunk, chunkX, chunkZ);

        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void recreateStructures(final int chunkX, final int chunkZ) {
        // TODO What should this do?
    }

    @Override
    public boolean saveChunks(final boolean b, final IProgressUpdate il) {
        return true;
    }

    /*@Override
    public boolean unload100OldestChunks() {
        return false;
    }*/
    
    //This seems to have replaced unload100OldestChunks?
	@Override
	public boolean unloadQueuedChunks() {
		// TODO Auto-generated method stub
		return false;
	}
}
