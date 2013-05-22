package net.extrabiomes.generation.biomes;

import java.util.ArrayList;

import net.extrabiomes.decorators.ExtraBiomesDecorator;
import net.extrabiomes.terraincontrol.Biome;
import net.extrabiomes.terraincontrol.BiomeGenCustom;
import net.minecraft.world.chunk.Chunk;

public abstract class ExtraBiomesBiome extends BiomeGenCustom{
	
	public enum BiomeType{
		Normal,
		Ice,
		Isle,
		Border
	}
	/**
	 * 
	 */
	public ArrayList<DecoratorCount> decorators;

	public Biome tcBiome;
	
	public BiomeType biomeType;
	
	public ExtraBiomesBiome(int id, String name) {
		super(id, name);
		
		biomeType = BiomeType.Normal;
		
		isEBXL = true;
		
		tcBiome = new Biome(this);
		
		decorators = new ArrayList<DecoratorCount>();
	}
	
	@Override
	public void decorate(net.minecraft.world.World par1World, java.util.Random par2Random, int par3, int par4) {
		int a = 1;
	};
	
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
