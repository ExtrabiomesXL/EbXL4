package ebxl4.world.biome;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.WorldChunkManager;

public class EbXL4ChunkManager extends WorldChunkManager {

  /** The BiomeCache object for this world. */
  private BiomeCache biomeCache;

  /** A list of biomes that the player can spawn in. */
  private List biomesToSpawnIn;
    
  public EbXL4ChunkManager(){
    this.biomeCache = new BiomeCache(this);
    this.biomesToSpawnIn = new ArrayList();
    this.biomesToSpawnIn.addAll(allowedBiomes);
  }
  
  public EbXL4ChunkManager(long par1, WorldType type){
    this();
    
    //
  }
  
  public EbXL4ChunkManager(World world){
    this(world.getSeed(), world.getWorldInfo().getTerrainType());
  }
}
