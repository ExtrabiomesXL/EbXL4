package net.extrabiomes.decorators;

import java.util.Random;

import net.extrabiomes.terraincontrol.SingleWorld;
import net.minecraft.world.chunk.Chunk;

public abstract class ExtraBiomesDecorator {
	
	Random random;
	SingleWorld world;
	
	public ExtraBiomesDecorator(SingleWorld world){
		this.world = world;
		random = new Random(world.getSeed());
	}
	
	public void decorate(Chunk c){
		
	}

}
