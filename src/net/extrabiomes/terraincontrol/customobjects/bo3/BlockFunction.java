package net.extrabiomes.terraincontrol.customobjects.bo3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.extrabiomes.terraincontrol.LocalWorld;
import net.extrabiomes.terraincontrol.configuration.Tag;
import net.extrabiomes.terraincontrol.exception.InvalidResourceException;
import net.extrabiomes.terraincontrol.util.BlockHelper;

/**
 * Represents a block in a BO3.
 * 
 */
public class BlockFunction extends BO3Function
{
    public int blockId;
    public int blockData;
    public int x;
    public int y;
    public int z;

    public boolean hasMetaData;
    public List<Tag> metaDataTags;
    public List<String> metaDataNames;
    public List<Integer> metaDataChances;

    @Override
    public void load(List<String> args) throws InvalidResourceException
    {
        assureSize(4, args);
        blockId = getBlockId(args.get(0));
        blockData = getBlockData(args.get(0));
        x = getInt(args.get(1), -100, 100);
        y = getInt(args.get(2), -100, 100);
        z = getInt(args.get(3), -100, 100);
        metaDataTags = new ArrayList<Tag>();
        metaDataChances = new ArrayList<Integer>();
        metaDataNames = new ArrayList<String>();
        for (int i = 4; i < args.size() - 1; i += 2)
        {
            Tag tag = BO3Loader.loadMetadata(args.get(i), getHolder().file);
            if (tag != null)
            {
                metaDataTags.add(tag);
                metaDataNames.add(args.get(i));
                metaDataChances.add(getInt(args.get(i + 1), 1, 100));
                hasMetaData = true;
            }
        }
    }

    @Override
    public String makeString()
    {
        String start = "Block(" + makeMaterial(blockId, blockData) + "," + x + "," + y + "," + z;
        for (int i = 0; i < metaDataNames.size(); i++)
        {
            start += "," + metaDataNames.get(i) + "," + metaDataChances.get(i);
        }
        return start + ")";
    }

    public BlockFunction rotate()
    {
        BlockFunction rotatedBlock = new BlockFunction();
        rotatedBlock.x = z;
        rotatedBlock.y = y;
        rotatedBlock.z = -x;
        rotatedBlock.blockId = blockId;
        rotatedBlock.blockData = BlockHelper.RotateData(blockId, blockData);
        rotatedBlock.metaDataTags = metaDataTags;
        rotatedBlock.metaDataNames = metaDataNames;
        rotatedBlock.metaDataChances = metaDataChances;

        return rotatedBlock;
    }

    /**
     * Spawns this block at the position. The saved x, y and z in this block are ignored.
     * 
     * @param world
     * @param x
     * @param y
     * @param z
     */
    public void spawn(LocalWorld world, Random random, int x, int y, int z)
    {
        world.setBlock(x, y, z, blockId, blockData);
        if (hasMetaData)
        {
            for (int metaDataNumber = 0; metaDataNumber < metaDataTags.size(); metaDataNumber++)
            {
                if (random.nextInt(100) < metaDataChances.get(metaDataNumber))
                {
                    world.attachMetadata(x, y, z, metaDataTags.get(metaDataNumber));
                    break;
                }
            }
        }
    }
}
