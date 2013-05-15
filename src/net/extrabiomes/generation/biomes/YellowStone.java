package net.extrabiomes.generation.biomes;

import java.util.Random;

import net.extrabiomes.decorators.*;
import net.extrabiomes.terraincontrol.BiomeGenCustom;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class YellowStone extends ExtraBiomesBiome{

	public YellowStone(int id, String name) {
		super(id, name);
		
		topBlock = (byte) Block.sand.blockID;
		maxHeight = 0.2f;
		color = 0xffaacc;
		
		
		
		
		
		//TODO make decorators static/stored somewhere
		decorators.add(new DecoratorCount(new Mesa(), 1));		
	}
	
	@Override
	public void decorate(World par1World, Random par2Random, int par3, int par4) {
		
	}
	
	

}
