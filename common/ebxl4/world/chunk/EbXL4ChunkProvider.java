package ebxl4.world.chunk;

import java.util.List;
import java.util.Random;

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
  
  public EbXL4ChunkProvider(World world, long seed, boolean structure, String options) {
    this.worldObj = world;
    this.random = new Random(seed);
  }

  @Override
  public boolean chunkExists(int x, int z) {
    return true;
  }

  @Override
  public Chunk provideChunk(int chunkX, int chunkZ) {
    // TODO Auto-generated method stub
    //byte[] data = new byte[65536];
    
    //TallChunk tallchunk = new TallChunk(this.worldObj, data, chunkX, chunkZ);
    Chunk chunk = new Chunk(this.worldObj, chunkX, chunkZ);
        
    ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[0];
    
    if (extendedblockstorage == null) {
        extendedblockstorage = new ExtendedBlockStorage(0, !this.worldObj.provider.hasNoSky);
        chunk.getBlockStorageArray()[0] = extendedblockstorage;
    }
    
    for(int x = 0; x < 16; ++x) {
      for(int z = 0; z < 16; ++z) {
        extendedblockstorage.setExtBlockID(x, 0, z, 2);
      }
    }
    
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
