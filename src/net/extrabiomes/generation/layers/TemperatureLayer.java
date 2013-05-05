package net.extrabiomes.generation.layers;

import net.extrabiomes.utility.PerlinNoiseGenerator;
import net.royawesome.jlibnoise.Noise;
import net.royawesome.jlibnoise.module.Module;
import net.royawesome.jlibnoise.module.source.Perlin;

/**
 * @author Chris
 * This layer defines a temperature range that will extend the default temperatures of biomes to give them a finer range of temperatures that vary within
 *  a biome. The values from this layer will modulate the biomes vanilla temperature value within a given range eg. +- 0.2
 *  
 *  Temperature will use a perlin noise function to generate psuedo random values that are contiguous
 */
public class TemperatureLayer implements IWorldGenLayer{

	//public PerlinNoiseGenerator noiseGen;
	public Perlin noiseGen;
	
	private int octaves = 8;
	private double amplitude = 1;
	private double frequency = 0.1;
	private double multiplyBy = 0.01;
	
	public TemperatureLayer(long seed){
		
		//noiseGen = new PerlinNoiseGenerator(seed);
		
		noiseGen = new Perlin();
		noiseGen.setSeed((int)seed);
		noiseGen.setOctaveCount(5);
		noiseGen.setFrequency(0.1);
		noiseGen.setPersistence(0.5);
		
	}
	

	@Override
	public double[] getValues(int x, int z, int rangeX, int rangeZ) {
		int range = rangeX * rangeZ;
		
		double[] temps = new double[range];
		for(int i = x - (rangeX / 2); i < x + (rangeX / 2); i++){
			for(int j = z - (rangeZ / 2); j < z + (rangeZ / 2); i++){
				temps[i * j] = getValue(i, j);
			}
		}
		
		return temps;
	}

	@Override
	public double getValue(int x, int z) {
		//return noiseGen.getNoise(x/divideBy, 0, z/divideBy, octaves, frequency, amplitude);
		return noiseGen.GetValue((double)x * multiplyBy, (double)z * multiplyBy, 0);
	}

}
