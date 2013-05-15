package net.extrabiomes.generation;

import net.extrabiomes.generation.layers.LayerManager;
import net.extrabiomes.terraincontrol.SingleWorld;
import net.minecraft.block.Block;
import net.minecraft.world.chunk.Chunk;


/**
 * @author Chris
 *
 *	This will be the entry point for all world gen stuff outside of Terrain Control
 *
 */
public class ExtraBiomesWorldGenerator {
	
	
	public static ExtraBiomesWorldGenerator instance;
	
	public LayerManager layerManager;
	
	public ExtraBiomesWorldGenerator(SingleWorld world){
		
		layerManager = new LayerManager(world);
		
		instance = this;
	}
	
	/**
	 * This method is called before a chunk is decorated and allows the terrain in that chunk to be modifed before decorations are placed
	 */
	public void PreDecorate(Chunk chunk){
		
	}
	
	
	/**
	 * This method is called after a chunk is decorated and allows the terrain and placed decorations to be modified
	 */
	public void PostDecorate(Chunk chunk){
		
		/*double[] features = layerManager.featureLayer.getValues(chunk.xPosition * 16, chunk.zPosition + 16, 16, 16);
		
		for(int x = 0; x < 16; x++){
			for(int z = 0; z < 16; z++){
				double temp = layerManager.temperatureLayer.getValue(chunk.xPosition * 16 + x, chunk.zPosition + 16 + z);
				if(temp > 0.1){
					chunk.setBlockIDWithMetadata(x, 70, z, Block.blockClay.blockID, 0);
				}
			}
		}*/
	}
	
	
	/**
	 * This method is called after the chunk terrain had been generated but is still an array of bytes representing blocks values. Blocks added here
	 * must have IDs less than 256.
	 * @param arrayOfBlocks
	 */
	public void PostGenerate(byte[] arrayOfBlocks){
		
	}

}
