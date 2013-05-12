package net.extrabiomes.generation.biomes;

import net.extrabiomes.decorators.ExtraBiomesDecorator;
import net.extrabiomes.decorators.*;
import net.extrabiomes.terraincontrol.BiomeGenCustom;

public class YellowStone extends ExtraBiomesBiome{

	public YellowStone(BiomeGenCustom biome) {
		super(biome);
		
		//TODO make decorators static/stored somewhere
		decorators.add(new DecoratorCount(new Mesa(), 1));		
	}
	
	

}
