package ebxl4.world.chunk;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class TallChunk extends Chunk {

  public TallChunk(World par1World, byte[] data, int par3, int par4) {
    super(par1World, par3, par4);

    ExtendedBlockStorage[] storageArrays = getBlockStorageArray();

    int height = data.length / 256;

    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        for (int y = 0; y < height; y++) {
          int block = data[(x << 12 | z << 8 | y)] & 0xFF;

          if (block == 0)
            continue;
          int offset = y >> 4;

          if (storageArrays[offset] == null) {
            storageArrays[offset] = new ExtendedBlockStorage(offset << 4, !par1World.provider.hasNoSky);
          }

          storageArrays[offset].setExtBlockID(x, y & 0xF, z, block);
        }
      }
    }
  }
}
