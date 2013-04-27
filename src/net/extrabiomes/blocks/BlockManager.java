package net.extrabiomes.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;

/**
 * @author Chris
 *
 *	This class will manage all of the blocks created by EBXL.
 */
public class BlockManager implements IBlockManager{
	
	
	/**
	 * List to store all of the blocks registered with this BlockManager
	 */
	private List<Block> blocks;
	
	
	public BlockManager(){
		blocks = new ArrayList<Block>();
	}
	
	/**
	 * Registers a block with the BlockManager
	 */
	@Override
	public void registerBlock(Block block) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Block> getBlocks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerBlocks(List<Block> blocks) {
		// TODO Auto-generated method stub
		
	}

}
