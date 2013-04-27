package net.extrabiomes.blocks;

import java.util.List;

import net.minecraft.block.Block;

public interface IBlockManager {
	
	void registerBlock(Block block);
	
	List<Block> getBlocks();
	
	void registerBlocks(List<Block> blocks);

}
