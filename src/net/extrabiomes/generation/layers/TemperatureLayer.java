package net.extrabiomes.generation.layers;

import net.extrabiomes.utility.PerlinNoiseGenerator;

/**
 * @author Chris
 * This layer defines a temperature range that will extend the default temperatures of biomes to give them a finer range of temperatures that vary within
 *  a biome. The values from this layer will modulate the biomes vanilla temperature value within a given range eg. +- 0.2
 *  
 *  Temperature will use a perlin noise function to generate psuedo random values that are contiguous
 */
public class TemperatureLayer implements IWorldGenLayer{

	public PerlinNoiseGenerator noiseGen;
	
	public TemperatureLayer(long seed){
		
		noiseGen = new PerlinNoiseGenerator(seed);
		
	}
	
	@Override
	public double[] getValues(int x, int z, int rangeX, int rangeZ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getValue(int x, int z) {
		return noiseGen.noise(x, z);
	}

}
