package net.extrabiomes.world;

import java.util.ArrayList;

import com.khorn.terraincontrol.LocalWorld;
import com.khorn.terraincontrol.TerrainControl;

import net.extrabiomes.ExtrabiomesXL;
import net.extrabiomes.generation.ExtraBiomesWorldGenerator;
import net.extrabiomes.generation.biomes.ExtraBiomesBiome;

/**
 * @author Chris
 * Class to manager all of the biomes added by EBXL
 */
public class BiomeManager {
	
	public ArrayList<ExtraBiomesBiome> Biomes;
	
	public BiomeManager(){
		Biomes = new ArrayList<ExtraBiomesBiome>();
	}
	
	public void RegisterBiome(ExtraBiomesBiome biome){
		
		Biomes.add(biome);
		
	}
	
	public void RegisterBiome(ArrayList<ExtraBiomesBiome> biomes){
		
		Biomes.addAll(biomes);
		
	}
	

	/**
	 * @param world The TerrainControl world to add biomes to
	 *  Registers all the biomes registered with this Biome Manager with the given TerrainControl world.
	 *  
	 */
	public void RegisterBiomesForWorldGen(LocalWorld world){
		for(ExtraBiomesBiome b : Biomes){
			try{
				world.AddCustomBiome(b);
			}catch(Exception e){
				System.err.println("Error adding biome " + b.biomeName + "\n" + e.getStackTrace());
			}
			
		}
	}
	

}
