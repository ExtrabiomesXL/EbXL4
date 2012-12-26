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

public class ReedGen extends Resource
{

    private int blockId;
    private int blockData;
    private int minAltitude;
    private int maxAltitude;
    private List<Integer> sourceBlocks;
    EventType eventType;

    @Override
    public void process(LocalWorld world, Random random, int chunkX, int chunkZ) {
        if (TerrainGen.decorate(world.getMCWorld(), random, chunkX, chunkZ, eventType))
            super.process(world, random, chunkX, chunkZ);
    }

    @Override
    public void spawn(LocalWorld world, Random rand, int x, int z)
    {
        int y = world.getHighestBlockYAt(x, z);
        if (y > maxAltitude
                || y < minAltitude
                || (!world.getMaterial(x - 1, y - 1, z).isLiquid() && !world.getMaterial(x + 1, y - 1, z).isLiquid() && !world.getMaterial(x, y - 1, z - 1).isLiquid() && !world.getMaterial(x, y - 1,
                        z + 1).isLiquid()))
        {
            return;
        }
        if (!sourceBlocks.contains(world.getTypeId(x, y - 1, z)))
        {
            return;
        }

        int n = 1 + rand.nextInt(2);
        for (int i1 = 0; i1 < n; i1++)
            world.setBlock(x, y + i1, z, blockId, blockData, false, false, false);
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
        eventType = (blockId == DefaultMaterial.SUGAR_CANE_BLOCK.id)? EventType.REED
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

    @Override
    public String makeString()
    {
        return "Reed(" + makeMaterial(blockId, blockData) + "," + frequency + "," + rarity + "," + minAltitude + "," + maxAltitude + makeMaterial(sourceBlocks) + ")";
    }
}