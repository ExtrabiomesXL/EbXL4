package net.extrabiomes.terraincontrol.generator.resourcegens;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.extrabiomes.terraincontrol.DefaultMaterial;
import net.extrabiomes.terraincontrol.LocalWorld;
import net.extrabiomes.terraincontrol.exception.InvalidResourceException;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;

public class GrassGen extends Resource
{
    private int blockId;
    private int blockData;
    private List<Integer> sourceBlocks;
    private EventType eventType;

    @Override
    public void load(List<String> args) throws InvalidResourceException
    {
        if (args.size() < 5)
        {
            throw new InvalidResourceException("Too few arguments supplied");
        }
        blockId = getBlockId(args.get(0));
        blockData = getInt(args.get(1), 0, 16);
        eventType = (blockId == DefaultMaterial.LONG_GRASS.id) ? EventType.GRASS
                  : (blockId == DefaultMaterial.DEAD_BUSH.id) ? EventType.DEAD_BUSH
                  : EventType.CUSTOM;
        frequency = getInt(args.get(2), 1, 500);
        rarity = getInt(args.get(3), 1, 100);
        sourceBlocks = new ArrayList<Integer>();
        for (int i = 4; i < args.size(); i++)
        {
            sourceBlocks.add(getBlockId(args.get(i)));
        }
    }

    @Override
    public void spawn(LocalWorld world, Random random, int x, int z)
    {
        // Handled by process().
    }

    @Override
    public void process(LocalWorld world, Random random, int chunkX, int chunkZ)
    {
        if (TerrainGen.decorate(world.getMCWorld(), random, chunkX, chunkZ, eventType))
        for (int t = 0; t < frequency; t++)
        {
            if (random.nextInt(100) >= rarity)
                continue;
            int x = chunkX * 16 + random.nextInt(16) + 8;
            int z = chunkZ * 16 + random.nextInt(16) + 8;
            int y = world.getHighestBlockYAt(x, z);

            int i;
            while ((((i = world.getTypeId(x, y, z)) == 0) || (i == DefaultMaterial.LEAVES.id)) && (y > 0))
                y--;

            if ((!world.isEmpty(x, y + 1, z)) || (!sourceBlocks.contains(world.getTypeId(x, y, z))))
                continue;
            world.setBlock(x, y + 1, z, blockId, blockData, false, false, false);
        }
    }

    @Override
    public String makeString()
    {
        return "Grass(" + makeMaterial(blockId) + "," + blockData + "," + frequency + "," + rarity + makeMaterial(sourceBlocks) + ")";
    }
}