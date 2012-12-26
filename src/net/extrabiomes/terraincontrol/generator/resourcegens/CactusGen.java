package net.extrabiomes.terraincontrol.generator.resourcegens;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.extrabiomes.terraincontrol.DefaultMaterial;
import net.extrabiomes.terraincontrol.LocalWorld;
import net.extrabiomes.terraincontrol.TerrainControl;
import net.extrabiomes.terraincontrol.exception.InvalidResourceException;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;

public class CactusGen extends Resource
{
    private int blockId;
    private int blockData;
    private int minAltitude;
    private int maxAltitude;
    private List<Integer> sourceBlocks;
    private EventType eventType;

    @Override
    public void process(LocalWorld world, Random random, int chunkX, int chunkZ) {
        if (TerrainGen.decorate(world.getMCWorld(), random, chunkX, chunkZ, eventType))
            super.process(world, random, chunkX, chunkZ);
    }

    @Override
    public void spawn(LocalWorld world, Random rand, int x, int z)
    {
        int y = rand.nextInt(maxAltitude - minAltitude) + minAltitude;

        for (int i = 0; i < 10; i++)
        {
            int j = x + rand.nextInt(8) - rand.nextInt(8);
            int k = y + rand.nextInt(4) - rand.nextInt(4);
            int m = z + rand.nextInt(8) - rand.nextInt(8);
            if (world.isEmpty(j, k, m))
            {
                int n = 1 + rand.nextInt(rand.nextInt(3) + 1);
                for (int i1 = 0; i1 < n; i1++)
                {
                    int id = world.getTypeId(j, k + i1 - 1, m);
                    if (sourceBlocks.contains(id))
                    {
                        world.setBlock(j, k + i1, m, blockId, blockData, false, false, false);
                    }
                }
            }
        }
    }

    @Override
    public String makeString()
    {
        return "Cactus(" + makeMaterial(blockId, blockData) + "," + frequency + "," + rarity + "," + minAltitude + "," + maxAltitude + makeMaterial(sourceBlocks);
    }

    @Override
    public void load(List<String> args) throws InvalidResourceException
    {
        if (args.size() < 6)
        {
            throw new InvalidResourceException("Too few arguments supplied");
        }

        blockId = getBlockId(args.get(0));
        blockData = getBlockData(args.get(0));
        eventType = (blockId == DefaultMaterial.CACTUS.id) ? EventType.CACTUS
                  : EventType.CUSTOM;
        frequency = getInt(args.get(1), 1, 100);
        rarity = getInt(args.get(2), 1, 100);
        minAltitude = getInt(args.get(3), TerrainControl.worldDepth, TerrainControl.worldHeight);
        maxAltitude = getInt(args.get(4), minAltitude + 1, TerrainControl.worldHeight);
        sourceBlocks = new ArrayList<Integer>();
        for (int i = 5; i < args.size(); i++)
        {
            sourceBlocks.add(getBlockId(args.get(i)));
        }
    }
}