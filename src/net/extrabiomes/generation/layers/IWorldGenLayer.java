package net.extrabiomes.generation.layers;

/**
 * @author Chris
 * This interface defines the blueprint that all layer types will implement
 *  These layers work similarly to the vanilla biome shape layer. EBXL will use a combination of layers to create unique terrain that is defined by
 *  multiple inputs.
 */
public interface IWorldGenLayer {
	
	double[] getValues(int x, int z, int rangeX, int rangeZ);
	
	double getValue(int x, int z);
}
