package net.extrabiomes.generation.layers;

/**
 * @author Chris
 * This interface defines the blueprint that all layer types will implement
 *  These layers work similarly to the vanilla biome shape layer. EBXL will use a combination of layers to create unique terrain that is defined by
 *  multiple inputs.
 */
public interface IWorldGenLayer {
	
	/**
	 * Gets an array of values centered on a given point. The range defines the number of values found in each axis. 
	 * 
	 * @param x The X coordinate at which to centre the the selected values at.
	 * @param z The Z coordinate at which to centre the the selected values at.
	 * @param rangeX The range in which to get values centered on the given X coordinate.
	 * @param rangeZ The range in which to get values centered on the given Y coordinate.
	 * @return An array of values which can be interpreted as needed based on the implementation of the layer
	 */
	double[] getValues(int x, int z, int rangeX, int rangeZ);
	
	double getValue(int x, int z);
}
