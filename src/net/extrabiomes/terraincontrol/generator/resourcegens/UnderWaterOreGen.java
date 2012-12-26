package net.extrabiomes.terraincontrol.generator.resourcegens;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.extrabiomes.terraincontrol.DefaultMaterial;
import net.extrabiomes.terraincontrol.LocalWorld;
import net.extrabiomes.terraincontrol.exception.InvalidResourceException;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import net.minecraftforge.event.terraingen.TerrainGen;

public class UnderWaterOreGen extends Resource
{
    private int blockId;
    private List<Integer> sourceBlocks;
    private int size;
    private int blockData;
    private EventType eventType;

    @Override
    public void process(LocalWorld world, Random random, int chunkX, int chunkZ) {
        if (TerrainGen.decorate(world.getMCWorld(), random, chunkX, chunkZ, eventType))
            super.process(world, random, chunkX, chunkZ);
    }

    @Override
    public void spawn(LocalWorld world, Random rand, int x, int z)
    {
        int y = world.getSolidHeight(x, z);
        if (world.getLiquidHeight(x, z) < y || y == -1)
            return;

        int currentSize = rand.nextInt(size);
        int two = 2;
        for (int k = x - currentSize; k <= x + currentSize; k++)
        {
            for (int m = z - currentSize; m <= z + currentSize; m++)
            {
                int n = k - x;
                int i1 = m - z;
                if (n * n + i1 * i1 <= currentSize * currentSize)
                {
                    for (int i2 = y - two; i2 <= y + two; i2++)
                    {
                        int i3 = world.getTypeId(k, i2, m);
                        if (sourceBlocks.contains(i3))
                        {
                            world.setBlock(k, i2, m, blockId, blockData, false, false, false);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void load(List<String> args) throws InvalidResourceException
    {
        assureSize(5, args);
        blockId = getBlockId(args.get(0));
        blockData = getBlockData(args.get(0));
        eventType = (blockId == DefaultMaterial.CLAY.id) ? EventType.CLAY
                  : (blockId == DefaultMaterial.SAND.id) ? EventType.SAND
                  : EventType.CUSTOM;
        size = getInt(args.get(1), 1, 8);
        frequency = getInt(args.get(2), 1, 100);
        rarity = getInt(args.get(3), 1, 100);
        sourceBlocks = new ArrayList<Integer>();
        for (int i = 4; i < args.size(); i++)
        {
            sourceBlocks.add(getBlockId(args.get(i)));
        }
    }

    @Override
    public String makeString()
    {
        return "UnderWaterOre(" + makeMaterial(blockId, blockData) + "," + size + "," + frequency + "," + rarity + makeMaterial(sourceBlocks) + ")";
    }
}
