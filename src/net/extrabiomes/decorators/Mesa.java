package net.extrabiomes.decorators;

import java.util.Random;

import net.extrabiomes.terraincontrol.SingleWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenerator;

public class Mesa extends WorldGenerator{
	
	public Mesa(SingleWorld world) {
		super();
		// TODO Auto-generated constructor stub
	}

	int maxHeight = 50;
	int minHeight = 30;
	int maxWidth = 60;
	int minWidth = 30;
	
	@Override
	public boolean generate(World world, Random random, int i, int j, int k) {
		int widthX = minWidth + random.nextInt(maxWidth - minWidth);
		int widthZ = minWidth + random.nextInt(maxWidth - minWidth);
		
		int[][] shape = new int[widthX][widthZ];
		return false;
	}
	
	/*private int[][] shrink(int[][] previousLayer, int layer){
		
	}*/

}
