package net.extrabiomes.generation;

import net.extrabiomes.generation.layers.LayerManager;
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
	
	public ExtraBiomesWorldGenerator(){
		
		layerManager = new LayerManager();
		
		instance = this;
	}
	
	
	/**
	 * This method is called before a chunk is decorated and allows the terrain in that chunk to be modifed before decorations are placed
	 */
	public void PreDecorate(Chunk chunk){
		int i = 1;
	}
	
	
	/**
	 * This method is called after a chunk is decorated and allows the terrain and placed decorations to be modified
	 */
	public void PostDecorate(Chunk chunk){
		
	}
	
	
	/**
	 * This method is called after the chunk terrain had been generated but is still an array of bytes representing blocks values. Blocks added here
	 * must have IDs less than 256.
	 * @param arrayOfBlocks
	 */
	public void PostGenerate(byte[] arrayOfBlocks){
		
	}

}
