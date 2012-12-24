package net.extrabiomes.terraincontrol;

import java.util.ArrayList;
import java.util.Random;

import net.extrabiomes.terraincontrol.configuration.Tag;
import net.extrabiomes.terraincontrol.configuration.WorldConfig;
import net.extrabiomes.terraincontrol.generator.resourcegens.TreeType;
import net.minecraft.world.World;

public interface LocalWorld
{
    // Biome init
    public LocalBiome AddBiome(String name, int id);

    public LocalBiome getNullBiome(String name);

    public int getMaxBiomesCount(); // With static id allocation this is not a
                                    // required feature.

    public int getFreeBiomeId();

    public LocalBiome getBiomeById(int id);

    public int getBiomeIdByName(String name);

    public ArrayList<LocalBiome> getDefaultBiomes();

    // Biome manager
    public int[] getBiomesUnZoomed(int[] biomeArray, int x, int z, int x_size, int z_size);

    public float[] getTemperatures(int x, int z, int x_size, int z_size);

    public int[] getBiomes(int[] biomeArray, int x, int z, int x_size, int z_size);

    public int getCalculatedBiomeId(int x, int z);
    
    public World getMCWorld();

    /**
     * Calculates the biome that should generate at the given coordinates.
     * 
     * @param x
     *            The block x.
     * @param z
     *            The block z.
     * @return The biome at the given coordinates.
     */
    public LocalBiome getCalculatedBiome(int x, int z);

    /**
     * Gets the (stored) biome at the given coordinates.
     * 
     * @param x
     *            The block x.
     * @param z
     *            The block z.
     * @return The biome at the given coordinates.
     */
    public LocalBiome getBiome(int x, int z);

    // temperature*rain
    public double getBiomeFactorForOldBM(int index);

    // Default generators

    public void PrepareTerrainObjects(int x, int z, byte[] chunkArray, boolean dry);

    public void PlaceDungeons(Random rand, int x, int y, int z);

    public boolean PlaceTree(TreeType type, Random rand, int x, int y, int z);

    public boolean PlaceTerrainObjects(Random rand, int chunk_x, int chunk_z);

    public void replaceBlocks();
    
    public void replaceBiomesLate();

    // Blocks

    public int getTypeId(int x, int y, int z);
    
    public byte getTypeData(int x, int y, int z);

    public boolean isEmpty(int x, int y, int z);

    public void setBlock(final int x, final int y, final int z, final int typeId, final int data, final boolean updateLight, final boolean applyPhysics, final boolean notifyPlayers);

    public void setBlock(final int x, final int y, final int z, final int typeId, final int data);

    // public void setRawBlockIdAndData(int x, int y, int z, int BlockId, int
    // Data);

    // public void setRawBlockId(int x, int y, int z, int BlockId);

    // public void setBlockId(int x, int y, int z, int BlockId);

    // public void setBlockIdAndData(int x, int y, int z, int BlockId, int
    // Data);

    public int getLiquidHeight(int x, int z);

    public int getSolidHeight(int x, int z);

    public int getHighestBlockYAt(int x, int z);

    public DefaultMaterial getMaterial(int x, int y, int z);

    public void setChunksCreations(boolean createNew);

    public int getLightLevel(int x, int y, int z);

    public boolean isLoaded(int x, int y, int z);

    public WorldConfig getSettings();

    // public void setSettings(WorldConfig settings);

    public String getName();

    // Terrain init
    public long getSeed();

    public int getHeight();

    public int getHeightBits();

    public void setHeightBits(int heightBits);
    
    public void attachMetadata(int x, int y, int z, Tag tag);

}