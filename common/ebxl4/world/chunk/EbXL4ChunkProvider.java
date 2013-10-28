package ebxl4.world.chunk;

import java.util.List;
import java.util.Random;

import ebxl4.world.EbXL4WorldType;
import ebxl4.world.noise.SimplexNoise;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class EbXL4ChunkProvider implements IChunkProvider {
  
  private World worldObj;
  private Random random;
  private short[] blockIDs;
  private byte[] metaDataIDs;
  private int generationHeight;
  
  private SimplexNoise layer1;
  
  
  public EbXL4ChunkProvider(World world, long seed, boolean structure, String options) {
    this.worldObj = world;
    this.random = new Random(seed);
    this.generationHeight = ((EbXL4WorldType)world.getWorldInfo().getTerrainType()).getGenerationHeight();
    this.blockIDs = new short[256 * this.generationHeight];
    this.metaDataIDs = new byte[256 * this.generationHeight];
        
    this.layer1 = new SimplexNoise(random.nextLong());
  }

  @Override
  public boolean chunkExists(int x, int z) {
    return true;
  }

  @Override
  public Chunk provideChunk(int chunkX, int chunkZ) {
    // Fill the world chunk provider
    for(int y = 0; y < this.generationHeight; ++y) {
      for(int z = 0; z < 16; ++z) {
        for(int x = 0; x < 16; x++) {
          int idOffset = y << 8 | z << 4 | x;
          
          // Block Metadata
          metaDataIDs[idOffset] = 0;
          
          if(y == 0) {
            // Bedrock
            blockIDs[idOffset] = 7;
          } else if(y < 15) {
            // Stone
            blockIDs[idOffset] = 1;
          } else if(y == 15) {
            // Get the noise
            int levels = 8;           // How many itterations we should use
            double rate = 2.125D;     // Factor or rate
            double detail = 5120D;    // How zoomed in we should get
            double fraction = 0.76D;  // How much each layer counts towards the total
            
            // Starting constants
            double pow = 1D;
            double val = 0D;
            
            for(int j = 1; j < levels; j++) {
              val += layer1.noise(((chunkX << 4) | x) / detail, ((chunkZ << 4) | z) / detail) * pow;
              detail /= rate;
              pow *= fraction;
            }
            val += layer1.noise(((chunkX << 4) | x) / detail, ((chunkZ << 4) | z) / detail) * pow;

            // Grass
            //blockIDs[idOffset] = 159;
            if(val > 0.0D) {
              blockIDs[idOffset] = 2;
            } else {
              blockIDs[idOffset] = 9;
            }
          } else {
            // Air
            blockIDs[idOffset] = 0;
          }
          
        }
      }
    }
    
    
    //TallChunk tallchunk = new TallChunk(this.worldObj, data, chunkX, chunkZ);
    Chunk chunk = new Chunk(this.worldObj, blockIDs, metaDataIDs, chunkX, chunkZ);
        
    //ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[0];
    
    //if (extendedblockstorage == null) {
    //    extendedblockstorage = new ExtendedBlockStorage(0, !this.worldObj.provider.hasNoSky);
    //    chunk.getBlockStorageArray()[0] = extendedblockstorage;
    //}
    
    //for(int x = 0; x < 16; ++x) {
    //  for(int z = 0; z < 16; ++z) {
    //    extendedblockstorage.setExtBlockID(x, 0, z, 2);
    //  }
    //}
    
    //chunk.generateHeightMap();
    
    //BiomeGenBase[] abiomegenbase = this.worldObj.getWorldChunkManager().loadBlockGeneratorData((BiomeGenBase[])null, chunkX * 16, chunkZ * 16, 16, 16);
    byte[] abyte = chunk.getBiomeArray();
    
    for (int k1 = 0; k1 < abyte.length; ++k1) {
        abyte[k1] = (byte)0;
    }
    
    chunk.generateSkylightMap();
    
    return chunk;
  }

  @Override
  public Chunk loadChunk(int x, int z) {
    return this.provideChunk(x, z);
  }

  @Override
  public void populate(IChunkProvider ichunkprovider, int x, int z) {
    // TODO Add population code
  }

  @Override
  public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
    return true;
  }

  @Override
  public boolean unloadQueuedChunks() {
    return false;
  }

  @Override
  public boolean canSave() {
    return true;
  }

  @Override
  public String makeString() {
    return "EbXL++LevelSource";
  }

  @Override
  public List getPossibleCreatures(EnumCreatureType enumCreatureType, int x, int y, int z) {
    BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(x, z);
    return biomegenbase == null ? null : biomegenbase.getSpawnableList(enumCreatureType);
  }

  @Override
  public ChunkPosition findClosestStructure(World world, String structureType, int x, int y, int z) {
    return null;
  }

  @Override
  public int getLoadedChunkCount() {
    return 0;
  }

  @Override
  public void recreateStructures(int x, int z) {
    // TODO Generate structures
  }

  @Override
  public void saveExtraData() {}

}
