package ebxl4.world.biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class EbXL4ChunkManager extends WorldChunkManager {

  /** The BiomeCache object for this world. */
  private BiomeCache biomeCache;
  private GenLayer biomeIndexLayer;
  private GenLayer genBiomes;

  /** A list of biomes that the player can spawn in. */
  private List biomesToSpawnIn;

  public EbXL4ChunkManager() {
    this.biomeCache = new BiomeCache(this);
    this.biomesToSpawnIn = new ArrayList();
    this.biomesToSpawnIn.addAll(allowedBiomes);
  }

  public EbXL4ChunkManager(long par1, WorldType type) {
    this();

    //
    GenLayer[] agenlayer = GenLayer.initializeAllBiomeGenerators(par1, type);
    agenlayer = getModdedBiomeGenerators(type, par1, agenlayer);
    this.genBiomes = agenlayer[0];
    this.biomeIndexLayer = agenlayer[1];
    
  }

  public EbXL4ChunkManager(World world) {
    this(world.getSeed(), world.getWorldInfo().getTerrainType());
  }

  /**
   * Returns a list of rainfall values for the specified blocks.
   */
  public float[] getRainfall(float[] listToReuse, int x, int y, int width, int length) {
    IntCache.resetIntCache();

    if (listToReuse == null || listToReuse.length < width * length) {
      listToReuse = new float[width * length];
    }

    for (int index = 0; index < width * length; ++index) {
      listToReuse[index] = 1.0F;
    }

    return listToReuse;
  }

  @SideOnly(Side.CLIENT)
  /**
   * Return an adjusted version of a given temperature based on the y height
   */
  public float getTemperatureAtHeight(float par1, int par2) {
    return par1;
  }

  /**
   * Returns a list of temperatures to use for the specified blocks. Args:
   * listToReuse, x, y, width, length
   */
  public float[] getTemperatures(float[] listToReuse, int x, int y, int width, int length) {
    IntCache.resetIntCache();

    if (listToReuse == null || listToReuse.length < width * length) {
      listToReuse = new float[width * length];
    }

    for (int index = 0; index < width * length; ++index) {
      listToReuse[index] = 1.0F;
    }

    return listToReuse;
  }

  /**
   * checks given Chunk's Biomes against List of allowed ones
   */
  public boolean areBiomesViable(int par1, int par2, int par3, List par4List) {
    return true;
  }
  
  /**
   * Finds a valid position within a range, that is in one of the listed biomes. Searches {par1,par2} +-par3 blocks.
   * Strongly favors positive y positions.
   */
  public ChunkPosition findBiomePosition(int par1, int par2, int par3, List par4List, Random par5Random) {
      IntCache.resetIntCache();
       //chunkposition = new ChunkPosition(l2, 0, i3);
       

      return null;
  }
  
  public BiomeGenBase getBiomeGenAt(int chunkX, int chunkZ) {
    return BiomeGenBase.plains;
  }

}
