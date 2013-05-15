package net.extrabiomes.generation.biomes;

import java.util.ArrayList;

import net.extrabiomes.decorators.ExtraBiomesDecorator;
import net.extrabiomes.terraincontrol.Biome;
import net.extrabiomes.terraincontrol.BiomeGenCustom;
import net.minecraft.world.chunk.Chunk;

public abstract class ExtraBiomesBiome extends BiomeGenCustom{
	
	/**
	 * 
	 */
	public ArrayList<DecoratorCount> decorators;

	public Biome tcBiome;
	
	public ExtraBiomesBiome(int id, String name) {
		super(id, name);
		
		tcBiome = new Biome(this);
		
		decorators = new ArrayList<DecoratorCount>();
	}
	
	public void decorate(Chunk chunk){
		for(DecoratorCount d: decorators){
			for(int i = 0; i < d.attempts; i++){
				d.decorator.decorate(chunk);
			}
		}
	}



}

/**
 * @author Chris
 * utility class to hold a combination of a decorator and the number of times it should be run per chunk
 */
class DecoratorCount{
	public int attempts;
	public ExtraBiomesDecorator decorator;
	
	public DecoratorCount(ExtraBiomesDecorator d, int a){
		this.attempts = a;
		this.decorator = d;
	}
}
